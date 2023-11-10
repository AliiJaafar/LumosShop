package com.dgpad.controlCenter;

import com.lumosshop.common.entity.control.Control;
import com.lumosshop.common.entity.control.ControlCenter;
import java.util.List;

public class CurrencyControlCenter extends ControlCenter {

	public CurrencyControlCenter(List<Control> controlList) {
		super(controlList);
	}


	public String getSymbol() {
		return super.getValue("CURRENCY_SYMBOL");
	}
}
