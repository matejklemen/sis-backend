package api.interceptors;

import api.interceptors.annotations.LogApiCalls;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

@Interceptor
@LogApiCalls
public class LogApiCallsInterceptor {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @AroundInvoke
    public Object logApiCall(InvocationContext context) throws Exception {
        StringBuilder b = new StringBuilder();
        b.append("API Call of method [[ ").append(context.getMethod().getName()).append(" ]]");
        b.append(" Parameters: [");
        for(int i = 0; i< context.getParameters().length; i++) {
            Object v = context.getParameters()[i];
            if(i != 0) b.append(", ");
            b.append(v);
        }
        b.append("]");

        log.info(b.toString());

        return context.proceed();
    }

}
