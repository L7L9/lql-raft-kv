package com.lql.raft.rpc.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: node.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ConsistencyServiceGrpc {

  private ConsistencyServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ConsistencyService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.lql.raft.rpc.proto.VoteParam,
      com.lql.raft.rpc.proto.VoteResponse> getVoteRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "voteRequest",
      requestType = com.lql.raft.rpc.proto.VoteParam.class,
      responseType = com.lql.raft.rpc.proto.VoteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.lql.raft.rpc.proto.VoteParam,
      com.lql.raft.rpc.proto.VoteResponse> getVoteRequestMethod() {
    io.grpc.MethodDescriptor<com.lql.raft.rpc.proto.VoteParam, com.lql.raft.rpc.proto.VoteResponse> getVoteRequestMethod;
    if ((getVoteRequestMethod = ConsistencyServiceGrpc.getVoteRequestMethod) == null) {
      synchronized (ConsistencyServiceGrpc.class) {
        if ((getVoteRequestMethod = ConsistencyServiceGrpc.getVoteRequestMethod) == null) {
          ConsistencyServiceGrpc.getVoteRequestMethod = getVoteRequestMethod =
              io.grpc.MethodDescriptor.<com.lql.raft.rpc.proto.VoteParam, com.lql.raft.rpc.proto.VoteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "voteRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.lql.raft.rpc.proto.VoteParam.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.lql.raft.rpc.proto.VoteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ConsistencyServiceMethodDescriptorSupplier("voteRequest"))
              .build();
        }
      }
    }
    return getVoteRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.lql.raft.rpc.proto.AppendEntriesParam,
      com.lql.raft.rpc.proto.AppendEntriesResponse> getAppendEntriesRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AppendEntriesRequest",
      requestType = com.lql.raft.rpc.proto.AppendEntriesParam.class,
      responseType = com.lql.raft.rpc.proto.AppendEntriesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.lql.raft.rpc.proto.AppendEntriesParam,
      com.lql.raft.rpc.proto.AppendEntriesResponse> getAppendEntriesRequestMethod() {
    io.grpc.MethodDescriptor<com.lql.raft.rpc.proto.AppendEntriesParam, com.lql.raft.rpc.proto.AppendEntriesResponse> getAppendEntriesRequestMethod;
    if ((getAppendEntriesRequestMethod = ConsistencyServiceGrpc.getAppendEntriesRequestMethod) == null) {
      synchronized (ConsistencyServiceGrpc.class) {
        if ((getAppendEntriesRequestMethod = ConsistencyServiceGrpc.getAppendEntriesRequestMethod) == null) {
          ConsistencyServiceGrpc.getAppendEntriesRequestMethod = getAppendEntriesRequestMethod =
              io.grpc.MethodDescriptor.<com.lql.raft.rpc.proto.AppendEntriesParam, com.lql.raft.rpc.proto.AppendEntriesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AppendEntriesRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.lql.raft.rpc.proto.AppendEntriesParam.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.lql.raft.rpc.proto.AppendEntriesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ConsistencyServiceMethodDescriptorSupplier("AppendEntriesRequest"))
              .build();
        }
      }
    }
    return getAppendEntriesRequestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ConsistencyServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ConsistencyServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ConsistencyServiceStub>() {
        @java.lang.Override
        public ConsistencyServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ConsistencyServiceStub(channel, callOptions);
        }
      };
    return ConsistencyServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ConsistencyServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ConsistencyServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ConsistencyServiceBlockingStub>() {
        @java.lang.Override
        public ConsistencyServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ConsistencyServiceBlockingStub(channel, callOptions);
        }
      };
    return ConsistencyServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ConsistencyServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ConsistencyServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ConsistencyServiceFutureStub>() {
        @java.lang.Override
        public ConsistencyServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ConsistencyServiceFutureStub(channel, callOptions);
        }
      };
    return ConsistencyServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * 请求投票选举的rpc接口
     * </pre>
     */
    default void voteRequest(com.lql.raft.rpc.proto.VoteParam request,
        io.grpc.stub.StreamObserver<com.lql.raft.rpc.proto.VoteResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getVoteRequestMethod(), responseObserver);
    }

    /**
     * <pre>
     * 追加日志的rpc接口
     * </pre>
     */
    default void appendEntriesRequest(com.lql.raft.rpc.proto.AppendEntriesParam request,
        io.grpc.stub.StreamObserver<com.lql.raft.rpc.proto.AppendEntriesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAppendEntriesRequestMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ConsistencyService.
   */
  public static abstract class ConsistencyServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ConsistencyServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ConsistencyService.
   */
  public static final class ConsistencyServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ConsistencyServiceStub> {
    private ConsistencyServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ConsistencyServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ConsistencyServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 请求投票选举的rpc接口
     * </pre>
     */
    public void voteRequest(com.lql.raft.rpc.proto.VoteParam request,
        io.grpc.stub.StreamObserver<com.lql.raft.rpc.proto.VoteResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getVoteRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 追加日志的rpc接口
     * </pre>
     */
    public void appendEntriesRequest(com.lql.raft.rpc.proto.AppendEntriesParam request,
        io.grpc.stub.StreamObserver<com.lql.raft.rpc.proto.AppendEntriesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAppendEntriesRequestMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ConsistencyService.
   */
  public static final class ConsistencyServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ConsistencyServiceBlockingStub> {
    private ConsistencyServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ConsistencyServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ConsistencyServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 请求投票选举的rpc接口
     * </pre>
     */
    public com.lql.raft.rpc.proto.VoteResponse voteRequest(com.lql.raft.rpc.proto.VoteParam request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getVoteRequestMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 追加日志的rpc接口
     * </pre>
     */
    public com.lql.raft.rpc.proto.AppendEntriesResponse appendEntriesRequest(com.lql.raft.rpc.proto.AppendEntriesParam request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAppendEntriesRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ConsistencyService.
   */
  public static final class ConsistencyServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ConsistencyServiceFutureStub> {
    private ConsistencyServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ConsistencyServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ConsistencyServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 请求投票选举的rpc接口
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.lql.raft.rpc.proto.VoteResponse> voteRequest(
        com.lql.raft.rpc.proto.VoteParam request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getVoteRequestMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 追加日志的rpc接口
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.lql.raft.rpc.proto.AppendEntriesResponse> appendEntriesRequest(
        com.lql.raft.rpc.proto.AppendEntriesParam request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAppendEntriesRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VOTE_REQUEST = 0;
  private static final int METHODID_APPEND_ENTRIES_REQUEST = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VOTE_REQUEST:
          serviceImpl.voteRequest((com.lql.raft.rpc.proto.VoteParam) request,
              (io.grpc.stub.StreamObserver<com.lql.raft.rpc.proto.VoteResponse>) responseObserver);
          break;
        case METHODID_APPEND_ENTRIES_REQUEST:
          serviceImpl.appendEntriesRequest((com.lql.raft.rpc.proto.AppendEntriesParam) request,
              (io.grpc.stub.StreamObserver<com.lql.raft.rpc.proto.AppendEntriesResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getVoteRequestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.lql.raft.rpc.proto.VoteParam,
              com.lql.raft.rpc.proto.VoteResponse>(
                service, METHODID_VOTE_REQUEST)))
        .addMethod(
          getAppendEntriesRequestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.lql.raft.rpc.proto.AppendEntriesParam,
              com.lql.raft.rpc.proto.AppendEntriesResponse>(
                service, METHODID_APPEND_ENTRIES_REQUEST)))
        .build();
  }

  private static abstract class ConsistencyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ConsistencyServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.lql.raft.rpc.proto.NodeProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ConsistencyService");
    }
  }

  private static final class ConsistencyServiceFileDescriptorSupplier
      extends ConsistencyServiceBaseDescriptorSupplier {
    ConsistencyServiceFileDescriptorSupplier() {}
  }

  private static final class ConsistencyServiceMethodDescriptorSupplier
      extends ConsistencyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ConsistencyServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ConsistencyServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ConsistencyServiceFileDescriptorSupplier())
              .addMethod(getVoteRequestMethod())
              .addMethod(getAppendEntriesRequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
