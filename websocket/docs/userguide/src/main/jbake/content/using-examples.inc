///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
These CLI examples can be customized as necessary.
///////////////////////////////////////////////////////////////////////

Start the Junit tests using the following command:

--
[source,oac_no_warn]
----
mvn verify
----
--


[[GCMCU]]

Example 5-1 {TechnologyShortName} TCK Signature Tests

To run the {TechnologyShortName} TCK signature tests, enter the
following commands:

[source,subs="attributes"]
----
mvn verify -Dit.test=com.sun.ts.tests.websocket.signaturetest.**
----

[[GCMBV]]


Example 5-2 Single Test Directory

To run a single test directory, enter the following commands:

[source,subs="attributes"]
----
mvn verify -Dit.test={singleTestDirectoryExample}.**
----

[[GCMCA]]


Example 5-3 Subset of Test Directories

To run a subset of test directories, enter the following commands:

[source,subs="attributes"]
----
mvn verify -Dit.test={subsetTestDirectoryExample}.**
----