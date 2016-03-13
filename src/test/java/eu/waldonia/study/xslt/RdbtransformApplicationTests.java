package eu.waldonia.study.xslt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

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
public class RdbTransformApplicationTests {
	
	private static final Logger LOG = LoggerFactory.getLogger(RdbTransformApplicationTests.class);

	@Autowired
	Extractor extractor;
	
	@Autowired
	TableXmlConverter rdbToXml;
	
	@Autowired
	XslTransformer transformer;
	
	private static final String SOURCE_XSL = "src/main/resources/xml/group.xslt";
	
	private Source xslSource;
	
	@Before
	public void setUp() {
		xslSource = new StreamSource(new File(SOURCE_XSL));
	}


	
	@Test
	public void shouldProcessFullFlow() {
		
		LOG.info("***** Step 1 - Extract data as List *****");
		List<Map<String,Object>> extractedData = extractor.getPersonDetails();
		LOG.info(extractedData.size()+" rows extracted for two people");
		
		LOG.info("***** Step 2 - Turn in to generic XML *****");
		String output = rdbToXml.makeGenericXml(extractedData);
		LOG.info(output);

		Source xmlSource = new StreamSource(new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8)));
		
		LOG.info("***** Step 3 - Turn in to people XML *****");
		String result = transformer.transform(xmlSource, xslSource);
		LOG.info(output);
	}
	
	@Test
	public void contextLoads() {
	}

}
