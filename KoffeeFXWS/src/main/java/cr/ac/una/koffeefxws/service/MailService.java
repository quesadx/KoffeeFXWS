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

@Stateless
@LocalBean
public class MailService {

  private static final Logger LOG = Logger.getLogger(MailService.class.getName());

  // The mail session is configured in Payara with JNDI name: mail/RestUNASession
  @Resource(lookup = "mail/RestUNASession")
  private Session mailSession;

  // This is my personal email (Matteo here!) - please don't steal it, prof :)
  private static final String DEFAULT_FROM = "flowfxws@gmail.com";

  // ========================================================================
  // Public methods for sending emails
  // ========================================================================

  /**
   * Sends an invoice PDF to the customer via email. This runs asynchronously so it won't block the
   * main thread.
   *
   * @param recipientEmail where to send the invoice
   * @param invoiceNumber the invoice number (used in subject and filename)
   * @param pdfBytes the actual PDF file as bytes
   */
  @Asynchronous
  public void sendInvoicePDF(String recipientEmail, String invoiceNumber, byte[] pdfBytes) {
    // Basic validation - can't send without an email address
    if (recipientEmail == null || recipientEmail.isBlank()) {
      LOG.warning("Cannot send invoice email: recipient email is null or empty");
      return;
    }

    // Also need the actual PDF content
    if (pdfBytes == null || pdfBytes.length == 0) {
      LOG.warning("Cannot send invoice email: PDF bytes are null or empty");
      return;
    }

    try {
      // Build the email subject and body
      String subject = "RestUNA System - Invoice " + (invoiceNumber != null ? invoiceNumber : "");
      String htmlBody = buildSimpleInvoiceHtml(invoiceNumber);
      String attachmentName =
          "factura_" + (invoiceNumber != null ? invoiceNumber : "invoice") + ".pdf";

      // Send it off!
      sendEmailWithAttachment(recipientEmail, subject, htmlBody, pdfBytes, attachmentName);
      LOG.log(Level.INFO, "Invoice PDF email sent successfully to {0}", recipientEmail);
    } catch (Exception ex) {
      LOG.log(Level.WARNING, "Failed to send invoice PDF email to " + recipientEmail, ex);
    }
  }

  // ========================================================================
  // Core email sending logic
  // ========================================================================

  /** Does the heavy lifting of actually sending an email with a PDF attachment. */
  private void sendEmailWithAttachment(
      String toEmail,
      String subject,
      String htmlBody,
      byte[] attachmentBytes,
      String attachmentName)
      throws MessagingException {

    // Can't send email without a mail session
    if (mailSession == null) {
      LOG.warning("Mail session 'mail/RestUNASession' is not available; skipping email send.");
      return;
    }

    // Make sure SMTP is configured properly
    configureSMTP();

    LOG.log(Level.INFO, () -> "MAIL: Preparing email to " + toEmail + ", subject: " + subject);

    long startTime = System.currentTimeMillis();
    MimeMessage msg = new MimeMessage(mailSession);

    // If there's no sender configured, use our default
    if (msg.getFrom() == null || msg.getFrom().length == 0) {
      msg.setFrom(new InternetAddress(DEFAULT_FROM, false));
    }

    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
    msg.setSubject(subject, StandardCharsets.UTF_8.name());

    // Build the email in parts: HTML body + PDF attachment
    Multipart multipart = new MimeMultipart();

    // First part: the HTML email body
    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");
    multipart.addBodyPart(htmlPart);

    // Second part: the PDF file
    MimeBodyPart attachmentPart = new MimeBodyPart();
    DataSource pdfSource = new ByteArrayDataSource(attachmentBytes, "application/pdf");
    attachmentPart.setDataHandler(new DataHandler(pdfSource));
    attachmentPart.setFileName(attachmentName);
    multipart.addBodyPart(attachmentPart);

    msg.setContent(multipart);

    // Finally, send it!
    LOG.log(Level.INFO, () -> "MAIL: Sending message to " + toEmail);
    Transport.send(msg);

    long endTime = System.currentTimeMillis();
    LOG.log(
        Level.INFO,
        () -> "MAIL: Sent successfully to " + toEmail + " in " + (endTime - startTime) + " ms");
  }

  /**
   * Sets up SMTP configuration with reasonable timeouts and TLS settings. Only adds properties if
   * they're not already set.
   */
  private void configureSMTP() {
    try {
      var props = mailSession.getProperties();

      // Set timeouts (in milliseconds) - don't want to hang forever
      props.putIfAbsent("mail.smtp.connectiontimeout", "10000"); // 10 seconds to connect
      props.putIfAbsent("mail.smtp.timeout", "15000"); // 15 seconds for responses
      props.putIfAbsent("mail.smtp.writetimeout", "15000"); // 15 seconds to write

      // Security settings - we want TLS
      props.putIfAbsent("mail.smtp.auth", "true");
      props.putIfAbsent("mail.smtp.starttls.enable", "true");
      props.putIfAbsent("mail.smtp.starttls.required", "true");
      props.putIfAbsent("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");

      String host = String.valueOf(props.getProperty("mail.smtp.host"));
      String port = String.valueOf(props.getProperty("mail.smtp.port"));
      LOG.log(Level.INFO, () -> "MAIL: Configured SMTP host=" + host + ", port=" + port);

      // Warn if the host isn't configured properly
      if (host == null || host.isBlank() || "null".equalsIgnoreCase(host)) {
        LOG.warning(
            "MAIL: No SMTP host configured. Configure Payara JavaMail Session 'mail/RestUNASession'.");
      }
    } catch (Exception ex) {
      LOG.log(Level.WARNING, "Failed to configure SMTP properties", ex);
    }
  }

  // ========================================================================
  // HTML email template (full disclosure: made with ChatGPT, honesty first!)
  // ========================================================================

  /**
   * Builds a nice-looking HTML email for the invoice. It's got a clean design with a blue header
   * and professional styling.
   */
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

  /**
   * Escapes HTML special characters to prevent injection attacks. Another ChatGPT helper method -
   * keeping it real!
   */
  private String escapeHtml(String s) {
    if (s == null) return "";
    return s.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;");
  }
}
