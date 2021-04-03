package it.uniroma1.grpcexampleserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class MyServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server;
        server = ServerBuilder
                .forPort(8080)
                .addService(new HelloServiceImpl()).build();

        server.start();
        System.out.println("Server is running and waiting for a contact.");
        server.awaitTermination();
    }
}
