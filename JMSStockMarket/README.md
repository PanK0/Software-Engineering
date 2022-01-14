# JMS Stock Market
JMS is an API that allows to send and receive messages on queue and topics, used for Message Oriented Middleware.

In our case the **JMS Provider** is ActiveMQ, used with a container.

The **JMS Domain** allows to understand the reference of the application.

Her is used **Docker**. It is used to allow multiple applications run on the same server.

This is the **Directory Tree** of the whole application:

![dirtree](https://github.com/PanK0/Software-Engineering/blob/main/pics/jmsstockmarket_dirtree.jpg?raw=true)

# Docker Part - Active MQ for Publish/Subscribe
On the terminal run `docker pull rmohr/activemq` , that downloads from the registry a container that is called *activemq* .

Now `docker run -p 61616:61616 -p 8161:8161 rmohr/activemq` runs the container that exposes two interfaces on ports 61616, that is the port in which you  have to connet and in which you provide the quue, and 8161 is the port for the web interface of activemq that allows you to monitor it. The numbers after the `:` are the ports exposed by the host OS, that in this case are the same exposed from the container.

Now the container is running as an activemq server that is waiting for connections.

Go on `localhost:8161` and see that the service is up and running. In the classical installation, the username and password are respectively *admin* and *admin*. Logging in, you can see what are the published topics.

Now download the jar file provided by the professor:
`java -cp JMSStockMarketServant-1.0-SNAPSHOT-jar-with-dependencies.jar it.uniroma1.jmsstockmarketservant.StockMarketServant`

This will start publishing messages in our activemq server. It will publish quotations in our pubisher/subscribers system.

We now have a topic called *Ordini* and another called *Quotazioni*, that we will use in our application.

# Messaging Oriented Midware and JMS

JMS is an API on top on Java that allow you to **send and receive messages on queues and topics**.

You first need to have a **JMS Provider**, that is an entity that implements the JMS API for a messaging product. In our case our provider is *activemq*.

Then you have a **JMS Client** that uses the service of the provider through the API. In this case the client is the .jar file that through the API publishe messages on the topics *Ordini* and *Quotazioni*.

**JMS Domains** are namings that allow you to understand the reference of the application. In JMS you have two different domains: the *point to point* domain, based on queues, and the *publish-subscribe* domain that is topic based.

In the **point to point** domain you send messages to a specific queue and the client extracts messages from the queues.

In the **publish-subscribe** domain the system takes care of distributing the messages belonging to a specific part of the content hierarchy to all clients interested in that content.

In JMS you call **common interfaces** those API provided by the JMS provider that allow yout code to publish and receive messages from the queue.

**Administrative Objects** are objects created by the provider to make the service work. Two important administrative objects are the **Connection Factory**, that is used by a client to establish a connection to the JMS provider, and the **Destination**, used by a client to specify the destination of messages it will send.

Connection Factory and Destination are framed into the **Java Naming and Directory Interface (JNDI) namespace**, provided by the provider. As a client you ask to JNDI for what you need and then you can connect to the JMS Provider.

Through a Connection Factory you create a **connection**, via a connection you create a **session**, from a session you create a **message producer** and a **message consumer** that respectively send and receive messages to a **destination** and again, through the session you can create specific **messages** that can be sent and received to/from a destination.

A JMS tipically:
- Uses JNDI to find a `ConnectionFactory` object
- Use JNDI to find one or more `Destination` objects. Predefined identifiers can also be used to instantiate new Destinations
- Use the `ConnectionFactory` to establish a `Connection` with the JMS Provider
- Use the `Connection` to create one or more `Session`
- Combine `Session` and `Destination` to create the needed `MessageConsumer` and `MessageProducer`
- Tell the `Connection` to start delivery messages

So two classes can be created: `Sender` and `Receiver`

### Sender Class
The sender initializes an object of type `Context` (object offered by the JND service), then creates an `InitialContext`, from which you connect to the JND service.

When connected to the JND service you lookup for something, maybe a string "queue" (maybe there is an administrative object whose name is queue), creating an object `queue`.

Then you create an object `ConnectionFactory` that looks up for the connection factory.

Then you create a `ConnectionQueue` and then a `Session`. Finally you create a `Sender`.

With the `Sender` you can create a message and with the object sender you can put messages inside the queue.

### Receiver Class
Similarly as the Sender, but you create an object `Receiver` after creating the session. When you subscribe you indicate to which topic you want to subscribe.

Specify which is the `Listener` in charge of being notified when a message arrives. (MOM is an asynchronous method to exchange message, using callback methos. On JMS the callback method is called `onMessage()`).

Through the object you receive the message and so you can read it.

The subscriber needs to incorporate a second class that implements the class  `MessageListener` that implements a method `onMessage()`. When a message is available, this method is called and then you decide what to do with the message.

### Important
In JMS the concurrent acces to the administrative objects `Destination` , `ConnectionFactory` and `Connection` is managed by the JMS Provider. So you may have **multiple clients** connecting to those objects.

Conversely, `Session`, `Message Producer` and `Message Consumer` are **not designed for concurrent usage** by multiple threads. So this means that if you want to support transactions via an object session it is difficult to have a multithreading support. In case you want multithreading in your client you need to **use multiple sessions**.

More, the Sender adn the Receiver adopt a **FIFO** approach, but only between a given sender and a given receiver. 

Another important aspect is the **transactionality of session**. This means that your `Session` is `AUTO_ACKNOWLEDGE` this means that your object automatically acknowledges messages **avoiding duplicates**. If it is `CLIENT_AKNOWLEDGE` it is your resonsibility to manage the duplicates.

Delivery modes can be `NON_PERSISTENT` or `PERSISTENT`, i.e. the message will be delivered only and only once or the provider will take care of the delivery of the message.

### Structure of a message
In JMS a message consists in an **header**, a **properties** field and a **body**.

Each message has some useful to know attributes in his **header**:
- `JMSMessageID`, each message has an unique value
- `JMSCorrelationID`, allows to correlate a message to another one
- `JMSReplyTo`, where you can specify that a specific destination supplied by a client should  be used to send  back the answer
- `JMSTimestamp`, timestamp
- `JMSPriority`, level of priority from 0 to 9

The **property** part is application specific and you can create your properties of type `<string, value>` . The receiver can **only read** the properties. With the method `getPropertyNames()` are returned the `Enumerations` of the property. 

The **body** part is the content of the message.

# Pomfile
In the pomfile of the client, be sure to include the specified `dependency` for `activemq`.


