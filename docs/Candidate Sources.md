# Candidate Sources

A candidate source defines where the objects you want to search come from.  It is defined by the CandidateSource interface as shown below:

{code:java}
/**
 * Interface used with SearchUtility that defines how to obtain the collection of candidates to be searched.
 * 
 * @author Scott Arnold
 *
 * @param <T>   candidate class type
 */
public interface CandidateSource<T> {

	/**
	 * Initialize the candidate source from the given properties.  The properties will be a subset of
	 * the main SearchUtility properties whose keys start with the SearchUtility.CANDIDATE_SOURCE_PROPERTY_PREFIX;
	 * property keys used will have the prefix truncated.
	 * 
	 * @param properties     properties for the candidate source
	 * @param candidateClass the candidate class
	 * 
	 * @throws Exception   if any errors occur
	 */
	public void initialize(Properties properties, Class<T> candidateClass) throws Exception;
	
	/**
	 * Return the collection of candidates to search.
	 * 
	 * @return    collection of candidates to search
	 * 
	 * @throws Exception   if any errors occur
	 */
	public Collection<T> getCandidates() throws Exception;
}
{code:java}

# CSVCandidateSource

CSVCandidateSource provides a way to load objects to search from a Comma Separated Value (CSV) file.  

CSVCandidateSource has the following properties associated with it:
|| Property || Disposition || Value ||
| file | Required | The path and file name for the CSV file |
| fields | Required | An ordered list of the field names, comma separated; the order should match what order used in the CSV input file. |
| field.types | Optional | Added in XanderCat OFE 1.2.  An ordered list of field data types, comma separated; each data type should be a fully qualified class name or the name of a class from the java.lang package.  |
| date.format | Optional | A date format in Java style; this tells it how to parse date values.  For information on how the formats work, see [http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html](http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html) |

# JSoupCandidateSource

JSoup is a popular HTML parser.  The JSoup candidate source is very simple, and it's only real purpose is to structurally separate creation of the JSoup document from the parsing of the JSoup document.

