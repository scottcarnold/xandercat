package org.xandercat.ofe.searchutility.result;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.xandercat.ofe.Candidate;
import org.xandercat.ofe.CandidateChange;
import org.xandercat.ofe.FilterGroup;
import org.xandercat.ofe.ScoredCandidate;
import org.xandercat.ofe.filter.AttributeFilter;
import org.xandercat.ofe.searchutility.ResultDestination;

/**
 * Abstract ResultDestination that sends search results as a text message.
 * 
 * If issueMessageOnlyWhenResultsChanged is set to true, the text message will only be sent if
 * the search results have changed since the last search run.
 * 
 * @author Scott Arnold
 *
 * @param <T> candidate class
 */
public abstract class TextMessageResultDestination<T extends Candidate> implements ResultDestination<T> {

	public static final String DEFAULT_REPORT_TITLE = "Search Utility Results";
	
	private static final Logger LOGGER = Logger.getLogger(TextMessageResultDestination.class);
	private static final NumberFormat SCORE_FORMAT = NumberFormat.getPercentInstance();
	
	private String reportTitle = DEFAULT_REPORT_TITLE;
	private boolean issueMessageOnlyWhenResultsChanged = true;
	
	
	@Override
	public void initialize(Properties properties, Class<T> candidateClass) {
		this.reportTitle = properties.getProperty("report.title", DEFAULT_REPORT_TITLE);
		this.issueMessageOnlyWhenResultsChanged = Boolean.parseBoolean(
				properties.getProperty("report.only.when.changes.exist", "true"));
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public boolean isIssueMessageOnlyWhenResultsChanged() {
		return issueMessageOnlyWhenResultsChanged;
	}

	public void setIssueMessageOnlyWhenResultsChanged(boolean issueMessageOnlyWhenResultsChanged) {
		this.issueMessageOnlyWhenResultsChanged = issueMessageOnlyWhenResultsChanged;
	}

	@Override
	public void handleSearchResults(float threshold, Collection<FilterGroup<?>> filterGroups,
			List<CandidateChange<T>> changes, List<ScoredCandidate<T>> scoredCandidates) {
		if (!issueMessageOnlyWhenResultsChanged || changes.size() > 0) {
			StringBuilder sb = new StringBuilder();
			if (reportTitle != null) {
				sb.append(reportTitle).append("\n");
			}
			sb.append("Threshold for match is set at ");
			sb.append(NumberFormat.getPercentInstance().format(threshold)).append("\n\n");
			sb.append("Changes Since Last Report:\n\n");
			if (changes.size() == 0) {
				sb.append("There have been no changes since the last report.");
			}
			for (CandidateChange<T> change : changes) {
				sb.append(change.getChangeType().name()).append(" :\n");
				switch (change.getChangeType()) {
				case ADDED:
					sb.append(SCORE_FORMAT.format(change.getNewCandidate().getScore())).append(" ");
					sb.append(change.getNewCandidate().getCandidate().getFullDescription()).append("\n");
					break;
				case REMOVED:
					sb.append(SCORE_FORMAT.format(change.getOldCandidate().getScore())).append(" ");
					sb.append(change.getOldCandidate().getCandidate().getFullDescription()).append("\n");
					break;
				case CHANGED:
					sb.append("Old: ").append(SCORE_FORMAT.format(change.getOldCandidate().getScore())).append(" ");
					sb.append(change.getOldCandidate().getCandidate().getFullDescription()).append("\n");
					sb.append("New: ").append(SCORE_FORMAT.format(change.getNewCandidate().getScore())).append(" ");
					sb.append(change.getNewCandidate().getCandidate().getFullDescription()).append("\n");
					break;
				}
				sb.append("\n");
			}
			sb.append("\n\nCurrent Candidates:\n\n");
			if (scoredCandidates != null && scoredCandidates.size() > 0) {
				for (ScoredCandidate<T> candidate : scoredCandidates) {
					sb.append(SCORE_FORMAT.format(candidate.getScore()));
					sb.append(" ").append(candidate.getCandidate().getShortDescription()).append("\n");
				}
			} else {
				sb.append("There are currently no matching candidates.\n");
			}
			sb.append("\n\nCurrent Filters:\n\n");
			for (FilterGroup<?> filterGroup : filterGroups) {
				String fieldName = filterGroup.getFieldName();
				for (AttributeFilter<?> filter : filterGroup.getFilters()) {
					sb.append(fieldName).append(" ").append(filter.getDescription()).append("\n");
				}
			}
			LOGGER.info("Search results have changed; sending notification.");
			handleTextMessage(sb.toString());
		} else {
			LOGGER.info("Search results have not changed; no notification was sent.");
		}
	}

	@Override
	public void handleError(Exception e) {
		handleTextMessage("An error occurred while running search.\n\n" + e.getMessage());		
	}
	
	protected abstract void handleTextMessage(String textMessage); 

}
