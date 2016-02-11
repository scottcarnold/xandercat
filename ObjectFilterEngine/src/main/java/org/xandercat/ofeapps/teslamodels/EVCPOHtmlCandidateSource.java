package org.xandercat.ofeapps.teslamodels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xandercat.ofe.searchutility.candidate.JSoupCandidateSource;

/**
 * Abstract CandidateSource for obtaining Model S cars to search from a JSoup document
 * built from the content of the ev-cpo.com website.
 * 
 * This class handles parsing of the document while leaving retreiving the document up to the subclass.
 * 
 * Currently configured to parse only the United States listing. (to be expanded at a later date)
 * 
 * @author Scott Arnold
 */
public abstract class EVCPOHtmlCandidateSource extends JSoupCandidateSource<CPOModelS> {
	
	@Override
	public Collection<CPOModelS> getCandidates(Document doc) throws Exception {
		EVCPOColumnManager columnManager = new EVCPOColumnManager();
		List<CPOModelS> list = new ArrayList<CPOModelS>();
		Element table = doc.getElementById("cpo");
		Elements rows = table.getElementsByTag("tr");
		for (Iterator<Element> iter = rows.iterator(); iter.hasNext();) {
			Element row = iter.next();
			Elements columns = null;
			if (!columnManager.isColumnsAssigned()) {
				columns = row.getElementsByTag("th");
				if (columns.size() > 0) {
					for (int i=0; i<columns.size(); i++) {
						Element column = columns.get(i);
						columnManager.assignColumn(i, column.text());
					}					
				}
			}
			columns = row.getElementsByTag("td");
			if (columns.size() > 0) {
				if (columns.size() < columnManager.getColumnCount()) {
					throw new IOException("One or more rows does not have the expected number of columns (expected at least " + columnManager.getColumnCount() + ", found " + columns.size() + ").");
				}
				CPOModelS modelS = new CPOModelS();
				for (int i=0; i<columns.size(); i++) {
					Element column = columns.get(i);
					columnManager.setValue(modelS, i, column);
				}
				list.add(modelS);
			}
		}
		return list;
	}
}
