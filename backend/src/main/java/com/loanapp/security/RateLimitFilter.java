package com.loanapp.security;
import jakarta.servlet.FilterChain; import jakarta.servlet.ServletException; import jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException; import java.time.Instant; import java.util.Map; import java.util.concurrent.ConcurrentHashMap;
@Component public class RateLimitFilter extends OncePerRequestFilter {
 @Value("${app.security.rate-limit.requests:60}") private int maxRequests;
 @Value("${app.security.rate-limit.window-seconds:60}") private long windowSeconds;
 private final Map<String,Bucket> buckets=new ConcurrentHashMap<>();
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
  String key=request.getRemoteAddr()+"|"+request.getRequestURI(); long now=Instant.now().getEpochSecond(); Bucket b=buckets.computeIfAbsent(key,k->new Bucket(now));
  synchronized(b){ if(now-b.started>=windowSeconds){b.started=now;b.count=0;} b.count++; if(b.count>maxRequests){response.setStatus(429);response.setHeader("Retry-After",String.valueOf(Math.max(1,windowSeconds-(now-b.started))));response.setContentType("application/json");response.getWriter().write("{\"status\":429,\"message\":\"Too many requests. Try again later.\"}");return;} }
  chain.doFilter(request,response);
 }
 private static final class Bucket { long started; int count; Bucket(long s){started=s;} }
}