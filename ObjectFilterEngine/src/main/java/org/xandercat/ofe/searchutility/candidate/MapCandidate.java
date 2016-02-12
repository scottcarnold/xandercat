package org.xandercat.ofe.searchutility.candidate;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.xandercat.ofe.Candidate;

/**
 * Generic Candidate for the Object Filter Engine that just stores all values in a Map.
 * 
 * getUniqueId() method will look for a Map entry with key "uniqueId" or "id"; if it finds
 * neither, it will use the first element added to the Map.
 * 
 * @author Scott Arnold
 */
public class MapCandidate extends LinkedHashMap<String, String> implements Candidate, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String getUniqueId() {
		if (containsKey("uniqueId")) {
			return get("uniqueId");
		} else if (containsKey("id")) {
			return get("id");
		} else {
			return entrySet().iterator().next().getValue(); 
		}
	}

	@Override
	public String getShortDescription() {
		return toString();
	}

	@Override
	public String getFullDescription() {
		return toString();
	}
}
