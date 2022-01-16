# SOAP

Implementation of a service that provides informations about a collection of students.

Here is how to create a Docker Image.

This is the directory tree:

![dirtree](https://github.com/PanK0/Software-Engineering/blob/main/pics/soap-dirtree.jpg?raw=true)

# Soap-WebService

## Pomfile

*SOAP-WebService\pom.xml*

Here we use a new `plugin`, the `maven-shade-plugin`, that allows do create standalone .jar that contains all the dependencies inside. The .jar file will be useful to create a docker image.

Here is used **cxf**, the technology used to provide the SOAP infrastructure.
