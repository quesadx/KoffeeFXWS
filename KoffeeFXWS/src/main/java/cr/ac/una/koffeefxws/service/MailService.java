package cr.ac.una.koffeefxws.service;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.annotation.Resource;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

/**
 * Stateless service responsible for sending email notifications via a container-managed Jakarta
 * Mail Session. Email delivery failures are logged but never propagate to callers to avoid
 * impacting core business flows.
 *
 * <p>JNDI configuration: a JavaMail session must be available at {@code mail/RestUNASession}.
 */
@Stateless
@LocalBean
public class MailService {

    private static final Logger LOG = Logger.getLogger(MailService.class.getName());

    /** Container-managed mail session (configure in Payara: JNDI name mail/RestUNASession). */
    @Resource(lookup = "mail/RestUNASession")
    private Session mailSession;

    /** Default From header if the server does not set one. */
    private static final String DEFAULT_FROM = "flowfxws@gmail.com"; // Es mío, soy Matteo

    // No me robe el correo profe

    // ---------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------

    /**
     * Sends an invoice PDF to a customer via email (asynchronous).
     *
     * @param recipientEmail customer email address
     * @param invoiceNumber invoice identifier for the subject line
     * @param pdfBytes PDF file content
     */
    @Asynchronous
    public void sendInvoicePDF(String recipientEmail, String invoiceNumber, byte[] pdfBytes) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            LOG.warning("Cannot send invoice email: recipient email is null or empty");
            return;
        }
        if (pdfBytes == null || pdfBytes.length == 0) {
            LOG.warning("Cannot send invoice email: PDF bytes are null or empty");
            return;
        }

        try {
            String subject =
                    "RestUNA System - Invoice " + (invoiceNumber != null ? invoiceNumber : "");
            String htmlBody = buildSimpleInvoiceHtml(invoiceNumber);
            String attachmentName =
                    "factura_" + (invoiceNumber != null ? invoiceNumber : "invoice") + ".pdf";

            sendEmailWithAttachment(recipientEmail, subject, htmlBody, pdfBytes, attachmentName);
            LOG.log(Level.INFO, "Invoice PDF email sent successfully to {0}", recipientEmail);
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Failed to send invoice PDF email to " + recipientEmail, ex);
        }
    }

    // ---------------------------------------------------------------------
    // Low-level send with attachment
    // ---------------------------------------------------------------------

    private void sendEmailWithAttachment(
            String toEmail,
            String subject,
            String htmlBody,
            byte[] attachmentBytes,
            String attachmentName)
            throws MessagingException {
        if (mailSession == null) {
            LOG.warning(
                    "Mail session 'mail/RestUNASession' is not available; skipping email send.");
            return;
        }

        // Configure SMTP timeouts and TLS settings
        configureSMTP();

        LOG.log(Level.INFO, () -> "MAIL: Preparing email to " + toEmail + ", subject: " + subject);

        long t0 = System.currentTimeMillis();
        MimeMessage msg = new MimeMessage(mailSession);

        // Set From: if session lacks default
        if (msg.getFrom() == null || msg.getFrom().length == 0) {
            msg.setFrom(new InternetAddress(DEFAULT_FROM, false));
        }

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        msg.setSubject(subject, StandardCharsets.UTF_8.name());

        // Create multipart message
        Multipart multipart = new MimeMultipart();

        // Part 1: HTML body
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");
        multipart.addBodyPart(htmlPart);

        // Part 2: PDF attachment
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(attachmentBytes, "application/pdf");
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(attachmentName);
        multipart.addBodyPart(attachmentPart);

        msg.setContent(multipart);

        LOG.log(Level.INFO, () -> "MAIL: Sending message to " + toEmail);
        Transport.send(msg);
        long t1 = System.currentTimeMillis();
        LOG.log(
                Level.INFO,
                () -> "MAIL: Sent successfully to " + toEmail + " in " + (t1 - t0) + " ms");
    }

    private void configureSMTP() {
        try {
            var props = mailSession.getProperties();
            props.putIfAbsent("mail.smtp.connectiontimeout", "10000");
            props.putIfAbsent("mail.smtp.timeout", "15000");
            props.putIfAbsent("mail.smtp.writetimeout", "15000");
            props.putIfAbsent("mail.smtp.auth", "true");
            props.putIfAbsent("mail.smtp.starttls.enable", "true");
            props.putIfAbsent("mail.smtp.starttls.required", "true");
            props.putIfAbsent("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");

            String host = String.valueOf(props.getProperty("mail.smtp.host"));
            String port = String.valueOf(props.getProperty("mail.smtp.port"));
            LOG.log(Level.INFO, () -> "MAIL: Configured SMTP host=" + host + ", port=" + port);

            if (host == null || host.isBlank() || "null".equalsIgnoreCase(host)) {
                LOG.warning(
                        "MAIL: No SMTP host configured. Configure Payara JavaMail Session 'mail/RestUNASession'.");
            }
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Failed to configure SMTP properties", ex);
        }
    }

    // ---------------------------------------------------------------------
    // HTML template HECHO CON CHATGPT! Honestidad ante todo <3
    // ---------------------------------------------------------------------

    private String buildSimpleInvoiceHtml(String invoiceNumber) {
        return "<!DOCTYPE html>"
                + "<html lang=\"es\"><head><meta charset=\"UTF-8\"/>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>"
                + "<style>"
                + "body{margin:0;padding:24px;background:#f7f7f9;color:#252835;font-family:Arial,sans-serif;}"
                + ".container{max-width:600px;margin:0 auto;background:#ffffff;border:1px solid #e6e7eb;border-radius:12px;padding:24px;}"
                + ".header{border-bottom:2px solid #5c77ff;padding-bottom:16px;margin-bottom:16px;}"
                + ".title{margin:0;font-size:24px;color:#252835;}"
                + ".content{color:#667085;line-height:1.6;}"
                + ".footer{margin-top:24px;padding-top:16px;border-top:1px solid #e6e7eb;color:#667085;font-size:12px;}"
                + "</style></head><body>"
                + "<div class=\"container\">"
                + "<div class=\"header\"><h1 class=\"title\">RestUNA</h1></div>"
                + "<div class=\"content\">"
                + "<p>Estimado cliente,</p>"
                + "<p>Adjunto encontrará su factura <strong>"
                + escapeHtml(invoiceNumber)
                + "</strong> en formato PDF.</p>"
                + "<p>Gracias por su preferencia.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>Este es un mensaje automático. Por favor no responda a este correo.</p>"
                + "</div>"
                + "</div></body></html>";
    }

    // Otro método hecho con chatgpt para escapar HTML
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
