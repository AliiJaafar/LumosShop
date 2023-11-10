package com.lumosshop.common.entity.control;

import java.util.List;

public class MailCenter extends ControlCenter {

    public MailCenter(List<Control> controlList) {
        super(controlList);
    }

    public String retrieveTheHost() {
        return super.getValue("HOST");
    }
    public String retrieveTheUserName() {
        return super.getValue("USERNAME");
    }
    public int retrieveThePortNum() {
        return Integer.parseInt(super.getValue("PORT")) ;
    }
    public String retrieveFromMail() {
        return super.getValue("FROM");
    }
    public String retrieveThePassword() {
        return super.getValue("PASSWORD");
    }
    public String retrieveWhoTheSender() {
        return super.getValue("SENDER_CHARACTERISTIC");
    }
    public String retrieveTheSMPT_Auth() {
        return super.getValue("SMPT_AUTH");

    }
    public String retrieveTheSMPT_Secured() {
        return super.getValue("SMPT_SECURED");
    }
    public String retrieveTheVerificationTitle() {
        return super.getValue("Verification_emailTitle");
    }
    public String retrieveTheVerificationMessageContent() {
        return super.getValue("Verification_messageContent");
    }
    public String retrieveTheRecoveryPasswordTitle() {
        return super.getValue("RecoverPassword_emailTitle");
    }
    public String retrieveTheRecoveryPasswordMessageContent() {
        return super.getValue("RecoverPassword_messageContent");
    }
    public String retrieveTheOrderAcknowledgeTitle() {
        return super.getValue("ORDER_emailTitle");
    }
    public String retrieveTheOrderAcknowledgeContent() {
        return super.getValue("Order_messageContent");
    }
}
