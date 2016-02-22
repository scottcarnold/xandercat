package org.xandercat.ofe.searchutility.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.xandercat.ofe.filter.AttributeFilter;
import org.xandercat.ofe.filter.FilterFactory;
import org.xandercat.ofe.searchutility.CSVFormatter;
import org.xandercat.ofe.searchutility.FilterSource;

/**
 * FilterSource that parses filters from a CSV text file.
 * 
 * @author Scott Arnold
 */
public class CSVFilterSource implements FilterSource {

	public static final String DEFAULT_FILE_NAME = "searchfilters.txt";
	
	private File file;
	private Float matchThreshold;
	private Integer maxResults;
	private Map<String, List<AttributeFilter<?>>> filters = new HashMap<String, List<AttributeFilter<?>>>();
	
	@Override
	public void initialize(Properties properties) throws Exception {
		this.file = new File(properties.getProperty("file", DEFAULT_FILE_NAME));
		CSVFormatter<Map<String, Object>> csvFormatter = CSVFormatter.genericFormatter();
		csvFormatter.setFields(new String[] {"fieldName", "fieldType", "matchType", "matchValue", "attr1", "attr2"});
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(this.file));
			String line = reader.readLine();
			while (line != null) {
				line = line.trim();
				if (line.length() > 0 && !line.startsWith("#")) { // ignore comments and blank lines; "#" is flag for comment
					Map<String, Object> values = csvFormatter.parse(line.trim());
					if ("threshold".equalsIgnoreCase((String) values.get("fieldName"))) {
						this.matchThreshold = Float.valueOf((String) values.get("fieldType"));
					} else if ("maxResults".equalsIgnoreCase((String) values.get("fieldName"))) {
						this.maxResults = Integer.valueOf((String) values.get("fieldType"));
					} else {
						String fieldName = (String) values.get("fieldName");
						String fieldType = (String) values.get("fieldType");
						String matchType = (String) values.get("matchType");
						String matchValue = (String) values.get("matchValue");
						String[] weights = null;
						if (values.containsKey("attr2")) {
							weights = new String[2];
							weights[1] = (String) values.get("attr2");
						}
						if (values.containsKey("attr1")) {
							if (weights == null) {
								weights = new String[1];
							}
							weights[0] = (String) values.get("attr1");
						}
						AttributeFilter<?> filter = FilterFactory.getFilter(fieldType, matchType, matchValue, weights);
						List<AttributeFilter<?>> filterList = filters.get(fieldName);
						if (filterList == null) {
							filterList = new ArrayList<AttributeFilter<?>>();
							filters.put(fieldName, filterList);
						}
						filterList.add(filter);
					}
				}
				line = reader.readLine();
			}
		} finally {
			try {
				reader.close();
			} catch (Exception e) { }
		}
	}

	@Override
	public Float getMatchThreshold() {
		return matchThreshold;
	}

	@Override
	public Integer getMaxResults() {
		return maxResults;
	}
	
	@Override
	public Set<String> getFilterFieldNames() {
		return filters.keySet();
	}

	@Override
	public List<AttributeFilter<?>> getFilters(String fieldName) {
		return filters.get(fieldName);
	}

}
