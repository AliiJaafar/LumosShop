package com.dgpad.controlCenter;


import com.lumosshop.common.constant.Constants;
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
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) req;
        String addr = servletRequest.getRequestURL().toString();

        if (addr.endsWith(".css") || addr.endsWith(".js") || addr.endsWith(".png") || addr.endsWith(".jpg") || addr.endsWith(".jpeg")) {
            chain.doFilter(req, response);
            return;
        }

        List<Control> standard = controlService.getStandardControl();
        standard.forEach(control -> {
//            System.out.println(control);
            req.setAttribute(control.getKey(), control.getValue());
        });

        req.setAttribute("B2_PATH", Constants.B2_ADDRESS);
        chain.doFilter(req, response);
    }
}
