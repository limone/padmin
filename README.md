### powerdns admin ###

Java-based web application to manage a PowerDNS server.  Designed and developed to be a drop in replacement for Poweradmin, just with a mildly different UI and no templating functionality (since we didn't desire said functionality).

##### Usage #####

* mvn clean package
* configure Tomcat and add -Dconfig=/path/to/padmin.properties
* edit padmin.properties and configure the DB connection
* deploy war

##### Notes #####
Java 7  
Wicket 1.5  
Spring 3.1  
Hibernate 4.1  

* Currently talks to a PostgreSQL back end
* Tested on Tomcat 7.0.25