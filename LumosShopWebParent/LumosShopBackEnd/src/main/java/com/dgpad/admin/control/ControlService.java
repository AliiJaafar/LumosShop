package com.dgpad.admin.control;

import com.lumosshop.common.entity.control.Control;
import com.lumosshop.common.entity.control.ControlType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ControlService {

    @Autowired
    private ControlRepository controlRepository;

    public List<Control> ListAllControls() {
        return (List<Control>) controlRepository.findAll();
    }

    public StandardControlCenter getStandardControl() {
        List<Control> controls = new ArrayList<>();
        List<Control> standardControls = controlRepository.findByType(ControlType.STANDARD);
        List<Control> currencyUnitControls = controlRepository.findByType(ControlType.CURRENCY_UNIT);

        controls.addAll(standardControls);
        controls.addAll(currencyUnitControls);

        return new StandardControlCenter(controls);
    }

    public void saveAll(List<Control> controls) {
        controlRepository.saveAll(controls);
    }

    public List<Control> getAllEmailSystems() {
        return controlRepository.findByType(ControlType.EMAIL_SYSTEM);
    }
    public List<Control> getAllCurrencySystems() {
        return controlRepository.findByType(ControlType.CURRENCY_UNIT);
    }
    public List<Control> getAllMessageOutlines() {
        return controlRepository.findByType(ControlType.MESSAGE_OUTLINE);
    }
    public List<Control> getAllPaymentControl() {
        return controlRepository.findByType(ControlType.PAYMENT);
    }
}


