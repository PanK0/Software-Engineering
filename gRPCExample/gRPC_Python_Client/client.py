import logging
import grpc
import HelloService_pb2
import HelloService_pb2_grpc

def run():
    with grpc.insecure_channel('localhost:8080') as channel:
        stub = HelloService_pb2_grpc.HelloServiceStub(channel)
        response = stub.hello(HelloService_pb2.HelloRequest(firstName='Alan',lastName='Turing'))
        print("The client received: " + response.greeting)

if __name__ == '__main__':
    logging.basicConfig()
    run()
