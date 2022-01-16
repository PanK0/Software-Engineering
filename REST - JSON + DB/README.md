# REST - JSON and DATABASE

**NB**: in the entire project, the word *flight* is mispelled as *fligth*. Sad but true reality.

This is the dirtree of the project:



![restjson-dirtree.jpg](https://github.com/PanK0/Software-Engineering/blob/main/pics/restjson-dirtree.jpg?raw=true)



JSON is conceptually equivalent to XML, but it is more human friendly.

Use postman requesting for a GET at *localhost:8080/fligths/1*

This REST service is managed through JSON and uses a real **database**. [Here](https://www.baeldung.com/java-jdbc) : (https://www.baeldung.com/java-jdbc) there is a tutorial on JDBC.

JDBC is the java interface for connecting to a **relational DBMS** in which you interact with the DBMS via SQL queries. Here we are using SQLite, an SQL interface to a file. The file is stored somewhere in the filesystem and it is passed in our application as `arg[0]` when the service is started.

# Service

We don't have anymore an in-memory data structure, now we use a real database. Some text for a non-database version is left as a comment in the code.

## FligthsRepository.java

*ComplexRESTService\src\main\java\it\sapienza\softeng\complexrestservice\FligthsRepository.java*

The file containing the database is passed as `arg[0]` when the service is started. **So, set it up in the options**

Setup a connection with the **database**. This happens in the method `setConnection()`, and the path to the file should be the string `pos`. The driver is loaded at the line `Class.forName("org.sqlite.JDBC");`; note that this string is different depending on the different DBMS technology that you are using.

The connection is invoked at line `DriverManager.getConnection("jdbc:sqlite:")+pos`, where `pos` is the absolute path of the database file.

The *FlightRepository* will be instantiated as a **singleton** in order to be sure that the persistance layer of our application will be running on the same database.

 In this interface are activated the `GET` and the `PUT`. Here is not allowed to DELETE or add (POST) new flight, but only to read (GET) and to update (PUT) flights: we only have `@GET` and `@PUT` annotations.

The `@GET` has the annotation `@Path("{fligthId}")` and the notation `@Produces()"application/json")`, that explicitates that the result is a json object. The method `getFligth()` calls the method `findById()`.

The `@PUT` has the annotation `@Path("{fligthId}")` and `@Consumes("application/json")`, saying that the body of the request will be in JSON format. The method `updateFligth()`, given a *flightid* and a *flight* object, will update the object in the database through the method `update()`.

In the `findById()` method there is the **real interaction** with the database. In the database suppose to have one table named *fligth*: the SQL query to recover a flight is `select * from fligth where id=?`, where the `?` at runtime should be passed as the id of the flight I want to find.

In JDBC I have to prepare an object that is a prepared statement: here it is the object `stat`. The method `setString()` is used to substitute the `?` with the wanted value for *id*.

The result of the query is saved into an object `ResultSet`. To read the record, iterate with the `rs.next()`: here, if there is a next (it should be only one record, because flights should be unique) then I create an object *fligth* with the wanted *id* and *name*, then I **close** the *ResultSet*. At the end **return** the flight.

Conversely, the `update()` method requires as input a *fligthId* and an object *fligth*. The SQL syntax to update something in the database is `update fligth set name=? where id=?` where the two `?` are two different runtime parameters, the first one going for the name and the second one for the id.

When you **execute** the update, the **return** is an integer that is the number of rows that are affected. We put a control that the update has been performed only on one row, and this is managed there.

## Fligth.java

*ComplexRESTService\src\main\java\it\sapienza\softeng\complexrestservice\Fligth.java*

This class is a **DTO = Data Transfer Object**, because it does nothing in practice. It works as the same as in XML, but it has the annotation `@JacksonXmlRootElement(localName = "Fligth")`.

It contains the get and set methods, plus the **overrides** of hashCode and equals.

## Server.java

*ComplexRESTService\src\main\java\it\sapienza\softeng\complexrestservice\Server.java*

Also **imports** `jackson`.

It is very similar to the version with XML except for some specific code used to manage JSON.

The `factoryBean` and the `FligthsRepository fr` are created. Then the **connection** is set for the `fr` object in order to connect it to the **database** passed as `arg[0]` and the address *localhost:8080*, that is passed to the server, is added to the `factoryBean`.

This `factoryBean` works with JSON. You create a `JacksonJaxbJsonProvider()` that allow the **marshalling** . 

Set the `BindingFactoryManager` and do other stuffs, that means that my bean is based on jaxrs and on jackson.

Then **create** the server and run the `while(true)` cycle.

## Pomfile

*ComplexRESTService\pom.xml*

You need in the `dependencies` some packages like `com.fasterxml.jackson` in which there are some libraries to use.

To **use SQLite** I need to import `org.xerial`.

Then also `org.codehaus.mojo`

# Database

In a real system a database usually already exists, but here is presented some code to create it for this project.

## Pomfile

*DBSimpleManager\pom.xml*

For the database the only `dependency` is `org.xerial` to use `sqlite-jdbc`.

## DBManager.java

*DBSimpleManager\src\main\java\it\sapienza\softeng\dbsimplemanager\DBManager.java*

The first input parameter at `arg[0]` is the full path of the **database** file.

It is a simple java program that prepares the database. The program accepts as `arg[1]` two modes: **create** mode and **showing** mode, to be passed as argument.

In **showing** mode it shows the situation of the database, printing all the flights.

In **creating** mode I create the database, filling it with all it is needed. It drops the existing database and recreates it from scratch.

First of all, create a `Connection` at the line `DriverManager.getConnection("jdbc:sqlite:"+args[0]);`, where in `args[0]` we should have the full path of the database.

When in the **create** mode I `setAutoCommit(false)`, I execute the query immediately. I do these instructions for all the needed flights to insert.

# Client

## Pomfile

*JSONClient\pom.xml*

As `dependencies` we need `org.apache.cxf` with `cxf-rt-frontend-jaxrs`, then we need `httpclient` from `org.apache.httpcomponents` and then we need two specific libraries of jackson to work with JSON and for demarshalling.

## Flight.java

*JSONClient\src\main\java\it\sapienza\softeng\jsonclient\Fligth.java*

This is, again, a **DTO**, as the one seen above.

## Client.java

*JSONClient\src\main\java\it\sapienza\softeng\jsonclient\Client.java*

It is very similar to the XML one.

In this case the `BASE_URL` is `"http://localhost:8080/fligths/"`. Then the `client` is created.

In this case, to work with JSON, you need to create an object `mapper` and then an object `url`, in where to run `openStream()`: in this method you are making the query, since when the query happens it is in the *openStream*, in this moment you are sending the **GET**.

In JSON the **marshalling** happens at the line `Fligth fl = (Fligth)mapper.readValue(input, Fligth.class);` , where you have to pass the *input* stream and the class in which the JSON object should b e converted.

For the **PUT**, you create another `objectMapper` and a new Fligth. Then a `String json = objectMapper.writeValueAsString(newFl);` is created. An object `httpPut` is created that states that we want to update the flight *2*. After that, set the `httpPut` object  and **execute** the **PUT**.

Notice that **the object HttpResponse is not your value, but the full response!** So if you want to access the data you have to open the stream with `openSteam()` and read the value again.
