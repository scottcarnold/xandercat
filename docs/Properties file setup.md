# Properties File Setup

The main application properties are as follows:

|| property key || property value description ||
| logging.properties.file | file name for the properties file used for configuring application logging. |
| previous.results.file | file name for the file Search Utility uses to save search results. |
| candidate.class | fully qualified class name for the class to be used as the candidate; if not specified, class MapCandidate will be used |
| filter.source.class | fully qualified class name for the filter source class |
| candidate.source.class | fully qualified class name for the candidate source class |
| result.destination.class | fully qualified class name for the result destination class |

# Properties for Sources and Destination

In addition to the main application properties, individual properties for the filter source, candidate source, and result destination can be included in the main properties file.  However, there is a naming convention that must be used for the property keys.  Property keys must have a prefix appended to them in order to get passed along; however, the passed along properties will have the prefix removed before they are passed.  

Example:

Assume you write a result destination class and you need a property from the properties file.  You would like to give that property a key value of "my.property".  In order for that property to actually get passed to your result destination, it must have the result destination prefix appended to it.  This means the main properties file needs to have the property named as "result.destination.my.property".  However, the prefix is stripped off before being passed to your result destination, so your result destination will still just see it as "my.property".

|| interface || property prefix ||
| FilterSource | filter.source. |
| CandidateSource | candidate.source. |
| ResultDestination | result.destination. |

