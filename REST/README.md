# REST - XML

Simple RESTful service in which data are transferred between client and server in **XML**.

This is the directory tree:



![restxml-dirtree.jpg](https://github.com/PanK0/Software-Engineering/blob/main/pics/restxml-dirtree.jpg?raw=true)



How to check whether a REST service is available? There is one tool useful to do this, [postman](https://www.postman.com/), to download and install on the machine. Postman is useful when you want to easily interact with the web service.

Postman allows you to act as a client for the web service.

If the web service is running, for example, on *localhost:8080*, postman lets you to generate HTTP requests like GET, POST, PUT, DELETE.

Run this service and ask a GET for *localhost:8080/courses/1* and you will receive a result getting back from the web service.

If you ask a GET for *localhost:8080/courses/1/students/2* it returns the student with id 2 for the course with id 1.

This helps to make clear what a REST web service is: when asket, it transfers information over HTTP.

# Web Service

When dealing with REST web service we use **specific libraries**. In particular we use the same midware infrastructure we used for SOAP, that is **apache cxf** (In the pomfile, ad the tag `dependencies`). 

## Pomfile

*RESTService\pom.xml* 

Include in the `dependencies` tag the `org.apache.cxf` library that provides the restful service like *jetty* and *jaxrs* in the `artifacts`.

Then we need our execution `plugin` that allows the system to properly work, that is `org.codehaus.mojo` , and, if I want the web service to work as a standalone .jar, include the `maven-shade-plugin` as in SOAP.

## Student.java

*RESTService\src\main\java\it\sapienza\softeng\rest\Student.java*

Students are enrolled in *Courses*. The class `Student` provides data concerning the students to the server. Students are characterized by an `id` and by a `name`. This java class will be **marshalled** in XML.

This type of class is called **DTO = Data Transfer Object**, since it has no real functionality, its role is only to be a data structure used to send data between the client and the service. DTO classes are characterized by **properties** and **methods** like *get* and *set*. The class needs to be done with the **overriding of equals**, because you can't leave in your system the equivalence by reference, but you need to redefine the equivalence by value.

In this class the annotation `@XmlRootElement(name = "Student")` from the library that allows to work with XML (*javax.xml.bind.annotation*). It is used to define an XML Root Element named *Student*. This class basically provides the **marshalling** of the *student* tag.

## Course.java

*RESTService\src\main\java\it\sapienza\softeng\rest\Course.java*

This class is not only a seen as an xml tag, but this also contains other elements, like *students*. The *Course* is an *aggregation* of students.

Being a class that has to be transferred, this class should also be set as a **DTO** with get and set  methods and the  `@XmlRootElement(name = "Course")` annotation for the **marshalling**.

In this class, other thatn basic information, I have a `List<Student>` with the students of the course with the proper get and set methods.

Since *Course* is an active object, i.e. *Course* is a root, I have to answer to calls like *GET, POST, DELETE*.

In the method `getStudent()`, with the annotation `@GET` I declare that the method is the code to be invoked when an HTTP GET is received. Here is stated then, when a GET is received, the parameter recovered from the GET is `studentId` . So, the `int studdntId` will be demarshalled from the `@PathParam{"studentId"}`, recovered from the HTTP GET. Once I have the `studentId` I have to return the `findById()` method that recovers what  I need from the parameters.
This method is associated with `@GET` and with `@PathParam{"studentId"}`, that specifies that I can't ask for the list of students without passing a parameter  `studentId`: if from postman I ask for a GET  *localhost:8080/courses/1/students/* withoud specifying the student ID I will get an error.

The method `findById()` is the method that goes in the data structure that maintains the real data (REST has CRUD operations over an hypothetical database. In this case **the database is the array list of students**, but it can be a real database). When I have `findById()` I have to lookup into the data structure that has the data: if what I am looking for is not found, I return `null`.

The **post** is implemented in the method `createStudent()`, that is equipped with the annotation `@POST`. Here I do not declare any parameter because the post will be the field. This method returns and object `Response` and adds a student into the data structure, managing conflicts if you find an equal student.

The same happens with the `deleteStudent()` method, equipped with the annotation `@DELETE`.  Here is also required the parameter `studentId`.

## CourseRepository.java

*RESTService\src\main\java\it\sapienza\softeng\rest\CoursesRepository.java*

Here are specified the **routes** for courses. With the annotation `@Path("/courses")` I declare that this class corresponds with the path */courses* and with  `@Produces("text/xml")` I say that this service is producing XML text.

Here are stored the data structures that make the courses available. Courses are stored into an `HashMap` over `<Integer, Course>`.

The `@GET` only allows me to obtain specific courses, not the course list, since it is specified that a parameter `@Path("{courseId}")` has to be passed.

Also the `@PUT` needs a parameter.

More, a **subroute** is enabled with `@Path("{courseId}/students"` that returns all the students of the course with id `courseId`. This uses the method `findByID()`. This method looksup for the courses into the data structure and, if the *getKey()* of the course is equal to *id* return the value of those course, that will be the list of the students.

## APIServer.java

*RESTService\src\main\java\it\sapienza\softeng\rest\APIServer.java*

This is the **main** class of the service.

In **cxf** when you want to work to REST service you need a `JAXRSServerFactoryBean` object, that is the factory class of the system that allow to create a component (bean) to include inside the http server.

Here, once we have the `factoryBean`, we set the **route class**, that now is the `CoursesRepository.class`. Then we set the **provider** as a *SingletonResourceProvider* so that we are sure that will be only one class *CourseRepository* at runtime. Then set the **address** of the webservice, that is for now *0.0.0.0:8080*.

Finally **create** the bean and make it work forever with the `while(true)`.


# Client

This is able to interact with the web service. The client is a simple program that tries to read and to make an update.

## Pomfile

RESTClient\pom.xml

Two libraries have to be imported in the `dependencies` : `org.apache.cxf`, specifically the *artifact* `cxf-rt-frontend-jaxrs`, the library that provides the marshalling on the frontend, and the library `org.apache.httpcomponents` with the *artifact* `httpclient` that is in charge of creating inside the code the http request and to manage the http interaction with the server.

## Student.java

*RESTClient\src\main\java\it\sapienza\softeng\rest\client\Student.java*

The *Student* class is equivalent to the one seen in the server side. Here I also make the override of the method `toString()`.

## Course.java

*RESTClient\src\main\java\it\sapienza\softeng\rest\client\Course.java*

The object `Course` is related to the `@XmlRootElement(name="Course")`.

It is characterized by an *int*, a *string* and a *list of students*, with all get and set methods and the **override** of equals, because this is a DTO.

## Client.java

*RESTClient\src\main\java\it\sapienza\softeng\rest\client\Client.java*

The client only works with HTTP Requests. There is no interface from which you can create STUB classes.

The class `org.apache.http.client` provides a lot of methods to work with clients.

A  `CloseableHttpClient client` object is created. 

Now I can create an object `Course`. The `getCourse()` method returns a course given an id. It uses a variable `URL`. Then, here you can open a **stream**, create an `InputStreamReader` passing the input and if you pass it to the method `unmarshal()` with the demarshalling class (that is `Course.class`) you will have an unmarshalling. The same happens with the method `getStudent()` for **students**.

The method `createValidStudent()` implements the creation of a POST. To do it, I read the XML assuming that there is a file called `newStudent.xml` that is located in the resources packages (*RESTClient\src\main\resources\newStudent.xml*. This file is basically used to avoid to write a big string into the code) . Then you create an `InputStream resourceStream` and then execute the POST.
