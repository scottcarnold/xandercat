package org.xandercat.ofeapps.teslamodels;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xandercat.ofe.searchutility.SearchUtility;

/**
 * Executable class for running a search against the Tesla Model S CPO inventory.
 * 
 * @author Scott Arnold
 */
public class CPOSearch {

	private static final Logger LOGGER = Logger.getLogger(CPOSearch.class);
	private static final File DEFAULT_PROPERTIES_FILE = new File("cposearch.properties");
	
	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			File propsFile = (args.length > 0)? new File(args[0]) : DEFAULT_PROPERTIES_FILE;
			properties.load(new FileReader(propsFile));
			PropertyConfigurator.configure(properties.getProperty("logging.properties.file"));
		} catch (Exception e) {
			BasicConfigurator.configure();
			LOGGER.error("Unable to load program proerties (properties file location should be " + DEFAULT_PROPERTIES_FILE.getAbsolutePath() + " or provided as first program argument).", e);
		}
		
		LOGGER.info("CPOSearch started...");
		CPOSearch cpoSearch = new CPOSearch();
		try {
			cpoSearch.executeSearch(properties);
		} catch (Exception e) {
			LOGGER.error("Unable to complete CPO Search.", e);
		}
		LOGGER.info("CPOSearch complete.");
	}
	
	public void executeSearch(Properties properties) throws Exception {
		SearchUtility<CPOModelS> searchUtility = new SearchUtility<CPOModelS>(properties);
		searchUtility.searchAndNotify();
	}
}
