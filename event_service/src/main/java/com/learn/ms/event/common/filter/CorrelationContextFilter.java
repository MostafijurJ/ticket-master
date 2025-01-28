package com.learn.ms.event.common.filter;

import com.learn.ms.event.common.utils.CorrelationContextHolder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CorrelationContextFilter implements Filter {
    private static String CORRELATION_ID = "correlationId";

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws ServletException, IOException {
        final String correlationId = ((HttpServletRequest) request).getHeader(CORRELATION_ID);

        CorrelationContextHolder.setCorrelationIdInContext(correlationId);

        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(CORRELATION_ID, correlationId);

        chain.doFilter(request, response);
    }
}
