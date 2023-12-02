package com.dgpad.admin.control;

import com.lumosshop.common.entity.control.Control;
import com.lumosshop.common.entity.control.ControlCenter;
import org.apache.commons.logging.Log;

import java.util.List;
import java.util.logging.Logger;

public class StandardControlCenter extends ControlCenter {
    private static final Logger logger = Logger.getLogger(StandardControlCenter.class.getName());

    public StandardControlCenter(List<Control> controlList) {
        super(controlList);
    }

    public void updateCurrencySymbol(String symbol) {
        // Log the symbol before the update
        logger.info("Updating currency symbol to: { " + symbol +" }");

        super.update("CURRENCY_SYMBOL", symbol);

        // Log a message after the update
        logger.info("Currency symbol updated successfully.");
    }
    public void updateCurrencyFormat(String format) {
        // Log the symbol before the update
        logger.info("Updating currency format to: { " + format +" }");

        super.update("CURRENCY_FORMAT", format);

        // Log a message after the update
        logger.info("Currency format updated successfully.");
    }


}

