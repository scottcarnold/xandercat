A stat collector source defines where the statistics collectors come from for searches. It is defined by the StatCollectorSource interface as shown below (XanderCat OFE 1.2):

```java
/**
 * Interface used with SearchUtility to define the stat collectors that will be used in the object search.
 * 
 * @author Scott Arnold
 */
public interface StatCollectorSource {

	/**
	 * Initialize the stat collector source from the given properties.  The properties will be a subset of
	 * the main SearchUtility properties whose keys start with the SearchUtility.STAT_COLLECTOR_SOURCE_PROPERTY_PREFIX;
	 * property keys used will have the prefix truncated.
	 * 
	 * @param properties    properties for the filter source
	 * 
	 * @throws Exception   if any errors occur
	 */
	public void initialize(Properties properties) throws Exception;
	
	/**
	 * Returns the set of field names that stat collectors will be run against in the search.
	 * 
	 * @return    set of field names that stat collectors will be run against
	 */
	public Set<String> getStatCollectorFieldNames();
	
	/**
	 * Returns a list of stat collectors for the given field name.
	 * 
	 * @param fieldName    name of field to collect statistics on
	 * 
	 * @return    list of stat collectors for the given field
	 */
	public List<StatCollector<?, ?>> getStatCollectors(String fieldName);
}
```

At this time, there is no CSVStatCollectorSource available for use.  However, writing your won stat collector source is pretty straight forward if you have any experience with Java programming.  Take a look at the stat collector source for the CPO Model S search application to see an example of what it can look like:

```java
public class CPOStatCollectorSource extends AbstractStatCollectorSource {

	@Override
	public void initialize(Properties properties) throws Exception {
		addStatCollector("price", new AverageStatCollector<Integer>(Integer.class, NumberFormat.getCurrencyInstance()));
		addStatCollector("price", new MinMaxStatCollector<Integer>(Integer.class, NumberFormat.getCurrencyInstance()));
		addStatCollector("price", new MatchCountStatCollector<Integer>(Integer.class, 
				new IntegerFilter(NumericMatchStyle.LESS_THAN, 60000)));
		addStatCollector("price", new MatchCountStatCollector<Integer>(Integer.class, new AggregateFilter<Integer>(
				new IntegerFilter(NumericMatchStyle.GREATER_THAN_OR_EQUALS, 60000),
				new IntegerFilter(NumericMatchStyle.LESS_THAN, 80000))));
		addStatCollector("price", new MatchCountStatCollector<Integer>(Integer.class, 
				new IntegerFilter(NumericMatchStyle.GREATER_THAN_OR_EQUALS, 80000)));
		addStatCollector("cpoInv", new CountDistinctStatCollector<String>(String.class));
		addStatCollector("year", new CountDistinctStatCollector<Integer>(Integer.class));
	}
}
```

