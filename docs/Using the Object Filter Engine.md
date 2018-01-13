# Using the Object Filter Engine
The Object Filter Engine has the following methods of interest:
```java
public ObjectFilterEngine()
public ObjectFilterEngine(float scoreThreshold)
public ObjectFilterEngine(int maxResults)
public ObjectFilterEngine(Float scoreThreshold, Integer maxResults)

public <S> void addFilter(String field, AttributeFilter<S> attributeFilter)

public <S> void addStatCollector(String field, StatCollector<S, ?> statCollector)

public void addCandidate(T item)
public void addCandidates(T... items) 
public void addCandidates(Collection<T> items)

public void calculateStatistics()

public SortedSet<ScoredCandidate<T>> getScoredCandidates()

public List<CandidateChange<T>> getDifferences(SortedSet<ScoredCandidate<T>> previousScoredCandidates)

public Collection<StatCollectorGroup<?>> getStatCollectorGroups()
```
As a general order of operations,  you want to construct, add filters, add stat collectors (if any), add candidates, and finally get results.
# Construction
Construction is fairly simple. The only consideration is what you want the score threshold and/or max results to be.  The score threshold can range from 0 to 1.  It is the percent match threshold that a candidate must exceed to be included in the search results.  (e.g. if you set the score threshold to 0.6, then the results will only include matches that were considered to be at least a 60% match).  Max results is the maximum number of results you want from the search.  If this maximum is reached, the object filter engine will keep the highest percent matches.  If you provide neither, the object filter engine uses default values. 
# Adding Filters
Add one or more filters -- these represent your search criteria.  The field name should match the name of the field in the candidate object.  The filter type should match the type of the field in the candidate object.  If you want to use the same filter on multiple fields, just add it multiple times, once for each field.

As an example, if you have a field "description" in your candidate object that is defined as a String, it should have a matching "getDescription()" getter method that returns a String, and the filter should be for generic type String.
# Adding Stat Collectors
Add zero or more stat collectors.  These are optional, and allow you to run statistics against the values of the candidates.  For example, you can collect a count of how often various values are encountered, calculate average, minimum, and maximum values for number fields, etc. 

If using stat collectors, make sure to call calculateStatistics() on the Object Filter Engine after adding all of your candidates but before reading the statistics.
# Adding Candidates
Candidates are run against the filters and stat collectors at the time they are added to the object filter engine.  You should therefore make sure you have all filters and stat collectors added before adding the first candidate.
# Getting Results
getScoredCandidates() returns your basic search results in sorted order.  The are sorted by match percent first (descending), then by candidate.  If the Candidate class isn't comparable, it will compare by the value returned from the getUniqueId() method.

getDifferences(...) will return a list of differences between the scored candidates currently stored in the object filter engine, and some other list of scored candidates.  A typical use case for this is to store the list of scored candidates somewhere, then the next time the same search is run, you can retrieve the previous list of scored candidates, pass it to the getDifferences(...) method, and you will be returned a list of changes in the search results as compared to the previous run.  

getStatCollectorGroups() will return the statistics collectors (grouped by field name).  Access the stat collectors within each stat collector group, read out the collected statistics, and process them as needed.

