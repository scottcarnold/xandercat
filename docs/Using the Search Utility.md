# Search Utility

The Search Utility defines where input comes from and where output goes to through interfaces, which are currently configured through a Properties object.  The SearchUtility class can also optionally be used as the main executable for a search.

The interfaces used with Search Utility are for [Filter Sources](Filter-Sources), [Candidate Sources](Candidate-Sources), and [Result Destinations](Result-Destinations).

The Search Utility can be constructed by passing it a Properties object, and optionally, the candidate class.  If you don't explicitly pass it the candidate class, it will look for the candidate class name in the properties under the key "candidate.class".  If that fails, it will assume you want to use the generic MapCandidate class.

If used as an executable, Search Utility will try to load the properties using a default properties file name, but alternate properties file paths can be passed to the Search Utility as a program argument; this can be used as a way to run multiple different searches -- by passing in different properties files.

