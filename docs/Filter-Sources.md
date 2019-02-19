# Filter Sources

A filter source defines where the search filters come from and what the overall match percent threshold should be for searches.  It is defined by the FilterSource interface as shown below (XanderCat OFE 1.2):

```java
/**
 * Interface used with SearchUtility to define the filters that will be used in the object search.
 * 
 * @author Scott Arnold
 */
public interface FilterSource {

	/**
	 * Initialize the filter source from the given properties.  The properties will be a subset of
	 * the main SearchUtility properties whose keys start with the SearchUtility.FILTER_SOURCE_PROPERTY_PREFIX;
	 * property keys used will have the prefix truncated.
	 * 
	 * @param properties    properties for the filter source
	 * 
	 * @throws Exception   if any errors occur
	 */
	public void initialize(Properties properties) throws Exception;
	
	/**
	 * Returns the match threshold to use for the object filtering engine (in range 0 to 1). 
	 * 
	 * @return    match threshold to use for the object filtering engine
	 */
	public Float getMatchThreshold();
	
	/**
	 * Returns the maximum results to use for the object filtering engine. 
	 * 
	 * @return    maximum results to use for the object filtering engine
	 */
	public Integer getMaxResults();

	/**
	 * Returns the set of field names that filters will be run against in the search.
	 * 
	 * @return    set of field names that filters will be run against
	 */
	public Set<String> getFilterFieldNames();
	
	/**
	 * Returns a list of filters for the given field name.
	 * 
	 * @param fieldName    name of field to be filtered
	 * 
	 * @return    list of filters for the given field
	 */
	public List<AttributeFilter<?>> getFilters(String fieldName);
}
```

# CSVFilterSource

As an alternative to writing your own FilterSource, the Search Utility comes with a CSVFilterSource for pulling filter information from a Comma Separated Value (CSV) file.  The Tesla Model S CPO Search example application gives an example of this.  An example of the filters from the CSV file is shown below:

```
threshold,       0.75
maxResults,      4
trim,            String,  STARTS_WITH, S85,      required
location,        String,  EQUALS,      San Francisco
location,        String,  STARTS_WITH, San Diego
interior,        String,  CONTAINS,    Nappa,    HIGH
interior,        String,  CONTAINS,    Black,    excluded
roof,            String,  EQUALS,      Pano,     required
color,           String,  CONTAINS,    Red,      excluded
color,           String,  CONTAINS,    White
color,           String,  CONTAINS,    Brown,    LOW
wheels,          String,  STARTS_WITH, 21
price,           Integer, LESS_THAN,   80000,    required
price,           Integer, LESS_THAN,   70000
miles,           Integer, LESS_THAN,   40000,    required
dateAdded,       Date,    AFTER,       1/1/2016, required
```

Lines in the filter file can be any of the following:
* Blank lines
* Comments, which must start with the # sign
* Match threshold, which should occur only once and consist of 2 comma separated values
* Maximum results, which should occur only once and consist of 2 comma separated values
* Filters, which can contain anywhere from 4 to 6 comma separated values.

For the filters, the comma separated values, in order, are:
1. field name
2. field type
3. match type
4. match value
5. weight/required/excluded attribute
6. weight/required/excluded attribute

## Field Names

Field names should match the field names of your candidate class if using a custom candidate class, or they should match the field names provided in the properties if using the CSVCandidateSource without a candidate class.

## Field Types

Field type should match the type of the variable in your candidate class for the field.  Built-in supported types include String, Date, Boolean, Long, Integer, Short, Byte, Double, and Float.  

CSVFilterSource does not currently support aggregate filters.

## Match Types

Match types include the following:
| Field Type | Valid Values |
| --- | --- |
| String | `EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH` |
| Date | `EQUALS, BEFORE, AFTER` |
| All number types | `EQUALS, LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUALS, GREATER_THAN_OR_EQUALS` |
| Boolean | `EQUALS` |

## Match Values

Match values should be whatever you want to compare against.  Values can optionally be wrapped in double quotes.  You must wrap the value in double quotes if you are trying to match a value that contains a comma in it.  String match values by default are case insensitive.

## Weight/Required/Excluded Attributes

You can have from 0 to 2 additional attributes for your filters.  The values for these are case insensitive.  Valid values are as follows:

| Value | Meaning |
| --- | --- |
| required | filter must match the candidate, or the candidate gets thrown out |
| excluded | filter must **not** match the candidate, or the candidate gets thrown out |
| maximum | filter should have maximum possible weight (1.0) |
| high | filter should have high weight (0.75) |
| medium | filter should have medium weight (0.5) |
| low | filter should have low weight (0.25) |
| none | filter should have no weight (0.0); useful for required filters if you don't want them affecting match percentage. |
| <decimal value> | custom set weight, can range from 0.0 to 1.0 |

If you don't specify any additional attributes, the filter will be neither required nor excluded, and the weight will default to medium (0.5).  Weight has no meaning for excluded filters.  Setting a filter both required and excluded is nonsensical and should be avoided, but is not explicitly forbidden.

