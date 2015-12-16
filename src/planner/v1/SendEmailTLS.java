package  planner.v1;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.LinkedList;
import java.util.Properties;

public class SendEmailTLS {
    private static String htmlText1 =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "<title>Demystifying Email Design</title>\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "</head>\n" +
            "<body style=\"margin: 0; padding: 0;\">\n" +
            "\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\t\n" +
            "\t\t<tr>\n" +
            "\t\t\t<td style=\"padding: 10px 0 30px 0;\">\n" +
            "\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border: 1px solid #cccccc; border-collapse: collapse;\">\n" +
            "\t\t\t\t\t<tr>\n" +              /* COLOR */
            "\t\t\t\t\t\t<td align=\"center\" bgcolor=\"C12424\" style=\"padding: 40px 0 30px 0; color: #153643; font-size: 28px; font-weight: bold; font-family: Arial, sans-serif;\">\n" +
            "\t\t\t\t\t\t\t<img src=\"cid:image0\" alt=\"Creating Email Magic\" width=\"300\" height=\"230\" style=\"display: block;\" />\n" +
            "\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t<td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\">\n" +
            "\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t<td style=\"color: #153643; font-family: Arial, sans-serif; font-size: 24px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<b>Thanks for using PiNavigator!</b>\n" + //Change
            "\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t<td style=\"padding: 20px 0 30px 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\n" +
            "\t\t\t\t\t\t\t\t\t\tYour plan is listed below\n" +
            "\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t<td>\n" +
            "\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n";

    private static String htmlText2 = "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t<td bgcolor=\"#ee4c50\" style=\"padding: 30px 30px 30px 30px;\">\n" +
            "\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t<td style=\"color: #ffffff; font-family: Arial, sans-serif; font-size: 14px;\" width=\"75%\">\n" +
            "\t\t\t\t\t\t\t\t\t\t&reg; PiNavigator Developer Team, Worcester 2015<br/>\n" +
            "\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t<td align=\"right\" width=\"25%\">\n" +
            "\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"font-family: Arial, sans-serif; font-size: 12px; font-weight: bold;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<a href=\"http://www.twitter.com/\" style=\"color: #ffffff;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<img src=\"images/tw.gif\" alt=\"Twitter\" width=\"38\" height=\"38\" style=\"display: block;\" border=\"0\" />\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</a>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"font-size: 0; line-height: 0;\" width=\"20\">&nbsp;</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"font-family: Arial, sans-serif; font-size: 12px; font-weight: bold;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t<a href=\"http://www.twitter.com/\" style=\"color: #ffffff;\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<img src=\"images/fb.gif\" alt=\"Facebook\" width=\"38\" height=\"38\" style=\"display: block;\" border=\"0\" />\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t</a>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t\t\t\t</tr>\n" +
            "\t\t\t\t\t\t\t</table>\n" +
            "\t\t\t\t\t\t</td>\n" +
            "\t\t\t\t\t</tr>\n" +
            "\t\t\t\t</table>\n" +
            "\t\t\t</td>\n" +
            "\t\t</tr>\n" +
            "\t</table>\n" +
            "</body>\n" +
            "</html>";

    private static String htmlBlock =
            "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"font-size: 0; line-height: 0;\" width=\"20\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t&nbsp;\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n";

    //TODO Take linked list of string instead of string
    public static void sendToEmail(LinkedList<String> msg, String destination) {
        //username and password
        final String username = "PiNavigatorcs3733@gmail.com";
        final String password = "wilsonwong";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            int numOfPages = msg.size();

            for (int i=0; i<numOfPages;i++){
                System.out.println(msg.get(i));
            }

            String htmlText = htmlText1;
            for (int i=0;i<numOfPages-1;i++){
                htmlText =
                        htmlText +
                                "\t\t\t\t\t\t\t\t\t\t\t\t<td width=\"260\" valign=\"top\">\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td>\n" +    //File 1
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<img src=\"cid:image"+(i+1)+"\" alt=\"\" width=\"100%\" height=\"400\" style=\"display: block;\" />\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding: 25px 0 0 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+msg.get(i)+"\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n"
                                //+ htmlBlock;
                                +"<tr></tr>";
            }

            htmlText = htmlText +
                    "\t\t\t\t\t\t\t\t\t\t\t\t<td width=\"260\" valign=\"top\">\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td>\n" +    //File 1
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<img src=\"cid:image"+(numOfPages)+"\" alt=\"\" width=\"100%\" height=\"400\" style=\"display: block;\" />\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding: 25px 0 0 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+msg.get(numOfPages-1)+"\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                    htmlText2;


            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("PiNavigatorcs3733@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destination));
            // Set email subject
            message.setSubject("Thanks for using PiNavigator");

            // The email will have 2 part, body and image
            MimeMultipart multipart = new MimeMultipart("related");

            // Add html text
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlText,"text/html");
            // Add text to multi
            multipart.addBodyPart(messageBodyPart);

            // Add image part
            messageBodyPart = new MimeBodyPart();

            File img = new File("src/planner/v1/h2.gif");

            DataSource fds = new FileDataSource(img);

            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image0>");
            // Add it
            multipart.addBodyPart(messageBodyPart);
            // Doing a loop for all the instructions
            for (int i=0;i<numOfPages;i++) {
                // Add image part
                messageBodyPart = new MimeBodyPart();

                img = new File("snapshot" + i + ".gif");
                //img.getPath();

                fds = new FileDataSource(img);

                messageBodyPart.setDataHandler(new DataHandler(fds));
                messageBodyPart.setHeader("Content-ID", "<image" + (i+1) + ">");
                // Add it
                multipart.addBodyPart(messageBodyPart);
                System.out.println("image added to email!");
            }
            // final merge
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //public static void main(String args[]){
    //    sendToEmail(,"yxu4@wpi.edu");
    //}
}