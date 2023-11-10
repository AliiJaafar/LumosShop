package com.dgpad.controlCenter;


import com.lumosshop.common.entity.control.Control;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ControlFilter implements Filter {

    @Autowired
    private ControlService controlService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String url = servletRequest.getRequestURL().toString();

        if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".jpeg")) {
            chain.doFilter(request, response);
            return;
        }

        List<Control> standard = controlService.getStandardControl();
        standard.forEach(control -> {
//            System.out.println(control);
            request.setAttribute(control.getKey(), control.getValue());
        });

        chain.doFilter(request, response);
    }
}
