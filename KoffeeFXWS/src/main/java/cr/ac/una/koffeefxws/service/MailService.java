package cr.ac.una.koffeefxws.service;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.annotation.Resource;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.EJB;
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

import cr.ac.una.koffeefxws.model.CustomerDTO;
import cr.ac.una.koffeefxws.model.SystemParameterDTO;
import cr.ac.una.koffeefxws.util.Respuesta;

@Stateless
@LocalBean
public class MailService {

  private static final Logger LOG = Logger.getLogger(MailService.class.getName());

  @Resource(lookup = "mail/RestUNASession")
  private Session mailSession;

  @EJB private SystemParameterService systemParameterService;

  // This is my personal email (Matteoo) no me lo robe carri :)
  private static final String DEFAULT_FROM = "flowfxws@gmail.com";

  @Asynchronous
  public void sendInvoicePDF(CustomerDTO customer, String invoiceNumber, byte[] pdfBytes) {
    LOG.log(Level.INFO, "MAIL: sendInvoicePDF called for invoice: {0}", invoiceNumber);

    if (customer == null || customer.getEmail() == null || customer.getEmail().isBlank()) {
      LOG.warning("Cannot send invoice email: customer or customer email is null or empty");
      return;
    }

    LOG.log(Level.INFO, "MAIL: Customer email: {0}", customer.getEmail());

    if (pdfBytes == null || pdfBytes.length == 0) {
      LOG.warning("Cannot send invoice email: PDF bytes are null or empty");
      return;
    }

    LOG.log(Level.INFO, "MAIL: PDF bytes length: {0}", pdfBytes.length);

    try {
      String languageCode = "es";
      LOG.log(Level.INFO, "MAIL: Fetching language parameter from database");
      try {
        Respuesta langResponse = systemParameterService.getSystemParameterByName("display.lang");
        if (langResponse.getEstado()) {
          SystemParameterDTO langParam =
              (SystemParameterDTO) langResponse.getResultado("SystemParameter");
          String langValue = langParam.getParamValue();
          if ("en".equals(langValue) || "es".equals(langValue)) {
            languageCode = langValue;
            LOG.log(Level.INFO, "MAIL: Using language: {0}", languageCode);
          }
        } else {
          LOG.log(
              Level.WARNING,
              "MAIL: Failed to fetch language parameter: {0}",
              langResponse.getMensaje());
        }
      } catch (Exception langEx) {
        LOG.log(Level.WARNING, "Failed to fetch language parameter, using default 'es'", langEx);
      }

      LOG.log(Level.INFO, "MAIL: Building email subject and body");
      String subject = buildSubject(languageCode, invoiceNumber);
      String htmlBody = buildInvoiceHtml(languageCode, invoiceNumber, customer);
      String attachmentName =
          "factura_" + (invoiceNumber != null ? invoiceNumber : "invoice") + ".pdf";

      LOG.log(Level.INFO, "MAIL: Attempting to send email to: {0}", customer.getEmail());
      // Y se enviaaa
      sendEmailWithAttachment(customer.getEmail(), subject, htmlBody, pdfBytes, attachmentName);
      LOG.log(Level.INFO, "Invoice PDF email sent successfully to {0}", customer.getEmail());
    } catch (Exception ex) {
      LOG.log(Level.WARNING, "Failed to send invoice PDF email to " + customer.getEmail(), ex);
    }
  }

  private void sendEmailWithAttachment(
      String toEmail,
      String subject,
      String htmlBody,
      byte[] attachmentBytes,
      String attachmentName)
      throws MessagingException {

    if (mailSession == null) {
      LOG.warning("Mail session 'mail/RestUNASession' is not available; skipping email send.");
      return;
    }

    configureSMTP();

    LOG.log(Level.INFO, () -> "MAIL: Preparing email to " + toEmail + ", subject: " + subject);

    long startTime = System.currentTimeMillis();
    MimeMessage msg = new MimeMessage(mailSession);

    if (msg.getFrom() == null || msg.getFrom().length == 0) {
      msg.setFrom(new InternetAddress(DEFAULT_FROM, false));
    }

    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
    msg.setSubject(subject, StandardCharsets.UTF_8.name());

    Multipart multipart = new MimeMultipart();

    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");
    multipart.addBodyPart(htmlPart);

    MimeBodyPart attachmentPart = new MimeBodyPart();
    DataSource pdfSource = new ByteArrayDataSource(attachmentBytes, "application/pdf");
    attachmentPart.setDataHandler(new DataHandler(pdfSource));
    attachmentPart.setFileName(attachmentName);
    multipart.addBodyPart(attachmentPart);

    msg.setContent(multipart);

    LOG.log(Level.INFO, () -> "MAIL: Sending message to " + toEmail);
    Transport.send(msg);

    long endTime = System.currentTimeMillis();
    LOG.log(
        Level.INFO,
        () -> "MAIL: Sent successfully to " + toEmail + " in " + (endTime - startTime) + " ms");
  }

