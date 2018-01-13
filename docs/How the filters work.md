# Filters

Filters are defined by implementing the AttributeFilter<T> interface.  The <T> in this case is _not_ the candidate class, but the class type for the field within the candidate that will be filtered against.

It is acceptable and suggested that you use wrapper class filters for primitives (e.g. if your candidate has an {{int}}, just use the {{IntegerFilter}}).

# Filter Rules

All filters have a "weight" that can range from 0 to 1, and each filter can also be set as "required" or "excluded".  Weight has no effect on filters that are marked excluded.

Each field in the candidate can be set up to run against zero or more filters.

If a filter is marked as excluded, it means that if that filter matches a candidate, the candidate is immediately thrown out.

If a filter is marked as required, it means that the candidate is thrown out if the filter does _not_ match.  One exception to this rule is if you define multiple required filters on the same field; in this case, only one of the required filters needs to match to prevent the candidate from being thrown out.

Assuming a candidate wasn't thrown out by the excluded and required filters, the last remaining check is to see if the weights of all matched filters exceeds the match threshold set in the object filter engine.  If it does, the candidate will be added to the list of matches.  If the number of matches is pushed over a set maximum number of matches (a set maximum is optional), the lowest match is dropped from the list.

When a field has multiple filters, the maximum possible weight for that one field will be set to whatever the maximum weight is among all the filters on that field.  The actual weight used for any candidate on that field will be set to whatever the maximum weight is for all the filters that _matched_ on that field.

# Aggregate Filters

Starting with XanderCat OFE 1.2, you can also create aggregate filters through the AggregateFilter class.  An aggregate filter combines multiple filters into a single filter.  Required, excluded, and weight attributes should be set on the aggregate filter itself; required, excluded, and weight attributes on the individual filters within the aggregate filter will be ignored.

A good example use for this is to create a range filter, where a match occurs when a number falls between two other numbers.  To do this with a single filter, create an aggregate filter, then add to the aggregate filter a numeric filter with style {{LESS_THAN}} or {{LESS_THAN_OR_EQUALS}} and a numeric filter with style {{GREATER_THAN}} or {{GREATER_THAN_OR_EQUALS}}.

# Calculating Match Percent

Fields without any filters are ignored.  Match percent is determined by summing the value of all actual weights for matched fields and dividing that by the sum of the maximum possible weights for all fields.

Example:

Assume we have the following filters:

| filter | field name | filter weight | matched candidate? |
| --- | --- | --- | --- |
| 1 | color | 0.5 | yes |
| 2 | flavor | 0.25 | yes |
| 3 | flavor | 0.5 | no |
| 4 | flavor | 0.75 | no |
| 5 | price | 0.75 | yes |
| 6 | size | 0.5 | no |

Now assess those filters by field:

| field name | max matched weight | max possible weight |
| --- | --- | --- |
| color | 0.5 | 0.5 | 
| flavor | 0.25 | 0.75 | 
| price | 0.75 | 0.75 |
| size | 0.0 | 0.5 |
| **sum** | **1.5** | **2.5** |

Divide the sum of all max matched weight by the sum of all max possible weight:

Match percent:  1.5 / 2.5 = 0.6 

Your final match percent is **60%**

