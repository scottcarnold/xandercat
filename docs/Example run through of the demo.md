# Demo Walkthrough

Download the latest release.  Create a folder where you want the application to reside at.  Lets say you want to use c:\ofe_demo.  Unzip the release to that folder.  You should something like the following in that folder after unzipping it:

{{
lib/
XanderCatOFE-1.1.jar
cposearch.bat
cposearch.properties
ev-cpo-20160211.htm
example-filters.txt
log4j.properties
}}

Now take a look at _cposearch.properties_.  This is where the demo application setup resides.  It will look like this:

{{
############## GENERIC SEARCH UTILITY PROPERTIES ##########################

logging.properties.file=log4j.properties
previous.results.file=previous-results.dat
candidate.class=org.xandercat.ofeapps.teslamodels.CPOModelS

############## FILTER SOURCE PROPERTIES ###################################

filter.source.class=org.xandercat.ofe.searchutility.filter.CSVFilterSource
filter.source.file=example-filters.txt

############## CANDIDATE SOURCE PROPERTIES ################################

# Properties needed for CSVCandidateSource	
#candidate.source.class=org.xandercat.ofe.searchutility.candidate.CSVCandidateSource
#candidate.source.fields=vin,url,cpoInv,location,trim,aptp,dualMotor,rearFacingSeats,coldWeatherPackage,soundStudio,superchargerEnabled,smartAirSuspension,dualChargers,color,roof,wheels,interior,year,miles,price,dateAdded
#candidate.source.date.format=yyyy-MM-dd HH:mm

# Properties needed for EVCPOOfflineHtmlCandidateSource
candidate.source.class=org.xandercat.ofeapps.teslamodels.EVCPOOfflineHtmlCandidateSource
candidate.source.file=ev-cpo-20160211.htm

############## RESULT DESTINATION PROPERTIES ###############################

# Properties needed for ConsoleResultDestination
result.destination.class=org.xandercat.ofe.searchutility.result.ConsoleResultDestination
result.destination.report.title=CPO Model S Search
result.destination.report.only.when.changes.exist=true

# Properties needed for MailResultDestination
#result.destination.class=org.xandercat.ofe.searchutility.result.MailResultDestination
#result.destination.report.title=CPO Model S Search
#result.destination.report.only.when.changes.exist=true
#result.destination.mail.smtp.auth=true
#result.destination.mail.smtp.starttls.enable=true
#result.destination.mail.smtp.host=
#result.destination.mail.smtp.port=
#result.destination.mail.to=
#result.destination.mail.from=
#result.destination.mail.username=
#result.destination.mail.password=
}}

