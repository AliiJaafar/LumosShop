package com.dgpad.admin.business;

import com.dgpad.admin.control.ControlService;
import com.lumosshop.common.entity.control.Control;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BusinessRecordController {

    @Autowired
    private ControlService controlService;

    @GetMapping("/Analyses/sales")
    public String displaySalesAnalyses(HttpServletRequest httpServletRequest) {
        FetchingAllCurrencyControl(httpServletRequest);
        return "Analyses/business-records";
    }

    private void FetchingAllCurrencyControl(HttpServletRequest httpServletRequest) {
        List<Control> controls = controlService.getAllCurrencySystems();
        for (Control c : controls) {
            httpServletRequest.setAttribute(c.getKey(), c.getValue());
        }
    }

}
