package eu.waldonia.study.xslt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RdbTransformApplication.class)
public class RdbtransformApplicationTests {
	
	private static final Logger LOG = LoggerFactory.getLogger(RdbtransformApplicationTests.class);

	@Autowired
	Extractor extractor;
	
	@Autowired
	TableXmlConverter rdbToXml;

	
	@Test
	public void fullFlow() {
		String output = rdbToXml.makeGenericXml(extractor.getPersonDetails());
		LOG.info(output);
	}
	
	@Test
	public void contextLoads() {
	}

}
