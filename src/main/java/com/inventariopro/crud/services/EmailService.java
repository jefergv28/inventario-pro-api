package com.inventariopro.crud.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jefergv28@gmail.com"); // Reemplaza con tu correo
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendInitialPasswordEmail(String toEmail, String name, String password) {
        String subject = "Bienvenido a InventarioPro - Tu cuenta ha sido creada";
        String body = "Hola " + name + ",\n\n"
                    + "Tu cuenta de empleado en InventarioPro ha sido creada con éxito.\n"
                    + "Tu contraseña inicial es: " + password + "\n\n"
                    + "Por favor, inicia sesión y cámbiala lo antes posible por seguridad.\n\n"
                    + "¡Bienvenido!";

        sendEmail(toEmail, subject, body);
    }
}