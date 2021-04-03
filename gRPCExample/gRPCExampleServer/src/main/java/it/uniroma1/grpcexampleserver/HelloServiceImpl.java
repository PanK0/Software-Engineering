package it.uniroma1.grpcexampleserver;

import io.grpc.stub.StreamObserver;
import it.uniroma1.gRPCExample.HelloRequest;
import it.uniroma1.gRPCExample.HelloResponse;
import it.uniroma1.gRPCExample.HelloServiceGrpc.HelloServiceImplBase;


public class HelloServiceImpl extends HelloServiceImplBase {

    @Override
    public void hello(
      HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

        System.out.println("... the server has received: " + request.getFirstName() + " " + request.getLastName());
        
        String greeting = new StringBuilder()
          .append("Hello, ")
          .append(request.getFirstName())
          .append(" ")
          .append(request.getLastName())
          .toString();

        HelloResponse response = HelloResponse.newBuilder()
          .setGreeting(greeting)
          .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
