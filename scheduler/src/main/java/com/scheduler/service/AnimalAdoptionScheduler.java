package com.scheduler.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class AnimalAdoptionScheduler {

    private final JavaMailSender javaMailSender;

    AnimalAdoptionScheduler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Tool(description = "Get an auto-generated pickup date (3 days from now) for adopting a dog from a hiii5 location.")
    String scheduler(@ToolParam(description = "id of the dog") int dogId,
                     @ToolParam(description = "name of the dog") String dogName) {
        System.out.println("scheduler working");
        var i = Instant
                .now()
                .plus(3, ChronoUnit.DAYS)
                .toString();

        System.out.println("scheduling " + dogId + " / " + dogName + " for pickup on " + i);

        return i;
    }

    @Tool(description = "Send an email to the dog owner with adoption confirmation details, including pickup date and adopter info.")
    String bookingAppointment(@ToolParam(description = "id of the dog") int dogId,
                              @ToolParam(description = "name of the dog") String dogName,
                              @ToolParam(description = "date and location they will pick dog") String dateAndLocation,
                              @ToolParam(description = "user who will pick that dog") String user,
                              @ToolParam(description = "user mobile number") String mobile) {

        sendMail(dogId, dogName, dateAndLocation, user, mobile);

        return "Owner is informed. You can connect him on Workspace.abhishek.08@gmail.com ";

    }

    @Async
    private void sendMail(int dogId,
                          String dogName,
                          String dateAndLocation,
                          String user,
                          String mobile)
    {
        System.out.println("Send mail working");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("Workspace.abhishek.08@gmail.com");
            message.setSubject("Someone is coming to pick your dogs");
            message.setText("""
                        Dear Abhishek,

                        There is good news! %s is interested in buying %s on %s.
                        Contact details %s
                        Dog ID is %d.

                        Kindly connect with them.
                    """.formatted(user, dogName, dateAndLocation, mobile, dogId));

            javaMailSender.send(message);
            System.out.println("Mail sent ");
        } catch (Exception e) {
            System.err.println("Failed to send mail " + e.getMessage());
        }
    }


}
