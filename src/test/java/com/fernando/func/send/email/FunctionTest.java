package com.fernando.func.send.email;

//import com.microsoft.azure.functions.*;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;

//import java.util.*;
//import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;


/**
 * Unit test for Function class.
 */
public class FunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testHttpTriggerJava() throws Exception {
//        // Setup
//        @SuppressWarnings("unchecked")
//        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);
//
//        final Map<String, String> queryParams = new HashMap<>();
//        queryParams.put("name", "Azure");
//        doReturn(queryParams).when(req).getQueryParameters();
//
//        final Optional<String> queryBody = Optional.empty();
//        doReturn(queryBody).when(req).getBody();
//
//        doAnswer(new Answer<HttpResponseMessage.Builder>() {
//            @Override
//            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
//                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
//                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
//            }
//        }).when(req).createResponseBuilder(any(HttpStatus.class));
//
//        final ExecutionContext context = mock(ExecutionContext.class);
//        doReturn(Logger.getGlobal()).when(context).getLogger();
//
//        // Invoke
//        final HttpResponseMessage ret = new Function().run(req, context);
//
//        // Verify
//        assertEquals(HttpStatus.OK, ret.getStatus());

        // Setup
        // final String message = "{\"userId\": 1, \"post\": \"Nuevo post\"}";
        // final ExecutionContext context = mock(ExecutionContext.class);
        // doReturn(Logger.getGlobal()).when(context).getLogger();

        // // Invoke
        // new Function().run(message, context);

        // // Verify
        // verify(context.getLogger(), times(1)).info("Mensaje recibido del Tópico de Service Bus: " + message);
    }
}
