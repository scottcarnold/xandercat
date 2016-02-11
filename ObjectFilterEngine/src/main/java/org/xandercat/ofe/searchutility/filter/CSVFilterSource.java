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
	private Map<String, List<AttributeFilter<?>>> filters = new HashMap<String, List<AttributeFilter<?>>>();
	
	@Override
	public void initialize(Properties properties) throws Exception {
		this.file = new File(properties.getProperty("file", DEFAULT_FILE_NAME));
		CSVFormatter<Map<String, String>> csvFormatter = CSVFormatter.genericFormatter();
		csvFormatter.setFields(new String[] {"fieldName", "fieldType", "matchType", "matchValue", "attr1", "attr2"},
				new Class<?>[] {String.class, String.class, String.class, String.class, String.class, String.class});
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(this.file));
			String line = reader.readLine();
			while (line != null) {
				line = line.trim();
				if (line.length() > 0 && !line.startsWith("#")) { // ignore comments and blank lines; "#" is flag for comment
					Map<String, String> values = csvFormatter.parse(line.trim());
					if ("threshold".equalsIgnoreCase(values.get("fieldName"))) {
						this.matchThreshold = Float.valueOf(values.get("fieldType"));
					} else {
						String fieldName = values.get("fieldName");
						String fieldType = values.get("fieldType");
						String matchType = values.get("matchType");
						String matchValue = values.get("matchValue");
						String[] weights = null;
						if (values.containsKey("attr2")) {
							weights = new String[2];
							weights[1] = values.get("attr2");
						}
						if (values.containsKey("attr1")) {
							if (weights == null) {
								weights = new String[1];
							}
							weights[0] = values.get("attr1");
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
	public Set<String> getFilterFieldNames() {
		return filters.keySet();
	}

	@Override
	public List<AttributeFilter<?>> getFilters(String fieldName) {
		return filters.get(fieldName);
	}

}
