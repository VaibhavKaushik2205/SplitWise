package com.project.splitwise.handler.payment;

import com.project.splitwise.handler.AsyncRequestHandler;
import com.project.splitwise.listener.RequestTypes;

public interface PaymentEventHandler extends AsyncRequestHandler {

    RequestTypes getPaymentHandlerRequestType();

}
