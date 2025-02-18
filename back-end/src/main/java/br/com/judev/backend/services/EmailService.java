package br.com.judev.backend.services;

import br.com.judev.backend.dto.UserRequestDTO;
import br.com.judev.backend.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("spring.mail.username")
    private String fromEmail;


    public void sendOrderConfirmation( Order order){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(order.getUser().getEmail());
        message.setSubject("Order confirmation");
        message.setText("Your order has been confirmed. Order ID " + order.getId());
        mailSender.send(message);
    }

    public void sendConfirmationCode(String email, String confirmationCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);  // Passando o e-mail diretamente
        message.setSubject("Confirm your email");
        message.setText("Please confirm your email by entering this code " + confirmationCode);
        mailSender.send(message);
    }

}
