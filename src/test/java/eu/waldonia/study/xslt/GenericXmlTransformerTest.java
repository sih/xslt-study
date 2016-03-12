	package eu.waldonia.study.xslt;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RdbTransformApplication.class)

public class GenericXmlTransformerTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(GenericXmlTransformer.class);

	@Autowired
	GenericXmlTransformer rdbToXml;

	private List<Map<String, Object>> table;
	private String xml;

	@Before
	public void setUp() {
		table = new ArrayList<Map<String, Object>>();
		Map<String, Object> columns = new HashMap<String, Object>();
		columns.put("id", 1);
		columns.put("name", "Alice");
		table.add(columns);

		xml = rdbToXml.makeGenericXml(table);
		LOG.info(xml);
	}

	@Test
	public void shouldProduceValidXml() {

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {

			Schema schema = factory.newSchema(new File("src/main/resources/xml/table.xsd"));

			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xml)));
			assertTrue(true);
		}

		catch (IOException | SAXException e) {
			fail("Shouldn't have thrown error " + e.getMessage());
		}
	}

	@Test
	public void shouldContainExpectedData() {

		LOG.info(xml);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
			doc.getDocumentElement().normalize();
			
			/*
			 * Navigate down the document
			 */
			
			// root == <resultSet>
			NodeList results = doc.getElementsByTagName("resultSet");
			assertTrue(results.getLength() == 1);
			Node resultSet = results.item(0);
			assertEquals("resultSet",resultSet.getNodeName());

			// <resultSet><rows>...</rows></resultSet>
			NodeList rows = resultSet.getChildNodes();
			assertTrue(rows.getLength() == 1);
			Node rowsElement = rows.item(0);
			assertNotNull(rowsElement);
			assertEquals("rows",rowsElement.getNodeName());
			

			// <resultSet><rows><row>...</row></rows></resultSet>
			NodeList rowChildren = rowsElement.getChildNodes();
			assertTrue(rowChildren.getLength() == 1);
			Node rowElement = rowChildren.item(0);
			assertNotNull(rowElement);
			assertEquals("row",rowElement.getNodeName());

			// <resultSet><rows><row><col>...</col></row></rows></resultSet>
			NodeList columns = rowElement.getChildNodes();
			assertTrue(columns.getLength() == 2);
			for (int i = 0; i < columns.getLength(); i++) {
				Node column = columns.item(i);
				assertEquals("col",column.getNodeName());
				
				// <resultSet><rows><row><col><name>...</name><value>...</value></col></row></rows></resultSet>
				NodeList colContents = column.getChildNodes();
				assertTrue(colContents.getLength() == 2);
				Node name = colContents.item(0);
				assertEquals("name", name.getNodeName());
				Node value = colContents.item(1);
				assertEquals("value", value.getNodeName());
				
				// finally check content
				if (name.getTextContent().equals("id")) {
					assertEquals("1", value.getTextContent());
				}
				else if (name.getTextContent().equals("name")) {
					assertEquals("Alice", value.getTextContent());
				}
				else {
					fail("Unexpected item in bagging area");
				}
			}
			
		}
		catch (SAXException | IOException | ParserConfigurationException e) {
				fail("Shouldn't have thrown error "+e.getMessage());			
		} 

		
	}

}
