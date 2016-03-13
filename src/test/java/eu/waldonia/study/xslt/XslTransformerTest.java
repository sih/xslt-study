package eu.waldonia.study.xslt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RdbTransformApplication.class)
public class XslTransformerTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(XslTransformerTest.class);
	

	@Autowired XslTransformer transformer;
	
	private static final String SOURCE_XML = "src/main/resources/xml/table.xml";
	private static final String SOURCE_XSL = "src/main/resources/xml/group.xslt";
	
	private Source xmlSource;
	private Source xslSource;
	
	@Before
	public void setUp() {
		xmlSource = new StreamSource(new File(SOURCE_XML));
		xslSource = new StreamSource(new File(SOURCE_XSL));
	}
	
	/**
	 * 
	 */
	@Test
	public void shouldTransformPersonData() {
		String result = transformer.transform(xmlSource, xslSource);
		assertNotNull(result);
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		try {
			Schema schema = factory.newSchema(new File("src/main/resources/xml/people.xsd"));

			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(result)));
			assertTrue(true);
		}
		
		catch(Exception e) {
			fail("Shouldn't have thrown exception "+e.getMessage());
		}

		
	}
}
