package org.xandercat.ofeapps.teslamodels;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class that attempts to map column types to column indicies for the CPO Model S table
 * being parsed with JSoup.
 * 
 * @author Scott Arnold
 */
public class EVCPOColumnManager {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Map<Integer, ValueSetter>  indexMappings = new HashMap<Integer, ValueSetter>();
	private Map<String, ValueSetter> mappings = new HashMap<String, ValueSetter>();
	private boolean columnsVerified = false;
	
	private interface ValueSetter {
		public void setValue(CPOModelS cpoModelS, Element element);
	}
	
	public EVCPOColumnManager() {
		mappings.put("VIN", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setVin(element.text());
				Elements link = element.getElementsByTag("a");
				if (link.size() > 0) {
					cpoModelS.setUrl(parseUrl(link.first().attr("href")));
				}				
			}
		});
		mappings.put("CPO/Inv", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setCpoInv(element.text());	
			}
		});
		mappings.put("Location", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setLocation(element.text());	
			}
		});
		mappings.put("Trim", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setTrim(element.text());
			}
		});
		mappings.put("AP/", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setAptp(element.text());
			}
		});
		mappings.put("DUAL", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setDualMotor(parseBoolean(element.text()));
			}
		});
		mappings.put("Rear", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setRearFacingSeats(parseBoolean(element.text()));
			}
		});
		mappings.put("Cold", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setColdWeatherPackage(parseBoolean(element.text()));
			}
		});
		mappings.put("Audio", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setSoundStudio(parseBoolean(element.text()));
			}
		});
		mappings.put("SC", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setSuperchargerEnabled(parseBoolean(element.text()));
			}
		});
		mappings.put("SAS", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setSmartAirSuspension(parseBoolean(element.text()));
			}
		});
		mappings.put("2CH", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setDualChargers(parseBoolean(element.text()));
			}
		});
		mappings.put("Color", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setColor(element.text());
			}
		});
		mappings.put("Roof", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setRoof(element.text());
			}
		});
		mappings.put("Wheels", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setWheels(element.text());
			}
		});
		mappings.put("Interior", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setInterior(element.text());
			}
		});
		mappings.put("Year", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setYear(parseInteger(element.text()));
			}
		});
		mappings.put("Miles", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setMiles(parseInteger(element.text()));
			}
		});
		mappings.put("Price", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setPrice(parseInteger(element.text()));
			}
		});
		mappings.put("Date", new ValueSetter() {
			@Override
			public void setValue(CPOModelS cpoModelS, Element element) {
				cpoModelS.setDateAdded(parseDate(element.text()));
			}
		});
	}
	
	private String parseUrl(String source) {
		int i = source.indexOf("http");
		if (i > 0 && source.charAt(i-1) == '\'') {
			int j = source.substring(i).indexOf('\'');
			if (j > 0) {
				return source.substring(i, i+j);
			}
		} 
		return source;
	}
	
	private Integer parseInteger(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim().replaceAll(",", "").replaceAll("$",  "");  // remove separators and dollar signs
		if (s.length() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int i=0;
		char c=s.charAt(0);
		while (i<s.length() && c>='0' && c<='9') {  // grab first sequence of numbers and discard anything after
			sb.append(c);
			i++;
			if (i<s.length()) {
				c = s.charAt(i);
			}
		}
		return Integer.valueOf(sb.toString());
	}
	
	private Date parseDate(String s) {
		try {
			return DATE_FORMAT.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Boolean parseBoolean(String s) {
		if ("Y".equals(s)) {
			return Boolean.TRUE;
		} else if ("N".equals(s)) {
			return Boolean.FALSE;
		} else {
			return null;
		}
	}
	
	public int getColumnCount() {
		return mappings.size();
	}
	
	public boolean isColumnsAssigned() {
		return indexMappings.size() == mappings.size();
	}
	
	public void assignColumn(int columnIndex, String label) {
		if (label != null) {
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<label.length(); i++) {  // strip all random junk
				char c = label.charAt(i);
				if ((c >= 'a' && c <= 'z')
						|| (c >='A' && c <= 'Z')
						|| (c >='0' && c <= '9')
						|| (c == '/')) {
					sb.append(c);
				}
			}
			String labelText = sb.toString();
			for (String labelMatchString : mappings.keySet()) {
				if (labelText.toLowerCase().startsWith(labelMatchString.toLowerCase())) {
					indexMappings.put(Integer.valueOf(columnIndex), mappings.get(labelMatchString));
				}
			}
		}
	}
	
	public void setValue(CPOModelS cpoModelS, int columnIndex, Element element) throws IOException {
		if (!columnsVerified) {
			if (indexMappings.size() != mappings.size()) {
				throw new IOException("Not all columns were found in the parsed HTML table.");
			}
			columnsVerified = true;
		}
		ValueSetter valueSetter = indexMappings.get(Integer.valueOf(columnIndex));
		if (valueSetter != null) {
			valueSetter.setValue(cpoModelS, element);
		}
	}
}
