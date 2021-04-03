For installing gRPC runtime in Python

1. upgrade your version of pip:
$ python3 -m pip install --upgrade pip

2. install gRPC:
$ python3 -m pip install grpcio

( or, to install it system wide:
$ sudo python -m pip install grpcio 
)

$ python3 -m pip install grpcio-tools


For compiling a .proto file (specifically in the example)
$ python3 -m grpc_tools.protoc --python_out=. --grpc_python_out=. --proto_path=./proto HelloService.proto