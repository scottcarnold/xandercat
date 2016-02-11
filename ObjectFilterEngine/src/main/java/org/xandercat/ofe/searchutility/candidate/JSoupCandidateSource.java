package org.xandercat.ofe.searchutility.candidate;

import java.io.IOException;
import java.util.Collection;

import org.jsoup.nodes.Document;
import org.xandercat.ofe.Candidate;
import org.xandercat.ofe.searchutility.CandidateSource;

/**
 * CandidateSource that reads objects from an HTML document using the JSoup API.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public abstract class JSoupCandidateSource<T extends Candidate> implements CandidateSource<T> {

	@Override
	public Collection<T> getCandidates() throws Exception {
		return getCandidates(createDocument());
	}

	protected abstract Document createDocument() throws IOException;
	
	protected abstract Collection<T> getCandidates(Document doc) throws Exception;
}
