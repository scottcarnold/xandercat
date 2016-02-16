package org.xandercat.ofe;

/**
 * Interface to implement for an object to be used in the ObjectFilterEngine.  
 * 
 * @author Scott Arnold
 */
public interface Candidate {

	/**
	 * Return a String value that uniquely identifies the candidate.  This is primarily needed
	 * for building the list of changes between runs.
	 * 
	 * @return    String that uniquely identifies the candidate.
	 */
	public String getUniqueId();
	
	/**
	 * Return a short description for the candidate.  This may be used for formatting search results.
	 * 
	 * @return    Short description for the candidate
	 */
	public String getShortDescription();
	
	/**
	 * Return a full description for the candidate.  This may be used for formatting search results.
	 * 
	 * @return    Full description for the candidate.
	 */
	public String getFullDescription();
}
