package org.xandercat.ofe.searchutility.candidate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.xandercat.ofe.Candidate;
import org.xandercat.ofe.searchutility.CSVFormatter;
import org.xandercat.ofe.searchutility.CSVFormatter.ValueFormatter;
import org.xandercat.ofe.searchutility.CandidateSource;

/**
 * CandidateSource that reads candidates from a CSV text file.
 * 
 * Field types allowed include the default field types supported by CSVFormatter
 * and also java.util.Date if the properties contains a date format under
 * the "date.format" key.
 * 
 * The candidate class type is expected to be under the properties key "candidate.class";
 * otherwise it will assume you want the generic MapCandidate class.  When using the
 * MapCandidate class, the field types can be specified through a CSV list of class
 * names under property key "field.types"; if the class name is not fully qualified,
 * it will be assumed from package java.lang.
 * 
 * @author Scott Arnold
 *
 * @param <T> candidate class
 */
public class CSVCandidateSource<T extends Candidate> implements CandidateSource<T> {

	public static final String DEFAULT_FILE_NAME = "candidates.txt";
	
	private File file;
	private CSVFormatter<T> formatter; 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(Properties properties, Class<T> candidateClass) throws Exception {
		this.file = new File(properties.getProperty("file", DEFAULT_FILE_NAME));
		formatter = new CSVFormatter(candidateClass);
		if (properties.containsKey("date.format")) {
			final String dateFormat = properties.getProperty("date.format");
			formatter.addValueFormatter(Date.class, new ValueFormatter<Date>() {
				private DateFormat format = new SimpleDateFormat(dateFormat);
				@Override
				public Date parse(String value) throws Exception {
					return format.parse(value);
				}
				@Override
				public String format(Date value) {
					return format.format(value);
				}
			});
		}
		String[] fields = properties.getProperty("fields").split(",");
		for (int i=0; i<fields.length; i++) {
			fields[i] = fields[i].trim();
		}
		Class[] fieldTypes = null;
		String fieldTypesCSVString = properties.getProperty("field.types");
		
		if (fieldTypesCSVString != null) {
			String[] fieldTypeStrings = fieldTypesCSVString.split(",");
			fieldTypes = new Class[fieldTypeStrings.length];
			for (int i=0 ;i<fieldTypeStrings.length; i++) {
				String className = fieldTypeStrings[i].contains(".")? fieldTypeStrings[i].trim() : "java.lang." + fieldTypeStrings[i].trim();
				fieldTypes[i] = Class.forName(className);
			}
		}
		if (fieldTypes == null) {
			formatter.setFields(fields);
		} else {
			formatter.setFields(fields, fieldTypes);
		}
	}

	@Override
	public Collection<T> getCandidates() throws Exception {
		Collection<T> candidates = new ArrayList<T>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				candidates.add(formatter.parse(line));
				line = reader.readLine();
			}
		} finally {
			try {
				reader.close();
			} catch (Exception e) { }
		}
		return candidates;
	}
}
