package eu.waldonia.study.xslt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

/**
 * Extract the data from the db in a flat format (with repeating groups)
 * 
 * @author sih
 */
@Component
public class Extractor {

	private static final String SQL = "SELECT p.id, p.name, p.nationality, a.address_1, a.postcode FROM person p, address a WHERE a.person_id = p.id ORDER BY p.id";

	private static final Logger LOG = LoggerFactory.getLogger(Extractor.class);
	
	@Autowired
	JdbcTemplate jdbc;

	
	
	/**
	 * TODO change me to return a map of a list of people
	 * @return
	 */
	public List<Map<String, Object>> getPersonDetails() {
				
		LOG.info("Executing query: "+SQL);
		
		return jdbc.query(SQL, new ResultSetExtractor<List<Map<String, Object>>>() {
			
			List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();

			/*
			 * Add each of the columns in the row to the map 
			 */
			public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					Map<String, Object> columns = new HashMap<String, Object>();
					columns.put("id", rs.getInt("id"));
					columns.put("name", rs.getString("name"));
					columns.put("nationality", rs.getString("nationality"));
					columns.put("address", rs.getString("address_1"));
					columns.put("postcode", rs.getString("postcode"));
					table.add(columns);
				}

				return table;
			}

		});
	}

}
