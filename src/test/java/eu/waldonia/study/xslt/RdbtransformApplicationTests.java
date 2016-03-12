package eu.waldonia.study.xslt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RdbTransformApplication.class)
public class RdbtransformApplicationTests {

	@Autowired
	Extractor extractor;
	
	@Autowired
	GenericXmlTransformer rdbToXml;

	
	@Test
	public void fullFlow() {
		rdbToXml.makeGenericXml(extractor.getPersonDetails());
	}
	
	@Test
	public void contextLoads() {
	}

}
