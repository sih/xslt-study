package eu.waldonia.study.xslt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Turn the extracted data in to a generic XML structure
 * @author sih
 */
@Component
public class GenericXmlTransformer {
	
	private static final String DIRECTIVE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final Logger LOG = LoggerFactory.getLogger(GenericXmlTransformer.class);
	
	public String makeGenericXml(List<Map<String,Object>> table) {
		StringBuffer buffy = new StringBuffer(DIRECTIVE);
		buffy.append("<resultSet>");
		buffy.append("<rows>");
		if (table != null && !table.isEmpty()) {
			for (Map<String, Object> cols : table) {
				buffy.append("<row>");
				Set<String> keys = cols.keySet();
				for (String key : keys) {
					buffy.append("<col>");
					buffy.append("<name>");
					buffy.append(key);
					buffy.append("</name>");
					buffy.append("<value>");
					buffy.append(cols.get(key));
					buffy.append("</value>");
					buffy.append("</col>");
				}
				buffy.append("</row>");
			}
		}
		buffy.append("</rows>");
		buffy.append("</resultSet>");
		
		LOG.debug(buffy.toString());
		
		return buffy.toString();
	}
	

}
