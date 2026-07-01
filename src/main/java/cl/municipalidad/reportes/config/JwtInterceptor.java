package cl.municipalidad.reportes.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

public class JwtInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            String authHeader = attributes.getRequest().getHeader("Authorization");
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                request.getHeaders().add("Authorization", authHeader);
                log.info("[ms-reportes] JwtInterceptor propagó el token JWT hacia: {}", request.getURI());
            } else {
                log.warn("[Error] JwtInterceptor ejecutado, pero no se encontró token Bearer en la petición original.");
            }
        } else {
            log.error("[Error] JwtInterceptor no pudo acceder al contexto de la petición.");
        }
        
        return execution.execute(request, body);
    }
}