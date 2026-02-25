package com.fuelstation.managmentapi.common.infrastructure;

public interface EmailService {

    void send(String to, String subject, String body);

}
