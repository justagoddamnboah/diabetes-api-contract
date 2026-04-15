package com.example.demo.exceptions;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomDataFetchingExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(
            DataFetcherExceptionHandlerParameters params) {
        
        Throwable exception = params.getException();
        
        if (exception instanceof ShowNotFoundException) {
            // Log it but let DGS handle its default output for DgsEntityNotFoundException 
            // Or construct a custom GraphQL error.
            System.err.println("Entity not found: " + exception.getMessage());
            
            graphql.GraphQLError graphqlError = graphql.GraphqlErrorBuilder.newError(params.getDataFetchingEnvironment())
                    .message(exception.getMessage())
                    .errorType(graphql.ErrorType.DataFetchingException)
                    .build();
            
            return CompletableFuture.completedFuture(
                DataFetcherExceptionHandlerResult.newResult().error(graphqlError).build()
            );
        }

        // Generic error
        graphql.GraphQLError graphqlError = graphql.GraphqlErrorBuilder.newError(params.getDataFetchingEnvironment())
                    .message("Internal Server Error")
                    .errorType(graphql.ErrorType.DataFetchingException)
                    .build();
                    
        return CompletableFuture.completedFuture(
                DataFetcherExceptionHandlerResult.newResult().error(graphqlError).build()
        );
    }
}
