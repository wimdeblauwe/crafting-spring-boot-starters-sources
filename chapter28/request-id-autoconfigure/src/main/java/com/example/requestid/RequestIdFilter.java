package com.example.requestid;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

public class RequestIdFilter extends OncePerRequestFilter {

    private final String headerName;
    private final String mdcKey;

    public RequestIdFilter(String headerName, String mdcKey) {
        this.headerName = headerName;
        this.mdcKey = mdcKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestId = request.getHeader(headerName);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        response.setHeader(headerName, requestId);
        MDC.put(mdcKey, requestId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(mdcKey);
        }
    }
}
