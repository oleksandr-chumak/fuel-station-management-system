package com.fuelstation.managmentapi.common.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;

public abstract class BaseExceptionHandler {
      
    protected Map<String, Object> getRequestDetails(WebRequest request) {
        Map<String, Object> details = new HashMap<>();
        
        if (request instanceof ServletWebRequest) {
            HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
            details.put("method", servletRequest.getMethod());
            details.put("path", servletRequest.getRequestURI());
            details.put("clientIp", servletRequest.getRemoteAddr());
            details.put("userAgent", servletRequest.getHeader("User-Agent"));
            
            if (!servletRequest.getParameterMap().isEmpty()) {
                details.put("parameters", servletRequest.getParameterMap());
            }
        }
        
        return details;
    } 
}
