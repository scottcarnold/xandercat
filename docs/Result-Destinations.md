# Result Destinations

A result destination defines where search results are sent.  It is defined by the ResultDestination interface as shown below (XanderCat OFE 1.2):

```java
/**
 * Interface used with the SearchUtility that defines what to do with the search results.
 * 
 * @author Scott Arnold
 *
 * @param <T>   type of object that was searched
 */
public interface ResultDestination<T extends Candidate> {
	
	/**
	 * Initialize the result destination from the given properties.  The properties will be a subset of
	 * the main SearchUtility properties whose keys start with the SearchUtility.RESULT_DESTINATION_PROPERTY_PREFIX;
	 * property keys used will have the prefix truncated.
	 * 
	 * @param properties     properties for the result destination
	 * @param candidateClass the candidate class
	 * 
	 * @throws Exception   if any errors occur
	 */	
	public void initialize(Properties properties, Class<T> candidateClass) throws Exception;
	
	/**
	 * Does something with the outcome of the search.
	 * 
	 * @param threshold           the threshold for matches (from 0 to 1; a percentage; null for no threshold)
	 * @param maxResults         the set maximum number of results (null for no maximum)
	 * @param filterGroups        the collection of filters for the various fields
	 * @param changes             the changes in the search results since the last run of the search
	 * @param scoredCandidates    the search matches and their match percentages
	 */
	public void handleSearchResults(Float threshold, Integer maxResults, 
			Collection<FilterGroup<?>> filterGroups, 
			List<CandidateChange<T>> changes, 
			SortedSet<ScoredCandidate<T>> scoredCandidates);
	
	/**
	 * Handles any exceptions that might have interrupted the search.  
	 * 
	 * @param e    the exception that occurred during the search
	 */
	public void handleError(Exception e);
}
```

# TextMessageResultDestination

The TextMessageResultDestination is abstract.  It takes care of formatting the search results as a text message, but it does not define what to do with the text message once it's built; this must be handled by a subclass.

# ConsoleResultDestination

The ConsoleResultDestination extends the TextMessageResultDestination, and simply spits the text message to the screen.  

Note that you can generally get the output routed to a file using this as well.  For example, in the batch script for the Tesla Model S CPO search example application, it by default routes the output to a text file.  You can see this if you use an editor to look at the batch file.  It will look something like follows:

`java -jar XanderCatOFE-1.1.jar cposearch.properties > copsearch-results.txt`

# MailResultDestination

The MailResultDestination extends the TextMessageResultDestination, and sends the text message as an email to a list of email addresses.

The MailResultDestination accepts the following properties:

| Property | Disposition | Value |
| --- | --- | --- |
| mail.username | Required | username for the from mail account |
| mail.password | Required | password for the from mail account |
| mail.from | Required | email address for the from mail account |
| mail.to | Required | CSV list of email addresses to send mail to |
| mail.subject | Optional | subject line for email; default uses report title |
| any properties listed in Appendix A of the JavaMail spec | | See Appendix A of the JavaMail spec |

MailResultDestination expects to use password authentication, which is why the username and password are required.

As an example of additional JavaMail spec properties you may need -- for Gmail, in addition to the required properties shown above, you need to specify the following properties from the JavaMail spec:

| Property | Value |
| --- | --- |
| mail.smtp.auth | true |
| mail.smtp.starttls.enable | true |
| mail.smtp.host | smtp.gmail.com |
| mail.smtp.port | 587 |

:_Note:  For Gmail, you can send a mail from an email account and to that same email account without any trouble._


