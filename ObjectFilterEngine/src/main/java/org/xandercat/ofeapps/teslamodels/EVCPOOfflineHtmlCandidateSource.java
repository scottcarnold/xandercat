package org.xandercat.ofeapps.teslamodels;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * CandidateSource for obtaining Model S cars to search from a copy of the ev-cpo.com
 * page source saved as a file.
 * 
 * @author Scott Arnold
 */
public class EVCPOOfflineHtmlCandidateSource extends EVCPOHtmlCandidateSource {

	private File htmlFile;
	
	@Override
	public void initialize(Properties properties) throws Exception {
		this.htmlFile = new File(properties.getProperty("file"));
	}

	@Override
	protected Document createDocument() throws IOException {
		return Jsoup.parse(htmlFile, "UTF-8");
	}
}
