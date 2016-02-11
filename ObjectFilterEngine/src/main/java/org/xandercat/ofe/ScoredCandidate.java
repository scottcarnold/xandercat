package org.xandercat.ofe;

import java.io.Serializable;

/**
 * Stores a candidate and the match percentage for that candidate.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class ScoredCandidate<T extends Candidate> implements Comparable<ScoredCandidate<T>>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private float score;
	private T candidate;
	
	public ScoredCandidate(float score, T candidate) {
		this.score = score;
		this.candidate = candidate;
	}
	public float getScore() {
		return score;
	}
	public T getCandidate() {
		return candidate;
	}
	@Override
	public int compareTo(ScoredCandidate<T> sc) {
		if (score > sc.score) {
			return -1;
		} else if (score < sc.score) {
			return 1;
		} else {
			return 0;
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((candidate == null) ? 0 : candidate.hashCode());
		result = prime * result + Float.floatToIntBits(score);
		return result;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScoredCandidate other = (ScoredCandidate) obj;
		if (candidate == null) {
			if (other.candidate != null)
				return false;
		} else if (!candidate.equals(other.candidate))
			return false;
		if (Float.floatToIntBits(score) != Float.floatToIntBits(other.score))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ScoredCandidate [score=" + score + ", candidate=" + candidate + "]";
	}
}
