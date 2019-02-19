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
* [Installing the latest demo release](Installing%20the%20latest%20demo%20release.md)
	* [Example run through of the demo](Example%20run%20through%20of%20the%20demo.md) <-- If you want a quick look at what this is all about, check here!
* [Setting up the source for development](Setting%20up%20the%20source%20for%20development.md)
* [Using the Object Filter Engine](Using%20the%20Object%20Filter%20Engine.md)
	* [Requirements for your Candidate objects](Requirements%20for%20your%20Candidate%20objects.md)
	* [How the filters work](How%20the%20filters%20work.md)
	* [How statistics collection works](How%20statistics%20collection%20works.md) (v1.2)
* [Using the Search Utility](Using%20the%20Search%20Utility.md)
	* [Properties file setup](Properties%20file%20setup.md)
	* [Filter Sources](Filter-Sources.md)
	* [Candidate Sources](Candidate-Sources.md)
	* [Statistics Collection Sources](Statistics%20Collection%20Sources.md) (v1.2)
	* [Result Destinations](Result-Destinations.md)

