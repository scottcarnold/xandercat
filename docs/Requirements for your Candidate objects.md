Objects that will be searched against are referred to as "candidates".  You have the option of building a class to represent the objects or not.  If you do not use a custom class to represent your objects, a generic Map-based class is provided that will be used instead.

# Writing Your Own Custom Class

When writing your own custom candidate class, it must conform to the following rules:
1. It must implement the Candidate interface.
2. It must have a default no-argument constructor.
3. It must have standard getter and setter names for any fields that will be filtered on.
4. If you want to use it with the Search Utility, it must implement Serializable, as the search utility currently stores results of previous searches by writing them to disk.
5. Your class should implement appropriate equals and hashCode methods.
6. It is recommended that your class implement Comparable so that search results with identical match scores will be returned in a logical sorted order.

# Using Generic Map-based Class

If you do not specify a custom class, your objects will be stored in the MapCandidate class which stores all fields and their values in a LinkedHashMap.  The MapCandidate class provides a generic implementation for the Candidate interface.  You should be aware of the following when using this approach:
1. For XanderCat OFE version 1.1, all keys and values within the Map are expected to be Strings when using the CSVCandidateSource.  However, as of version 1.2, you can specify data types for each field when using the CSVCandidateSource by providing a comma separated list of class names through the property with key {{candidate.source.field.types}}; class names for this property must be fully qualified unless from the {{java.lang}} package. 
2. The Candidate interface requires a getUniqueId() method that returns a String value.  The MapCandidate class looks for an item in your map with the key of "uniqueId", or "id"; if it does not find one, it uses the first element that was stored in the map.
3. The Candidate interface requires a getShortDescription() method.  The MapCandidate class looks for an item in your map with key of "shortDescription"; if it does not find one, it will use the generic map toString() method.
4. The Candidate interface requires a getFullDescription() method.  The MapCandidate class looks for an item in your map with key of "fullDescription"; if it does not find one, it will use the generic map toString() method.

