package com.dempseywood.email;

import com.dempseywood.webservice.equipmentstatus.DailyExcelReportView;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;
import org.springframework.scheduling.annotation.Async;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    public JavaMailSender emailSender;


    @Autowired
    private DailyExcelReportView report;

    private Properties props = new Properties();

    @PostConstruct
    public void init() {
       /* logger.info("initiating mail service, using the following config:");
        logger.info("MAIL_SMTP_AUTH : " + MAIL_SMTP_AUTH);
        logger.info("MAIL_SMTP_STARTTLS_ENABLE : " + MAIL_SMTP_STARTTLS_ENABLE);
        logger.info("MAIL_SMTP_HOST : " + MAIL_SMTP_HOST);
        logger.info("MAIL_SMTP_PORT : " + MAIL_SMTP_PORT);
        logger.info("GMAIL_USERNAME : " + GMAIL_USERNAME);
        logger.info("GMAIL_PASSWORD : " + GMAIL_PASSWORD);

        props.put("mail.smtp.auth", MAIL_SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", MAIL_SMTP_STARTTLS_ENABLE);

        mailSender.setHost(MAIL_SMTP_HOST);
        mailSender.setPort(MAIL_SMTP_PORT);
        mailSender.setUsername(GMAIL_USERNAME);
        mailSender.setPassword(GMAIL_PASSWORD);
        mailSender.setJavaMailProperties(props);

        velocityEngine.setProperty("spring.velocity.resource-loader-path", "classpath:/templates/");*/

    }

    public void sendMail(String from, String to, String subject, String msg) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        emailSender.send(message);
    }


   /* public String formatCustomerResponseEmail(Customer customer, List<Response> allResponses){
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("customer", customer);
        velocityContext.put("allResponses", allResponses);
        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("templates/CustomerResponse.vm", "UTF-8", velocityContext, stringWriter);
        return stringWriter.toString();
    }*/
   /* @Async
    public void sendCustomerResponseEmail(Customer customer, List<Response> allResponses){
        String emailContent = this.formatCustomerResponseEmail(customer, allResponses);
        String toAddress = notificationService.getCurrentNotificationEmail();
        logger.info("sending to " + toAddress);
        this.sendMail(GMAIL_USERNAME, toAddress, CUSTOMER_RESPONSE_EMAIL_SUBJECT, emailContent);
    }
*/

   public void send(){
       MimeMessage mimeMessage = emailSender.createMimeMessage();
       FileSystemResource file = new FileSystemResource(new File("Informe.xlsx"));
       try {
           MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
           helper.setTo("tliu861@aucklanduni.ac.nz");
           helper.setFrom("xyz@gmail.com");
           helper.setText(
                   "Hi");

           Workbook workbook = report.writeReport();
           ByteArrayOutputStream os = new ByteArrayOutputStream();


           try {
               workbook.write(os);

           }catch(IOException ex){
               ex.printStackTrace();
           }
           ByteArrayResource resource = new ByteArrayResource(os.toByteArray());

           helper.addAttachment("report.xlsx", resource);
       } catch (MessagingException e) {
           e.printStackTrace();
       }

       try {
           this.emailSender.send(mimeMessage);
       } catch (MailException ex) {
           // simply log it and go on...
           System.err.println(ex.getMessage());
       }
   }
}
