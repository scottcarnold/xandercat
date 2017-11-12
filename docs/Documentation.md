# XanderCat OFE

XanderCat OFE is an object filtering engine with an executable search utility written in Java.  It can be used as a component of a larger Java application, or as a standalone console application that can be run on demand or by a job scheduler.  When used as a standalone application, you can attach your own interface classes for handling input and output, or use it completely as-is by utilizing the comma separated value (CSV) support for the filters and input.

The search uses filters that filter on the fields or attributes of your objects.  You can have zero or more filters for each field of the object, and each filter can be assigned a weight in order to produce a list of matches with percent match values.  In addition, each filter can optionally exclude a candidate from the search results if matched, or be required whereas a candidate will be excluded if _not_ matched.  

Once filters have been added and a search performed, you can retrieve the search results, and if needed, compare that list of search results to another (usually older) list of search results.

Some example use cases:
* You have an application where you need to provide a search over a list of objects.  In this case you could utilize just the Object Filtering Engine to perform the search and retrieve the matches.
* You want to monitor a list of items on a website looking for ones that match a set of criteria.  In this case you could use the Search Utility, build a class to extract the list of items from the website, use a saved set of filters, set up the email destination handler, and add the application to a job scheduler to run at periodic intervals.  You would then receive an email alert every time a search of the website returns new or different results.
	* If you want to write a Java class to extract the list of items, you can grab the latest release, pull the jars into your development enviroment, write a new Candidate and CandidateSource, build the project setting SearchUtility as the executable, update the properties to point to your new Candidate and CandidateSource, and it should be ready to run.
	* If you don't want to mess with writing any Java code or building your own project, you can use the provided CSVCandidateSource, and have some other script or tool that dumps out a CSV file that the CSVCandidateSource can pick up when it runs.

For further details, see the following sections:
* [Installing the latest demo release](Installing-the-latest-demo-release)
	* [Example run through of the demo](Example-run-through-of-the-demo) <-- If you want a quick look at what this is all about, check here!
* [Setting up the source for development](Setting-up-the-source-for-development)
* [Using the Object Filter Engine](Using-the-Object-Filter-Engine)
	* [Requirements for your Candidate objects](Requirements-for-your-Candidate-objects)
	* [How the filters work](How-the-filters-work)
	* [How statistics collection works](How-statistics-collection-works) (v1.2)
* [Using the Search Utility](Using-the-Search-Utility)
	* [Properties file setup](Properties-file-setup)
	* [Filter Sources](Filter-Sources)
	* [Candidate Sources](Candidate-Sources)
	* [Statistics Collection Sources](Statistics-Collection-Sources) (v1.2)
	* [Result Destinations](Result-Destinations)

