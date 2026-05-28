package com.olympia.service.impl;

import com.olympia.entity.Pedido;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}") private String remetente;
    @Value("${olympia.admin.email}") private String adminEmail;

    public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }

    @Async
    public void notificarAdminIndisponibilidade(Pedido pedido) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(remetente, "Sistema Quarteto Olympia");
            helper.setTo(adminEmail);
            helper.setSubject("⚠️ Músicos indisponíveis — Pedido #" + pedido.getId());
            helper.setText(buildHtml(pedido), true);
            mailSender.send(msg);
            log.info("✅ Email enviado para {}", adminEmail);
        } catch (Exception e) {
            log.error("❌ Erro ao enviar email: {}", e.getMessage());
        }
    }

    private String buildHtml(Pedido p) {
        return "<div style='font-family:Arial,sans-serif;max-width:600px;'>"
            + "<h2 style='color:#5a5353;'>⚠️ Músicos indisponíveis</h2>"
            + "<p>Pedido <strong>#" + p.getId() + "</strong> — " + p.getTipoFormacao().getDescricao() + "</p>"
            + "<table style='width:100%;border-collapse:collapse;'>"
            + "<tr><td><b>Cliente</b></td><td>" + p.getNomeCliente() + "</td></tr>"
            + "<tr><td><b>Email</b></td><td>" + p.getEmailCliente() + "</td></tr>"
            + "<tr><td><b>Telefone</b></td><td>" + p.getTelefoneCliente() + "</td></tr>"
            + "<tr><td><b>Data</b></td><td>" + p.getDataEvento() + " às " + p.getHoraEvento() + "</td></tr>"
            + "<tr><td><b>Local</b></td><td>" + p.getCidadeEvento() + "/" + p.getEstadoEvento() + "</td></tr>"
            + "<tr><td><b>Total</b></td><td>R$ " + String.format("%.2f", p.getValorTotal()) + "</td></tr>"
            + "</table>"
            + "<p style='color:#c0392b;'>Por favor, verifique a disponibilidade de músicos substitutos.</p>"
            + "</div>";
    }
}
