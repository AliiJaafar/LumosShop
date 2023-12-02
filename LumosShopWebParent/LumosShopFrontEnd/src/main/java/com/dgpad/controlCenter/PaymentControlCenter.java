package com.dgpad.controlCenter;

import com.lumosshop.common.entity.control.Control;
import com.lumosshop.common.entity.control.ControlCenter;

import java.util.List;

public class PaymentControlCenter extends ControlCenter {

	public PaymentControlCenter(List<Control> controlList) {
		super(controlList);
	}


	public String getStripeAPIKey() {
		return super.getValue("STRIPE_API_KEY");
	}
	public String getStripePublicKey() {
		return super.getValue("STRIPE_PUBLIC_KEY");
	}
	public String getStripeURL() {
		return super.getValue("STRIPE_URL");
	}
}
