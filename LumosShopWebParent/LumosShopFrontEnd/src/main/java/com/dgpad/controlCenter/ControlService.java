package com.dgpad.controlCenter;

import com.lumosshop.common.entity.Currency;
import com.lumosshop.common.entity.control.Control;
import com.lumosshop.common.entity.control.ControlType;
import com.lumosshop.common.entity.control.MailCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ControlService {

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    public PaymentControlCenter RetrievePaymentControl() {
        List<Control> paymentControl = controlRepository.findByType(ControlType.PAYMENT);
        return new PaymentControlCenter(paymentControl);
    }
    public List<Control> getStandardControl() {
        return controlRepository.findByManyType(ControlType.STANDARD, ControlType.CURRENCY_UNIT);
    }

    public MailCenter RetrieveTheControlsOfMailSystem() {
        List<Control> controls = controlRepository.findByType(ControlType.EMAIL_SYSTEM);
        controls.addAll(controlRepository.findByType(ControlType.MESSAGE_OUTLINE));
        return new MailCenter(controls);
    }

    public CurrencyControlCenter getCurrencyControls() {
        List<Control> controls = controlRepository.findByType(ControlType.CURRENCY_UNIT);
        return new CurrencyControlCenter(controls);
    }

    public String getCurrencyFormat() {
        Control control = controlRepository.findByKey("CURRENCY_ID");
        Integer currencyId = Integer.parseInt(control.getValue());
        Currency currency = currencyRepository.findById(currencyId).get();
        return currency.getCode();
    }
}


