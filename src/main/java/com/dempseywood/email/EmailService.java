package com.dempseywood.email;

import com.dempseywood.webservice.equipmentstatus.DailyExcelReportView;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    public JavaMailSender emailSender;


    @Autowired
    private DailyExcelReportView report;

    @Value("${client.time.zone}") String clientTimeZone;

    public void sendMail(String from, String to, String subject, String msg) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        emailSender.send(message);
    }

    public void send(Workbook workbook, String content, String[] recipient, Date forDate , String projectName){
        String messageString = content;
        MimeMessage mimeMessage = getMimeMessage(recipient , messageString, workbook, forDate, projectName);


        try {
            this.emailSender.send(mimeMessage);
        } catch (MailException ex) {
            logger.error(ex);
        }
    }

    private MimeMessage getMimeMessage(String[] toEmailAddress, String messageString, Workbook workbook, Date forDate, String projectName) {
        logger.info("constructing and sending email.");
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            SimpleDateFormat df = new SimpleDateFormat("EEE dd/MM/yyyy");
            df.setTimeZone(TimeZone.getTimeZone(clientTimeZone));
            helper.setSubject("Haul count for "+ projectName + " " + df.format(forDate));
            helper.setTo(toEmailAddress);
            helper.setText(
                    messageString, true);
            //Workbook workbook = report.writeReport();
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            try {
                workbook.write(os);

            }catch(IOException ex){
                ex.printStackTrace();
            }
            InputStreamSource resource = new ByteArrayResource(os.toByteArray());
            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone(clientTimeZone));
            //sdf.setTimeZone();
            StringBuilder fileNameStringBuilder = new StringBuilder();
            fileNameStringBuilder.append("Daily_Report_");
            fileNameStringBuilder.append(sdf.format(forDate));
            fileNameStringBuilder.append(".xlsx");
            helper.addAttachment(fileNameStringBuilder.toString(), resource);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mimeMessage;
    }
}
