package eu.waldonia.study.xslt;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RdbTransformApplication.class)
public class ExtractorIntegrationTest {

	@Autowired
	Extractor extractor;

	
	private Map<String,Boolean> addresses;
	private Map<String,Boolean> postcodes;
	
	@Before
	public void setUp() {
		addresses = new HashMap<String,Boolean>();
		addresses.put("39 Woodland Rd", false);
		addresses.put("34 Dart Rd", false);
		addresses.put("Cedar House", false);
		addresses.put("112A High St", false);
		addresses.put("25 Cardigan St", false);
		addresses.put("1 Bulstrode Rd", false);
		addresses.put("19 Cecil Rd", false);
		addresses.put("13C Comeragh Rd", false);
		addresses.put("39A Dorothy Rd", false);

		postcodes = new HashMap<String,Boolean>();
		postcodes.put("SE19 1NU", false);
		postcodes.put("GU14 9PB", false);
		postcodes.put("CT6 6LA", false);
		postcodes.put("CT6 5JY", false);
		postcodes.put("OX2 6BP", false);
		postcodes.put("TW3 3AP", false);
		postcodes.put("TW3 1NU", false);
		postcodes.put("W14 9HP", false);
		postcodes.put("SW11 2JJ", false);
	}
	
	
	@Test
	public void shouldReturnPersonDetails() {
		List<Map<String,Object>> results = extractor.getPersonDetails();
		assertNotNull(results);
		assertEquals(9,results.size());
		for (Map<String, Object> cols : results) {
			assertNotNull(cols);
			assertEquals(new Integer(1),cols.get("id"));
			assertEquals("Sid Haniff",cols.get("name"));
			assertEquals("1966-04-13",cols.get("birthdate"));
			assertEquals("GB",cols.get("nationality"));
			checkAddress(cols.get("address").toString());
			checkPostcode(cols.get("postcode").toString());
		}
		
		postcodes.values().forEach(found -> assertTrue(found));
		addresses.values().forEach(found -> assertTrue(found));
	}

	private void checkPostcode(String value) {
		if (postcodes.containsKey(value)) {
			postcodes.put(value, true);
		}
	}

	private void checkAddress(String value) {
		if (addresses.containsKey(value)) {
			addresses.put(value, true);
		}
	}

}
