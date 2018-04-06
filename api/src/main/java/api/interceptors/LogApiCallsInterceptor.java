package api.interceptors;

import api.interceptors.annotations.LogApiCalls;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.logging.Logger;

@Interceptor
@LogApiCalls
public class LogApiCallsInterceptor {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @AroundInvoke
    public Object logApiCall(InvocationContext context) throws Exception {
        log.info(String.format("API Call of method [[ %s ]] with parameters: %s", context.getMethod().getName(), Arrays.toString(context.getParameters())));
        return context.proceed();
    }

}
