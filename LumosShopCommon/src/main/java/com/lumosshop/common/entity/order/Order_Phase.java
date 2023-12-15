package com.lumosshop.common.entity.order;

public enum Order_Phase {
    NEW,
    REVOKED,
    IN_PROGRESS,
    PACKAGED,
    SECURED,
    IN_TRANSIT,
    RECEIVED,
    REVERTED,
    PAID,
    REFUNDED,
    CUSTOMER_REQUESTED_RETURN;

    public String getRemark() {
        return switch (this) {
            case NEW -> "The order has been initiated and is awaiting processing.";
            case REVOKED -> "The order has been canceled or revoked by the customer or the system.";
            case IN_PROGRESS -> "The order is currently in progress, undergoing various processing stages.";
            case PACKAGED -> "The order items have been prepared and packaged for shipping.";
            case SECURED -> "The order has been secured and is ready for the next phase.";
            case IN_TRANSIT -> "The order is currently in transit, being shipped to its destination.";
            case RECEIVED -> "The order has been received at its destination or by the customer.";
            case REVERTED -> "The order has been returned or rolled back to a previous state.";
            case PAID -> "The order has been paid for and is awaiting further processing.";
            case REFUNDED -> "The order has undergone a refund process, returning the payment to the customer.";
            case CUSTOMER_REQUESTED_RETURN -> "The customer has requested to return the order for specific reasons.";
            default -> "Status unknown";
        };
    }


}

