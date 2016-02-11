package org.xandercat.ofe;

/**
 * Interface to implement for an object to be used in the ObjectFilterEngine.  
 * 
 * @author Scott Arnold
 */
public interface Candidate {

	public String getUniqueId();
	
	public String getShortDescription();
	
	public String getFullDescription();
}
