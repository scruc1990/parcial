package com.dbconnect.dbconnect.Models.DAO;

public interface IEmailServices {

    void sendEmail(String toUser, String subject, String message);
    
}