  private void configureSMTP() {
    try {
      var props = mailSession.getProperties();

      props.putIfAbsent("mail.smtp.connectiontimeout", "10000"); // 10 seconds to connect
      props.putIfAbsent("mail.smtp.timeout", "15000"); // 15 seconds for responses
      props.putIfAbsent("mail.smtp.writetimeout", "15000"); // 15 seconds to write

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

  // ========================================================================
  // HTML email template (full disclosure: made with ChatGPT, honesty first!)
  // ========================================================================
  private String buildSubject(String languageCode, String invoiceNumber) {
    if ("en".equals(languageCode)) {
      return "RestUNA System - Invoice " + (invoiceNumber != null ? invoiceNumber : "");
    } else {
      return "Sistema RestUNA - Factura " + (invoiceNumber != null ? invoiceNumber : "");
    }
  }

  private String buildInvoiceHtml(String languageCode, String invoiceNumber, CustomerDTO customer) {
    String lang = "en".equals(languageCode) ? "en" : "es";
    String greeting = "en".equals(languageCode) ? "Dear" : "Estimado/a";
    String customerName =
        customer.getFullName() != null && !customer.getFullName().isBlank()
            ? customer.getFullName()
            : ("en".equals(languageCode) ? "Customer" : "Cliente");
    String bodyText1 =
        "en".equals(languageCode)
            ? "Please find attached your invoice"
            : "Adjunto encontrará su factura";
    String bodyText2 = "en".equals(languageCode) ? "in PDF format." : "en formato PDF.";
    String thanksText =
        "en".equals(languageCode)
            ? "Thank you for your preference."
            : "Gracias por su preferencia.";
    String footerText =
        "en".equals(languageCode)
            ? "This is an automated message. Please do not reply to this email."
            : "Este es un mensaje automático. Por favor no responda a este correo.";
    String customerInfoLabel =
        "en".equals(languageCode) ? "Customer Information:" : "Información del Cliente:";
    String emailLabel = "en".equals(languageCode) ? "Email:" : "Correo:";
    String phoneLabel = "en".equals(languageCode) ? "Phone:" : "Teléfono:";

    StringBuilder html = new StringBuilder();
    html.append("<!DOCTYPE html>")
        .append("<html lang=\"")
        .append(lang)
        .append("\"><head><meta charset=\"UTF-8\"/>")
        .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>")
        .append("<style>")
        .append(
            "body{margin:0;padding:24px;background:#f7f7f9;color:#252835;font-family:Arial,sans-serif;}")
        .append(
            ".container{max-width:600px;margin:0 auto;background:#ffffff;border:1px solid #e6e7eb;border-radius:12px;padding:24px;}")
        .append(".header{border-bottom:2px solid #5c77ff;padding-bottom:16px;margin-bottom:16px;}")
        .append(".title{margin:0;font-size:24px;color:#252835;}")
        .append(".content{color:#667085;line-height:1.6;}")
        .append(
            ".customer-info{background:#f7f7f9;border-left:3px solid #5c77ff;padding:12px;margin:16px 0;}")
        .append(".customer-info p{margin:4px 0;}")
        .append(
            ".footer{margin-top:24px;padding-top:16px;border-top:1px solid #e6e7eb;color:#667085;font-size:12px;}")
        .append("</style></head><body>")
        .append("<div class=\"container\">")
        .append("<div class=\"header\"><h1 class=\"title\">RestUNA</h1></div>")
        .append("<div class=\"content\">")
        .append("<p>")
        .append(greeting)
        .append(" ")
        .append(escapeHtml(customerName))
        .append(",</p>")
        .append("<p>")
        .append(bodyText1)
        .append(" <strong>")
        .append(escapeHtml(invoiceNumber))
        .append("</strong> ")
        .append(bodyText2)
        .append("</p>")
        .append("<div class=\"customer-info\">")
        .append("<p><strong>")
        .append(customerInfoLabel)
        .append("</strong></p>")
        .append("<p>")
        .append(escapeHtml(customer.getFullName()))
        .append("</p>");

    if (customer.getEmail() != null && !customer.getEmail().isBlank()) {
      html.append("<p>")
          .append(emailLabel)
          .append(" ")
          .append(escapeHtml(customer.getEmail()))
          .append("</p>");
    }
    if (customer.getPhone() != null && !customer.getPhone().isBlank()) {
      html.append("<p>")
          .append(phoneLabel)
          .append(" ")
          .append(escapeHtml(customer.getPhone()))
          .append("</p>");
    }

    html.append("</div>")
        .append("<p>")
        .append(thanksText)
        .append("</p>")
        .append("</div>")
        .append("<div class=\"footer\">")
        .append("<p>")
        .append(footerText)
        .append("</p>")
        .append("</div>")
        .append("</div></body></html>");

    return html.toString();
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
