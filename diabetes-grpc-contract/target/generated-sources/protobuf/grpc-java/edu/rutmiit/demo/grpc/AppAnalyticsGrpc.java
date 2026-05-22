package edu.rutmiit.demo.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.66.0)",
    comments = "Source: app_analytics.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AppAnalyticsGrpc {

  private AppAnalyticsGrpc() {}

  public static final java.lang.String SERVICE_NAME = "appanalytics.AppAnalytics";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeAppRequest,
      edu.rutmiit.demo.grpc.AppAnalysisResponse> getAnalyzeAppMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AnalyzeApp",
      requestType = edu.rutmiit.demo.grpc.AnalyzeAppRequest.class,
      responseType = edu.rutmiit.demo.grpc.AppAnalysisResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeAppRequest,
      edu.rutmiit.demo.grpc.AppAnalysisResponse> getAnalyzeAppMethod() {
    io.grpc.MethodDescriptor<edu.rutmiit.demo.grpc.AnalyzeAppRequest, edu.rutmiit.demo.grpc.AppAnalysisResponse> getAnalyzeAppMethod;
    if ((getAnalyzeAppMethod = AppAnalyticsGrpc.getAnalyzeAppMethod) == null) {
      synchronized (AppAnalyticsGrpc.class) {
        if ((getAnalyzeAppMethod = AppAnalyticsGrpc.getAnalyzeAppMethod) == null) {
          AppAnalyticsGrpc.getAnalyzeAppMethod = getAnalyzeAppMethod =
              io.grpc.MethodDescriptor.<edu.rutmiit.demo.grpc.AnalyzeAppRequest, edu.rutmiit.demo.grpc.AppAnalysisResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AnalyzeApp"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.rutmiit.demo.grpc.AnalyzeAppRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.rutmiit.demo.grpc.AppAnalysisResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AppAnalyticsMethodDescriptorSupplier("AnalyzeApp"))
              .build();
        }
      }
    }
    return getAnalyzeAppMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AppAnalyticsStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AppAnalyticsStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AppAnalyticsStub>() {
        @java.lang.Override
        public AppAnalyticsStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AppAnalyticsStub(channel, callOptions);
        }
      };
    return AppAnalyticsStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AppAnalyticsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AppAnalyticsBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AppAnalyticsBlockingStub>() {
        @java.lang.Override
        public AppAnalyticsBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AppAnalyticsBlockingStub(channel, callOptions);
        }
      };
    return AppAnalyticsBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AppAnalyticsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AppAnalyticsFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AppAnalyticsFutureStub>() {
        @java.lang.Override
        public AppAnalyticsFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AppAnalyticsFutureStub(channel, callOptions);
        }
      };
    return AppAnalyticsFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void analyzeApp(edu.rutmiit.demo.grpc.AnalyzeAppRequest request,
        io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.AppAnalysisResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAnalyzeAppMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AppAnalytics.
   */
  public static abstract class AppAnalyticsImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AppAnalyticsGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AppAnalytics.
   */
  public static final class AppAnalyticsStub
      extends io.grpc.stub.AbstractAsyncStub<AppAnalyticsStub> {
    private AppAnalyticsStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AppAnalyticsStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AppAnalyticsStub(channel, callOptions);
    }

    /**
     */
    public void analyzeApp(edu.rutmiit.demo.grpc.AnalyzeAppRequest request,
        io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.AppAnalysisResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAnalyzeAppMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AppAnalytics.
   */
  public static final class AppAnalyticsBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AppAnalyticsBlockingStub> {
    private AppAnalyticsBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AppAnalyticsBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AppAnalyticsBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.rutmiit.demo.grpc.AppAnalysisResponse analyzeApp(edu.rutmiit.demo.grpc.AnalyzeAppRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAnalyzeAppMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AppAnalytics.
   */
  public static final class AppAnalyticsFutureStub
      extends io.grpc.stub.AbstractFutureStub<AppAnalyticsFutureStub> {
    private AppAnalyticsFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AppAnalyticsFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AppAnalyticsFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.rutmiit.demo.grpc.AppAnalysisResponse> analyzeApp(
        edu.rutmiit.demo.grpc.AnalyzeAppRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAnalyzeAppMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ANALYZE_APP = 0;

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
        case METHODID_ANALYZE_APP:
          serviceImpl.analyzeApp((edu.rutmiit.demo.grpc.AnalyzeAppRequest) request,
              (io.grpc.stub.StreamObserver<edu.rutmiit.demo.grpc.AppAnalysisResponse>) responseObserver);
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
          getAnalyzeAppMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.rutmiit.demo.grpc.AnalyzeAppRequest,
              edu.rutmiit.demo.grpc.AppAnalysisResponse>(
                service, METHODID_ANALYZE_APP)))
        .build();
  }

  private static abstract class AppAnalyticsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AppAnalyticsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.rutmiit.demo.grpc.AppAnalyticsOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AppAnalytics");
    }
  }

  private static final class AppAnalyticsFileDescriptorSupplier
      extends AppAnalyticsBaseDescriptorSupplier {
    AppAnalyticsFileDescriptorSupplier() {}
  }

  private static final class AppAnalyticsMethodDescriptorSupplier
      extends AppAnalyticsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AppAnalyticsMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (AppAnalyticsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AppAnalyticsFileDescriptorSupplier())
              .addMethod(getAnalyzeAppMethod())
              .build();
        }
      }
    }
    return result;
  }
}
