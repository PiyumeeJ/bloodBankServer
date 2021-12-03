package com.bloodbank.bloodBankServer.service.helper;

import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Component
public class HandleSms {

    public static final String ACCOUNT_SID = "ACe9a4f2343426d71d3a5fc0aecb749d21";
    public static final String AUTH_TOKEN = "c717203fa07b49c3af4cad47ff218fa1";

    public void sendSmsMessage(String phoneNumber, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message2 = Message.creator(
                new com.twilio.type.PhoneNumber(phoneNumber),
                new com.twilio.type.PhoneNumber("+13132469530"),
                message)
                .create();
        System.out.println("Message has sent : "+message2.getSid());
    }

}
