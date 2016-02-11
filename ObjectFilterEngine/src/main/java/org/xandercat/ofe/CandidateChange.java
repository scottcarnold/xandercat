package org.xandercat.ofe;

/**
 * Stores information on the differences or changes between two scored candidates.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class CandidateChange<T extends Candidate> implements Comparable<CandidateChange<T>> {

	private ChangeType changeType;
	private ScoredCandidate<T> oldCandidate;
	private ScoredCandidate<T> newCandidate;
	
	public CandidateChange(ChangeType changeType, ScoredCandidate<T> oldCandidate, ScoredCandidate<T> newCandidate) {
		this.changeType = changeType;
		this.oldCandidate = oldCandidate;
		this.newCandidate = newCandidate;
	}
	public ChangeType getChangeType() {
		return changeType;
	}
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}
	public ScoredCandidate<T> getOldCandidate() {
		return oldCandidate;
	}
	public void setOldCandidate(ScoredCandidate<T> oldCandidate) {
		this.oldCandidate = oldCandidate;
	}
	public ScoredCandidate<T> getNewCandidate() {
		return newCandidate;
	}
	public void setNewCandidate(ScoredCandidate<T> newCandidate) {
		this.newCandidate = newCandidate;
	}
	@Override
	public int compareTo(CandidateChange<T> other) {
		int result = changeType.compareTo(other.changeType);
		if (result == 0 && oldCandidate != null && other.oldCandidate != null) {
			result = oldCandidate.compareTo(other.oldCandidate);
		}
		if (result == 0 && newCandidate != null && other.newCandidate != null) {
			result = newCandidate.compareTo(other.newCandidate);
		}
		return result;
	}
	@Override
	public String toString() {
		return "CandidateChange [changeType=" + changeType + ", oldCandidate="
				+ oldCandidate + ", newCandidate=" + newCandidate + "]";
	}
}
