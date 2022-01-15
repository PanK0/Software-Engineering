# JMS Stock Market
JMS is an API that allows to send and receive messages on queue and topics, used for Message Oriented Middleware.

In our case the **JMS Provider** is ActiveMQ, used with a container.

The **JMS Domain** allows to understand the reference of the application.

Her is used **Docker**. It is used to allow multiple applications run on the same server.

This is the **Directory Tree** of the whole application:

![dirtree](https://github.com/PanK0/Software-Engineering/blob/main/pics/jmsstockmarket_dirtree.jpg?raw=true)

Also here is used **Maven**.

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

A simple plugin is able to produce a jar file that is self executable (the given jar file). This plugin in the tag `plugin` is `org.apache.maven.plugin` and it is called maven-assembly-plugin. It creates a .jar file with all the dependencies that are included, so that all the dependencies are built into a standalone jar.

In the `mainClass` tag you should put the main class of your program. In this case it is `it.uniroma1.jmsstockmarketservant.StockMarketServant`.

# JMSStockMarket Servant

## StockMarketServant.java

*JMSStockMarketServant\src\main\java\it\uniroma1\jmsstockmarketservant\StockMarketServant.java*

It starts two JMS clients: one is `NotificatoreAcquisto` and the other is `ProduttoreQuotazioni`.

The  objects are started with the `start()` method. This will make the objects start their job.

## ProduttoreQuotazioni.java

*JMSStockMarketServant\src\main\java\it\uniroma1\jmsstockmarketservant\ProduttoreQuotazioni.java*

This object **publishes** in the queue. So, two libraries are required: `javax.jms.*` , that provides all the interfaces from the midware, and `javax.naming.*` for accessing the JND Service.

These functionalities are provided by the library activemq-all that has the STUB needed to interact with the activemq server.

The class `ProduttoreQuotazioni` is allocated only once in the code. 

In this example **our database is simply an array of strings**, named `titoli[]`. The program will pick a random value from this array and publishes it with the function `scegliTitolo()`. 

The function `valore()` chooses a value to give with the quotation. In the reality, all these three parts should live into a database.

The `Logger` allors to print into the console what is happening.

The `start()` method is the one called by the Servant. Here is the **real interaction** with the JMS Provider.  All the subsequent objects are needed for a JMS connection. In our case the string `destinationName` is *"dynamicTopics/Quotazioni"*

The `Properties` objext `props` is dependant from the specific JMS provider you are using. In *activemq* the string to be used to have an `INITIAL_CONTEXT_FACTORY` is the one stated in the code. For other services that are not *activemq* this string will be different.

The `PROVIDER_URL` in our case is `localhost:61616`, because we are deploying on the same machine. 

Passing the properties to the object `jndiContext`. With this you can lookup for the `connectionfactory` . You need a connection factory to obtain a destination, that we find through the `lookup()` method. Otherwise, in the `catch`, we have an exception raised.

The **final catch** is used to close the connection in any case.

With the `connectionfactory` we can have a `connection` and then a `session`. Now you can create a `producer` for the selected destination.

The `producer` creates a `TextMessage` . The message is then filled into the `while(true)`, bty setting all its properties. The message is sent through the `send()` method, then the servant sleeps and restarts the cycle.

This object `producer` is running in the thread of ProduttoreQuotazioni. 3400

## NotificatoreAcquisto.java

*JMSStockMarketServant\src\main\java\it\uniroma1\jmsstockmarketservant\NotificatoreAcquisto.java*

It is a **subscriber** of the *dynamicTipics/Ordini* topic.

It is also a **publisher**.

When a message is received it randomly takes a decision, then it reads data from the message and then publishes a new message with the same user and the status (if the stocks have been successfully buyied or not).

# JMSStockMarket Client

## Utente.java

Represents an authenticated user. It is a **singleton** instance, because we want only one instance at runtime. We assure this using the method `getInstange()` . At runtime, when we ask for an user, we are returned the authenticated user.

## Quotazione.java

It is what I want to receive from the JNDI. The class implements `Comparable`, means that we have to implement a `compareTo()` and an `equals()` method.

## QuotazioniTableModel.java

The class `QuotazioniTableModel` takes our "database", by recovering messages from the JMS Service, as the `Vector<Quotazione> lista` . In that list will be inserted the received quotations.

## AzioniJMSListener.java

*JMSStockMarketClient\src\main\java\it\uniroma1\jmsstockmarketclient\AzioniJMSListener.java*

Receives the message from the JMS Provider and writes it into the table.

How to implement an object in JMS? You need to implement a **listener** that implements the `MessageListener`, that is the interface.

To receive from a JMS provider we need `topicConnection`, `topicSession`, a `Destination` and a `Producer`. 

I create a `jndiContext` and a `connectionFactory`. Then lookup for the `ConnectionFactory` and , as in the servant, create the connection, the session, and the destination. 

Then must create an object `TopicSubscriber`  to the session to create it over a destination. Here I say that the self object `AzioniJMSLIstener` is the subscriber, so we have to implement the `onMessage()` method.

In the **catch**, each time a message arrives to the client, the *topic subscriber* will invoke the *message listener*, so we'll run an instance of *onMessage()* that will do stuffs. The `onMessage()` takes the properties from the message, then creates a new *quotazione* that is added to my model. Then the view is updated.

## CompraJMSManager.java

*JMSStockMarketClient\src\main\java\it\uniroma1\jmsstockmarketclient\CompraJMSManager.java*

When I push **ordina** on the gui (`CompraListener.java`), the client sends a message to the servant, the servant sends back to the client a message with the confirmation or the negation of the action and the client captures it, showing the response to the user.

In `CompraListener.java`, in the gui, this action is performed by the `CompraJMSManager` object there created. When the data is sent back, the listener asks the JMSManager to perform the buying.

The `CompraJMSManager` is a publisher this time: it needs the same objects for **properties**, address, **initial context**. But now **I use another topic:** `dynamicTopic/Ordini`. The publisher is inside the method `compra()` that asks for some arguments and the user. Then there is the user control.

In the `try` the `TextMessage` is created, with properties like user, name of the stock, price and quantity. After this, i **publish** this message and, to have the answer back, I (`this`) want to be a subscriber of the previous destination, but this time with a filter by passing a query (`String query`) that specifies that I want to know this specific attempt of buying. So I set myself (`this`) as a subscriber. To avoid **race conditions** it is better to first subscribe and then to publish the message.

When the answer to my request for buying arrives I manage it into the `onMessage()` method: we extract a boolean property called *Status* that says whether the buying happened or not.

## StockMarketClient.java

*JMSStockMarketClient\src\main\java\it\uniroma1\jmsstockmarketclient\StockMarketClient.java*

The client just launches an `AzioniFrame`.

# How to run

- Run the docker engine with activemq
- Run the .jar that starts publishing
- Run the system
