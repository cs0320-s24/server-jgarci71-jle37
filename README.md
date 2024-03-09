> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details
This project is a locally hosted server project that allows for JSON responses for 
a CSVParser. The following endpoints provide the listed functionality: 

- ```csvload```: loads the csv file and returns a success or failure response based on ability to parse
- ```csvview```: returns a JSON containing a List of Lists representing the tokens in the CSV. 
- ```csvsearch```: allows to search the currently loaded CSV for a given token and search params.

See the **How To** section for more on the query params. 

This project supports a 4th endpoint that allows for our ACSAPI to fetch data from the Census API for broadband data on a
given state and county. 

- ```broadband```: given a state and county, retrieves the broadband data


See the **How To** section for more on the query params for this endpoint.
# Design Choices
We decided to create four different classes to represent the routes for the for the four different endpoints. 
Three of these routes followed the dependency injection pattern, requiring for the three of them to use a shared state. 
The load and view classes retrieve the data through this state, not by reparsing the files, so this dependancy is necessary. 

Our broadBand handler also has a dependency: it requires an ACSDatasource. This interface enforces the query method, 
which is responsible for returning the broadband data for the given state and county. The use of this dependency allows 
for one to use the broadBand handler with our API, a mock API or a proxy API. 

We used the proxy pattern to implement a ACSAPI with Caching by wrapping the first API class. This class
makes use of the Google Guava Cache Library to reduce the number of unnecessary network requests. 
# Errors/Bugs
the slashes...

# Tests
We performed integration and unit tests for the four different endpoints. We used both malformed and wellformed API calls 
to ensure that the success and failure responses were being serialized without error. 

We performed integration tests with our API, ensuring that it was pulling accurate data from the Census API

To perform integration tests on our API, we also used mock data, limitting the amount of live network requests were being
made to the server. 

Finally, we implemented tests for our ACSAPI with caching. 

# How to

First, run ```mvn package``` in the source directory to compile the program and run the tests. 

Then, use ```./run```. This should print out a port at which the local server is hosted. 

##CSV

To use the CSV Parser commands, you must first load in a file that is located in the data folder. This can be done 
by performing the following call: 

```csvload?file=FILE_PATH```

Note that the ``FILE_PATH`` field should be accessible from a starting directory of `data/`. 

After loading in the file, you can perform a view or search of the CSV. 

```csvview```
This endpoint takes in no query parameters, as its only functionality is to return the data in a JSON. 

```csvsearch?token=TOKEN&column=COLUMN```

In order to search, a token must be passed in. Additionally, an optional column identifier can be provided, either
as an index or as a header name. Note that inputting a column header will assume that your file has a header, so do not 
use this with a headerless file, as it may result in unexpected results. 

##Broadband
Within the local server, you can call the following endpoint: 

```broadband?state=STATE_NAME&county=COUNTY_NAME```

Note that the wild card argument `*` is not supported, as broadband only allows querying within a specific county
in a specific state. Neither argument can be null for a successful result. 