Any line starting with a pound sign (#) is a comment (or commented out).

Looking through the properties, you can see:
* Logging is configured by another properties file named log4j.properties and the last search (if there is one) will be stored in file _previous-results.dat_.
* Filters (search criteria) will be defined in a CSV file (a comma seperated value text file) named example-filters.txt.
* The actual objects to search will come from the html file named _ev-cpo-20160211.htm_, parsed out of that file by the EVCPOOfflineHtmlCandidateSource class.
* Search results will be sent to the console (printed to the screen) with a report title of "CPO Model S Search", and that will only be printed if the search results are different from the previous run.
* You can optionally switch out the candidate source to pull data from a CSV file instead of the HTML file.  However, you would need to figure out how to generate that CSV file yourself.
* You can optionally switch out the result destination to send the search results via email instead of printing to the screen.  However, you would need to get the mail configuration set up right for that.

Lets leave it as is.  If you were to run it now, it would read the filters in _example-filters.txt_, pull out a list of Tesla Model S car info from _ev-cop-20160211.htm_, store the results of that seach for later use in _previous-results.dat_, and print the current search results to the screen assuming they are different from the previous run.

Now take a look at your filters:

{{
# Valid values for fieldName and fieldType are:
#   vin (String), cpoInv (String), location (String), trim (String), aptp (String), 
#   dualMotor (Boolean), rearFacingSeats (Boolean), coldWeatherPackage (Boolean), 
#   soundStudio (Boolean), superchargerEnabled (Boolean), smartAirSuspension (Boolean), 
#   dualChargers (Boolean), color (String), roof (String), wheels (String), 
#   interior (String), year (Integer), miles (Integer), price (Integer)
#
# Valid values for matchType are:
#   * String:  STARTS_WITH, ENDS_WITH, EQUALS, CONTAINS 
#   * Integer:  LESS_THAN, GREATER_THAN, EQUALS
#   * Boolean:  EQUALS
#
# Valid values for weight are: 
#   required, excluded, low, medium, high, maximum, [floating point number between 0 and 1](floating-point-number-between-0-and-1) 
#
# Match value can be:
#  * String:  any string, can be wrapped in quotes (value will be case insensitive)
#  * Integer:  a whole number
#  * Boolean:  true, false


threshold,       0.75
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
}}

Based on these filters, you want to find a Tesla Model S85 with a pano roof for less than $80,000 with less than 40,000 miles that was listed after January 1st, 2016.  Those attributes are required.  You also want to exclude cars with black interior, as well as cars with red exterior color.  In addition, you strongly prefer Nappa leather interior.  You also prefer the car be located in San Francisco or San Diego, you prefer white color and 21 inch wheels, and you prefer the price be less than $70,000 if possible.  You also have a slight preference for brown exterior color, though you don't like it as much as white.  And after all that, you only want to see cars that are considered at least a 75% match for that criteria.

Now there are no previous search results yet, as you have never run the search before.  So all the matches found will be considered new matches.  Run it and see what the output looks like.  I should look like this:

{{
CPO Model S Search
Threshold for match is set at 75%

Changes Since Last Report:

ADDED :
76% CPOModelS [vin=P19847, url=http://www.teslamotors.com//models/preowned/P19847, cpoInv=CPO, location=New England, trim=S85, aptp=TP, dualMotor=null, rearFacingSeats=null, coldWeatherPackage=null, soundStudio=null, superchargerEnabled=null, smartAirSuspension=null, dualChargers=null, color=Dark Blue Metallic, roof=Pano, wheels=21-Silver, interior=Tan Nappa, year=2013, miles=39233, price=62800, dateAdded=Tue Jan 05 13:00:00 CST 2016](vin=P19847,-url=http___www.teslamotors.com__models_preowned_P19847,-cpoInv=CPO,-location=New-England,-trim=S85,-aptp=TP,-dualMotor=null,-rearFacingSeats=null,-coldWeatherPackage=null,-soundStudio=null,-superchargerEnabled=null,-smartAirSuspension=null,-dualChargers=null,-color=Dark-Blue-Metallic,-roof=Pano,-wheels=21-Silver,-interior=Tan-Nappa,-year=2013,-miles=39233,-price=62800,-dateAdded=Tue-Jan-05-13_00_00-CST-2016)

ADDED :
76% CPOModelS [vin=P22679, url=http://www.teslamotors.com//models/preowned/P22679, cpoInv=CPO, location=Wash, DC, trim=S85, aptp=TP, dualMotor=null, rearFacingSeats=null, coldWeatherPackage=null, soundStudio=null, superchargerEnabled=null, smartAirSuspension=null, dualChargers=null, color=Black Solid, roof=Pano, wheels=21-Silver, interior=Tan Nappa, year=2013, miles=13602, price=64000, dateAdded=Tue Jan 05 15:00:00 CST 2016](vin=P22679,-url=http___www.teslamotors.com__models_preowned_P22679,-cpoInv=CPO,-location=Wash,-DC,-trim=S85,-aptp=TP,-dualMotor=null,-rearFacingSeats=null,-coldWeatherPackage=null,-soundStudio=null,-superchargerEnabled=null,-smartAirSuspension=null,-dualChargers=null,-color=Black-Solid,-roof=Pano,-wheels=21-Silver,-interior=Tan-Nappa,-year=2013,-miles=13602,-price=64000,-dateAdded=Tue-Jan-05-15_00_00-CST-2016)

ADDED :
76% CPOModelS [vin=P29450, url=http://www.teslamotors.com//models/preowned/P29450, cpoInv=CPO, location=Wash, DC, trim=S85, aptp=TP, dualMotor=null, rearFacingSeats=null, coldWeatherPackage=null, soundStudio=null, superchargerEnabled=null, smartAirSuspension=null, dualChargers=null, color=Grey Metallic, roof=Pano, wheels=21-Grey, interior=Tan Nappa, year=2014, miles=20751, price=61500, dateAdded=Fri Jan 15 18:20:00 CST 2016](vin=P29450,-url=http___www.teslamotors.com__models_preowned_P29450,-cpoInv=CPO,-location=Wash,-DC,-trim=S85,-aptp=TP,-dualMotor=null,-rearFacingSeats=null,-coldWeatherPackage=null,-soundStudio=null,-superchargerEnabled=null,-smartAirSuspension=null,-dualChargers=null,-color=Grey-Metallic,-roof=Pano,-wheels=21-Grey,-interior=Tan-Nappa,-year=2014,-miles=20751,-price=61500,-dateAdded=Fri-Jan-15-18_20_00-CST-2016)

ADDED :
76% CPOModelS [vin=P27889, url=http://www.teslamotors.com//models/preowned/P27889, cpoInv=CPO, location=Los Angeles, trim=S85, aptp=TP, dualMotor=null, rearFacingSeats=null, coldWeatherPackage=null, soundStudio=null, superchargerEnabled=null, smartAirSuspension=null, dualChargers=null, color=Dark Blue Metallic, roof=Pano, wheels=21-Silver, interior=Tan Nappa, year=2013, miles=23083, price=69300, dateAdded=Tue Jan 12 14:00:00 CST 2016](vin=P27889,-url=http___www.teslamotors.com__models_preowned_P27889,-cpoInv=CPO,-location=Los-Angeles,-trim=S85,-aptp=TP,-dualMotor=null,-rearFacingSeats=null,-coldWeatherPackage=null,-soundStudio=null,-superchargerEnabled=null,-smartAirSuspension=null,-dualChargers=null,-color=Dark-Blue-Metallic,-roof=Pano,-wheels=21-Silver,-interior=Tan-Nappa,-year=2013,-miles=23083,-price=69300,-dateAdded=Tue-Jan-12-14_00_00-CST-2016)

ADDED :
76% CPOModelS [vin=P32543, url=http://www.teslamotors.com//models/preowned/P32543, cpoInv=CPO, location=New York, trim=S85, aptp=TP, dualMotor=null, rearFacingSeats=null, coldWeatherPackage=null, soundStudio=null, superchargerEnabled=null, smartAirSuspension=null, dualChargers=null, color=Pearl White, roof=Pano, wheels=19-Base, interior=Tan Nappa, year=2014, miles=27215, price=62000, dateAdded=Tue Jan 12 15:00:00 CST 2016](vin=P32543,-url=http___www.teslamotors.com__models_preowned_P32543,-cpoInv=CPO,-location=New-York,-trim=S85,-aptp=TP,-dualMotor=null,-rearFacingSeats=null,-coldWeatherPackage=null,-soundStudio=null,-superchargerEnabled=null,-smartAirSuspension=null,-dualChargers=null,-color=Pearl-White,-roof=Pano,-wheels=19-Base,-interior=Tan-Nappa,-year=2014,-miles=27215,-price=62000,-dateAdded=Tue-Jan-12-15_00_00-CST-2016)

ADDED :
76% CPOModelS [vin=P27379, url=http://www.teslamotors.com//models/preowned/P27379, cpoInv=CPO, location=San Francisco, trim=S85, aptp=TP, dualMotor=null, rearFacingSeats=null, coldWeatherPackage=null, soundStudio=null, superchargerEnabled=null, smartAirSuspension=null, dualChargers=null, color=Black Solid, roof=Pano, wheels=19-Base, interior=Grey Nappa, year=2013, miles=26425, price=59100, dateAdded=Tue Jan 05 15:10:00 CST 2016](vin=P27379,-url=http___www.teslamotors.com__models_preowned_P27379,-cpoInv=CPO,-location=San-Francisco,-trim=S85,-aptp=TP,-dualMotor=null,-rearFacingSeats=null,-coldWeatherPackage=null,-soundStudio=null,-superchargerEnabled=null,-smartAirSuspension=null,-dualChargers=null,-color=Black-Solid,-roof=Pano,-wheels=19-Base,-interior=Grey-Nappa,-year=2013,-miles=26425,-price=59100,-dateAdded=Tue-Jan-05-15_10_00-CST-2016)


Current Candidates:

76% http://www.teslamotors.com//models/preowned/P19847
76% http://www.teslamotors.com//models/preowned/P22679
76% http://www.teslamotors.com//models/preowned/P27379
76% http://www.teslamotors.com//models/preowned/P27889
76% http://www.teslamotors.com//models/preowned/P29450
76% http://www.teslamotors.com//models/preowned/P32543


Current Filters:

roof EQUALS "Pano" [required](required) [weight of 0.5](weight-of-0.5)
wheels STARTS_WITH "21" [weight of 0.5](weight-of-0.5)
trim STARTS_WITH "S85" [required](required) [weight of 0.5](weight-of-0.5)
color CONTAINS "Red" [excluded](excluded)
color CONTAINS "White" [weight of 0.5](weight-of-0.5)
color CONTAINS "Brown" [weight of 0.25](weight-of-0.25)
price LESS_THAN 80,000 [required](required) [weight of 0.5](weight-of-0.5)
price LESS_THAN 70,000 [weight of 0.5](weight-of-0.5)
location EQUALS "San Francisco" [weight of 0.5](weight-of-0.5)
location STARTS_WITH "San Diego" [weight of 0.5](weight-of-0.5)
dateAdded AFTER Fri Jan 01 00:00:00 CST 2016 [required](required) [weight of 0.5](weight-of-0.5)
interior CONTAINS "Nappa" [weight of 0.75](weight-of-0.75)
interior CONTAINS "Black" [excluded](excluded)
miles LESS_THAN 40,000 [required](required) [weight of 0.5](weight-of-0.5)
}}

:_Note:  The null values you are seeing for some fields is because those values are only available if you have a subscription to the website.  If you subscribed to the ev-cpo.com website, logged in, saved a copy of the HTML page, and used that as the input, you would then have all of those values to search upon as well._

The search results shows you 3 sections -- changes, matches, and filters.  
* Changes shows you how your current search results have changed since the last search.  Since you never ran this search before, every search result is considered as ADDED.  If on the next search, a candidate's attributes had changed and/or the match percentage had changed, it would be listed as CHANGED.  If on the next search, a prior candidate is no longer there or no longer matches for some reason, it would be listed as REMOVED.
* Matches shows you a summary of all of your current matches, and what their match percentage is.
* Filters is just a reminder as to what your filters were for the search.

The descriptions shown in the changes and matches sections is determined by what values are returned for the short and full description of the candidate class.  For this demo, the short description just returns the listing URL for the match, while the full description shows you a list of everything (although not very nicely formatted).  

Now imagine how this could be further built upon.  It's already doing a pretty good job.  But lets say you have some other website you want to monitor for something that you had approval to screen scrape from.  You could write a new CandidateSource that screen scrapes the website, and switch the result destination to email.  You then set up the search utility to be launched from a job scheduler at semi-regular intervals.  And now you have automatic alerts for whenever the search of whatever you are searching for returns new or different results!  Nice!

