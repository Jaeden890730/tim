package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import com.mxic.oiplus.rs.TDSResource;
import com.mxic.tdsplus.util.StringUtil;
import com.mxic.tdsplus.util.TDSLogger;



import java.io.File;
import java.util.*;


public class SendMail {
    public static String server;
    public static String[] serverList;
    static {
        try {
            server = null;
            Properties properties = TDSResource.getProperties("TDS");
            server = properties.getProperty("smtp.server");
        } catch (Exception e) {
        }
        if (server == null) {
            server = "mxotsqr,mxot2sqr";
        }

        serverList = server.split(",");
    }

    public static void main(String[] args) {
        String to = "alechuang@mxic.com.tw";
        if (args.length > 0) {
            to = args[0];
        }
        String from = "sophialai@mxic.com.tw";
        String cc = "alechuang@mxic.com.tw";
        String subject = "Analysis send mail testing";
        System.out.println("Mail Send to " + to);
        String body = "<html><font color=\"blue\">Hello!!! " + to + "<br>如果你有看到這封mail，請通知我，好讓我知道send mail的功能是可以work. <br> thanks<br> CIT Jerry</font></html>";
        String filename = "xxx.xls";
        StringBuffer excel = new StringBuffer();
        excel.append("<table>										");
        excel.append("  <tr>                                                                            ");
        excel.append("    <td  colspan=\"2\">                                                  ");
        excel.append("YYYYYYYYYYYYYYYYYY");
        excel.append("    </td>                                                                         ");
        excel.append("  </tr>                                                                           ");
        excel.append("  <tr>                                                                            ");
        excel.append("    <td  colspan=\"2\">                                                  ");
        excel.append("XXXXXXXXXX Xxx");
        excel.append("    </td>                                                                         ");
        excel.append("  </tr>                                                                           ");
        excel.append("</table>      ");
        //SendMail.send(to, from, subject, body,filename,excel.toString());
        SendMail.sendCCHTML("", to, cc, from, subject, body, filename, excel.toString());
    }

    public static boolean send(String to, String from, String subject, String body) {
        Properties properties = TDSResource.getProperties("TDS");
        String mailserver_addr = properties.getProperty("mailserver_addr");
        return SendMail.send(mailserver_addr, to, from, subject, body);
    }

    public static boolean send(String to, String from, String subject, String body, String filename, String attchment) {
        Properties properties = TDSResource.getProperties("TDS");
        String mailserver_addr = properties.getProperty("mailserver_addr");
        return SendMail.send(mailserver_addr, to, from, subject, body, filename, attchment);
    }
    public static boolean sendCCHTML(String to, String cc, String from,
            String subject, String body) {
        Properties properties = TDSResource.getProperties("TDS");
        String mailserver_host = properties.getProperty("mailserver_host");
        return SendMail.sendCCHTML(mailserver_host, to, cc, from, subject, body, null, "");
    }
    public static boolean sendHtml(String to,String from, 
            String subject, String body){
        Properties properties = TDSResource.getProperties("TDS");
        String mailserver_host = properties.getProperty("mailserver_host");
        return SendMail.sendHtml(mailserver_host,to,from,subject,body);
    }
    /**
     * For send Html
     * @param smtpServer
     * @param to
     * @param from
     * @param subject
     * @param body
     * @return
     */
    public static boolean sendHtml(String smtpServer, String to, String from,
            String subject, String body){
        boolean result = false;
        try {
            Properties props = System.getProperties();
            for (int j=0; j<serverList.length; j++) {
                smtpServer = serverList[j];
                // -- Attaching to default Session, or we could start a new one --
                props.put("mail.smtp.host", smtpServer);
                Session session = Session.getDefaultInstance(props, null);
                // session.setDebug(true) ;
                // session.setDebug(false) ;
                // -- Create a new message --
                MimeMessage msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress(from));
                // msg.setRecipients(Message.RecipientType.TO,
                // InternetAddress.parse(to, false));
                String[] tos = StringUtil.parse2StringsStr(to, ",");
                //20130530-lai-start
                if ((tos == null) || (tos.length == 0)) {
                    return result;
                }
                //20130530-lai-end
                tos = convertAddress(tos);

                if (tos != null && tos.length > 1) {
                    ArrayList<InternetAddress> tmp = new ArrayList<>();
                    InternetAddress[] address;
                    for (int i = 0; i < tos.length; i++) {
                        tmp.add(new InternetAddress(tos[i]));
                    }
                    address = (InternetAddress[]) tmp.toArray(new InternetAddress[0]);
                    msg.addRecipients(Message.RecipientType.TO, address);
                }
                else {
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                }
                // -- We could include CC recipients too --
                // if (cc != null)
                // msg.setRecipients(Message.RecipientType.CC
                // ,InternetAddress.parse(cc, false));

                // -- Set the subject and body text --
                //subject = new String(subject.getBytes("Big5"), "ISO8859_1");
                //msg.setSubject(subject, "Big5");
                msg.setSubject(subject, "utf-8");

                //body = new String(body.getBytes("Big5"), "ISO8859_1");
                //msg.setText(body, "Big5");
                msg.setText(body, "utf-8");

                // -- Set some other header information --
                msg.setHeader("X-Mailer", "CITMailSender");
                msg.setSentDate(new Date());
                //msg.setContent(body,"text/html");
                msg.setContent(body,"text/html;charset=utf-8");

                // -- Send the message --
                try {
                    Transport.send(msg);
                    result = true;
                    break;
                } catch (Exception e) {
                    TDSLogger.println(e);
                    if (j == serverList.length - 1) {
                        throw e;
                    }
                }
            }	
            return result;
            // TDSLogger.println("Message["+ subject +"] send to " + to + " .");
        }
        catch (Exception ex) {
            TDSLogger.println(ex);
            reportInvalidMail(ex, subject, to, null, from);
            return false;
        }
    }
    public static boolean send(String smtpServer, String to, String from, String subject, String body) {
        boolean result = false;
        try {

            Properties props = System.getProperties();
            for (int j=0; j<serverList.length; j++) {
                smtpServer = serverList[j];

                // -- Attaching to default Session, or we could start a new one --
                props.put("mail.smtp.host", smtpServer);
                props.put("mail.smtp.sendpartial","true");
                Session session = Session.getDefaultInstance(props, null);
                // session.setDebug(true) ;
                // session.setDebug(false) ;
                // -- Create a new message --
                MimeMessage msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress(from));
                // msg.setRecipients(Message.RecipientType.TO,
                // InternetAddress.parse(to, false));
                String[] tos = StringUtil.parse2StringsStr(to,",");
                //20130530-lai-start
                if ((tos == null) || (tos.length == 0)) {
                    return result;
                }
                //20130530-lai-end
                tos = convertAddress(tos);

                if(tos!=null && tos.length>1){
                    ArrayList<InternetAddress> tmp = new ArrayList<>();
                    InternetAddress[] address;
                    for(int i=0;i<tos.length;i++){
                        tmp.add(new InternetAddress(tos[i]));
                    }
                    address = (InternetAddress[])tmp.toArray(new InternetAddress[0]);
                    msg.addRecipients(Message.RecipientType.TO, address);
                }else{
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                }
                // -- We could include CC recipients too --
                // if (cc != null)
                // msg.setRecipients(Message.RecipientType.CC
                // ,InternetAddress.parse(cc, false));

                // -- Set the subject and body text --
                msg.setSubject(subject, "Big5");

                body = new String(body.getBytes("Big5"), "ISO8859_1");

                msg.setText(body, "Big5");

                // -- Set some other header information --
                msg.setHeader("X-Mailer", "CITMailSender");
                msg.setSentDate(new Date());

                // -- Send the message --
                try {
                    Transport.send(msg);
                    result = true;
                    break;
                } catch (Exception e) {
                    TDSLogger.println(e);
                    if (j == serverList.length - 1) {
                        throw e;
                    }
                }
            }	
            return result;
            // TDSLogger.println("Message["+ subject +"] send to " + to + " .");
        } catch (Exception ex) {
            reportInvalidMail(ex, subject, to, null, from);
            return false;
        }
    }
    public static boolean sendCCHTML(String smtpServer, String to, String cc, String from, String subject, String body, String filename, String attchment) {
        boolean result = false;	
        try {

            Properties props = System.getProperties();
            for (int j=0; j<serverList.length; j++) {
                smtpServer = serverList[j];

                // -- Attaching to default Session, or we could start a new one --
                props.put("mail.smtp.host", smtpServer);
                props.put("mail.smtp.sendpartial","true");

                Session session = Session.getDefaultInstance(props, null);
                // session.setDebug(true) ;
                // session.setDebug(false) ;
                // -- Create a new message --
                MimeMessage msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress(from));
                // msg.setRecipients(Message.RecipientType.TO,
                // InternetAddress.parse(to, false));
                String[] tos = StringUtil.parse2StringsStr(to,",");
                //20130530-lai-start
                if ((tos == null) || (tos.length == 0)) {
                    return result;
                }
                //20130530-lai-end
                tos = convertAddress(tos);

                if(tos!=null && tos.length>1){
                    ArrayList<InternetAddress> tmp = new ArrayList<>();
                    InternetAddress[] address;
                    for(int i=0;i<tos.length;i++){
                        tmp.add(new InternetAddress(tos[i]));
                    }
                    address = (InternetAddress[])tmp.toArray(new InternetAddress[0]);
                    msg.addRecipients(Message.RecipientType.TO, address);
                }else{
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                }

                String[] ccs = StringUtil.parse2StringsStr(cc,",");
                ccs = convertAddress(ccs);
                if(ccs!=null && ccs.length>1){
                    ArrayList<InternetAddress> tmp = new ArrayList<>();
                    InternetAddress[] address;
                    for(int i=0;i<ccs.length;i++){
                        //tmp.add(new InternetAddress(ccs[i], false));
                        tmp.add(new InternetAddress(ccs[i]));
                    }
                    address = (InternetAddress[])tmp.toArray(new InternetAddress[0]);
                    msg.setRecipients(Message.RecipientType.CC, address);
                }else{
                    msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
                }

                //if (cc != null)
                //	msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc, false));

                // -- Set the subject and body text --
                //subject = new String(subject.getBytes("Big5"), "ISO8859_1");
                //msg.setSubject(subject, "Big5");
                msg.setSubject(subject, "utf-8");			    		

                //body = new String(body.getBytes("Big5"), "ISO8859_1");
                //msg.setText(body, "Big5");
                msg.setText(body, "utf-8");

                // -- Set some other header information --
                msg.setHeader("X-Mailer", "CITMailSender");
                msg.setSentDate(new Date());
                msg.setContent(body,"text/html;charset=utf-8");
                // -- Send the message --
                try {
                    Transport.send(msg);
                    result = true;
                    break;
                } catch (Exception e) {
                    TDSLogger.println(e);
                    if (j == serverList.length - 1) {
                        throw e;
                    }
                }
            }
            return result;
            // TDSLogger.println("Message["+ subject +"] send to " + to + " .");
        } catch (Exception ex) {
            //	    		TDSLogger.println(ex);
            reportInvalidMail(ex, subject, to, cc, from);
            return false;
        }
    } 
    public static boolean send(String smtpServer, String to, String from, String subject, String body, String filename, String attchment) {
        boolean result = false;    
        try {

            Properties props = System.getProperties();
            for (int j=0; j<serverList.length; j++) {
                smtpServer = serverList[j];

                // -- Attaching to default Session, or we could start a new one --
                props.put("mail.smtp.host", smtpServer);
                props.put("mail.smtp.sendpartial","true");
                Session session = Session.getDefaultInstance(props, null);
                // session.setDebug(true) ;
                // session.setDebug(false) ;
                // -- Create a new message --
                MimeMessage msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress(from));
                // msg.setRecipients(Message.RecipientType.TO,
                // InternetAddress.parse(to, false));
                String[] tos = StringUtil.parse2StringsStr(to,",");
                //20130530-lai-start
                if ((tos == null) || (tos.length == 0)) {
                    return result;
                }
                //20130530-lai-end
                tos = convertAddress(tos);
                if(tos!=null && tos.length>1){
                    ArrayList<InternetAddress> tmp = new ArrayList<>();
                    InternetAddress[] address;
                    for(int i=0;i<tos.length;i++){
                        tmp.add(new InternetAddress(tos[i]));
                    }
                    address = (InternetAddress[])tmp.toArray(new InternetAddress[0]);
                    msg.addRecipients(Message.RecipientType.TO, address);
                }else{
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                }
                // -- We could include CC recipients too --
                // if (cc != null)
                // msg.setRecipients(Message.RecipientType.CC
                // ,InternetAddress.parse(cc, false));

                // -- Set the subject and body text --
                msg.setSubject(subject, "Big5");


                // space (local not ok, server not ok)
                //body = new String(body.getBytes("Big5"), "ISO8859_1"); //(ori, local ok, server not ok)
                //msg.setContent( body, "text/html; charset=big5" );//(local ok, server not ok)
                //msg.setText(MimeUtility.encodeText( body) ); //(local ok, server not ok)

                //msg.setText(body, "Big5"); (ori)
                msg.setText(body, "utf-8");
                msg.setContent(body,"text/html;charset=utf-8");

                // -- Set some other header information --
                //msg.setHeader("X-Mailer", "CITMailSender");
                //msg.setSentDate(new Date());

                // Create the message part

                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText(body);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                if (attchment != null) {
                    // Part two is attachment
                    messageBodyPart = new MimeBodyPart();
                    //DataSource source = new FileDataSource("c:/analysisFile/report117.xls");
                    //messageBodyPart.setDataHandler(new DataHandler(source));

                    messageBodyPart.setDataHandler(new DataHandler(attchment,"text/plain"));
                    messageBodyPart.setFileName(filename);
                    multipart.addBodyPart(messageBodyPart);
                }

                // Put parts in message
                msg.setContent(multipart);

                // -- Send the message --
                try {
                    Transport.send(msg);
                    result = true;
                    break;
                } catch (Exception e) {
                    TDSLogger.println(e);
                    if (j == serverList.length - 1) {
                        throw e;
                    }
                }
            }
            return result;
            // TDSLogger.println("Message["+ subject +"] send to " + to + " .");
        } catch (Exception ex) {
            TDSLogger.println(ex);
            reportInvalidMail(ex, subject, to, "", from);
            return false;
        }
    }
    public static void reportInvalidMail(Exception ex, String subject, String to, String cc, String from) {
        if ((to == null || to.length() == 0) && (cc == null || cc.length() == 0)) {
            return;
        }
        StringBuffer buf = new StringBuffer();
        buf.append("Subject : " + subject + "\n");
        buf.append("From : " + to + "\n");
        buf.append("To : " + to + "\n");
        buf.append("CC : " + cc + "\n\n");

        if( ex instanceof SendFailedException ) {
            SendFailedException sfex = (SendFailedException) ex;
            Address[] invalid = sfex.getInvalidAddresses();

            if( invalid != null ) {
                buf.append( "    ** Invalid Addresses\n" );
                if( invalid != null ) {
                    for( int i = 0; i < invalid.length; i++ ) {
                        buf.append( "         " + invalid[i] + "\n");
                    }
                }
            }

            Address[] validUnsent = sfex.getValidUnsentAddresses();
            if( validUnsent != null ) {
                buf.append( "    ** ValidUnsent Addresses\n" );
                if( validUnsent != null ) {
                    for( int i = 0; i < validUnsent.length; i++ ) {
                        buf.append( "         " + validUnsent[i] + "\n");
                    }
                }
            }

            Address[] validSent = sfex.getValidSentAddresses();
            if( validSent != null ) {
                buf.append( "    ** ValidSent Addresses\n" );
                if( validSent != null ) {
                    for( int i = 0; i < validSent.length; i++ ) {
                        buf.append( "         " + validSent[i] + "\n");
                    }
                }
            }
        } else {
            ex.fillInStackTrace();
            buf.append(ex.toString());
        }

        try { // sending mail to admin
            Properties properties = TDSResource.getProperties("TDS");
            String admin = properties.getProperty("admin.mail");
            if (admin == null) return;
            if (admin.indexOf('@') == -1) {
                admin = admin + "@mxic.com.tw";
            }
            Properties props = System.getProperties();
            String smtpServer = "";

            for (int j=0; j<serverList.length; j++) {
                smtpServer = serverList[j];

                props.put("mail.smtp.host", smtpServer);
                props.put("mail.smtp.sendpartial","true");
                Session session = Session.getDefaultInstance(props, null);
                // -- Create a new message --
                MimeMessage msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress("PEIS_ADMIN@mxic.com.tw"));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(admin));
                msg.setSubject("Mail Send Fail", "Big5");

                String body = new String(buf.toString().getBytes("Big5"), "ISO8859_1");

                msg.setText(body, "Big5");

                // -- Set some other header information --
                msg.setHeader("X-Mailer", "CITMailSender");
                msg.setSentDate(new Date());

                // -- Send the message --
                try {
                    Transport.send(msg);
                    break;
                } catch (Exception e) {
                    if (j == (serverList.length - 1)) {
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            TDSLogger.println(e);
        }
        return;
    }        
    public static String[] convertAddress(String[] oldStr){
        String[] result;
        if (oldStr == null) {
            result = new String[0];
        } else {
            result = new String[oldStr.length];
            for (int i=0; i<oldStr.length; i++) {
                if (oldStr[i].indexOf('@') != -1) {
                    result[i] = oldStr[i];
                } else {
                    result[i] = oldStr[i] + "@mxic.com.tw";
                }
            }
        }

        return result;
    }  

    public static boolean send(String smtpServer, String to, String from, String cc, String subject, String body, String filename, File sourcefile) {
        boolean result = false;
        try {

            Properties props = System.getProperties();
            if (from.indexOf('@') == -1) {
                from = from.trim() + "@mxic.com.tw";
            }

            String[] tos = StringUtil.parse2StringsStr(to,",");
            if ((tos == null) || (tos.length == 0)) {
                return result;
            }
            tos = convertAddress(tos);
            for (int j=0; j<serverList.length; j++) {
                smtpServer = serverList[j];
                // -- Attaching to default Session, or we could start a new one --
                props.put("mail.smtp.host", smtpServer);
                props.put("mail.smtp.sendpartial","true");
                Session session = Session.getDefaultInstance(props, null);
                // session.setDebug(true) ;
                // session.setDebug(false) ;
                // -- Create a new message --
                MimeMessage msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress(from));
                // msg.setRecipients(Message.RecipientType.TO,
                // InternetAddress.parse(to, false));
                if(tos!=null && tos.length>1){
                    ArrayList<InternetAddress> tmp = new ArrayList<>();
                    InternetAddress[] address;
                    for(int i=0;i<tos.length;i++){
                        tmp.add(new InternetAddress(tos[i]));
                    }
                    address = (InternetAddress[])tmp.toArray(new InternetAddress[0]);
                    msg.addRecipients(Message.RecipientType.TO, address);
                }else{
                    if ((tos != null) && (tos.length == 1)) {
                        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(tos[0]));
                    }
                }
                // -- We could include CC recipients too --
                if (cc != null) {
                    if (cc.indexOf('@') != -1) {
                        msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc, false));
                    } else {
                        msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc + "@mxic.com.tw", false));
                    }
                }

                // -- Set the subject and body text --
                msg.setSubject(subject, "Big5");

                body = new String(body.getBytes("Big5"), "ISO8859_1");

                msg.setText(body, "Big5");

                // -- Set some other header information --
                //msg.setHeader("X-Mailer", "CITMailSender");
                //msg.setSentDate(new Date());

                // Create the message part

                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText(body);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                //DataSource source = new FileDataSource("c:/analysisFile/report117.xls");
                //messageBodyPart.setDataHandler(new DataHandler(source));

                ((MimeBodyPart)messageBodyPart).attachFile(sourcefile);
                //                    messageBodyPart.setHeader("Content-Type", "application/x-zip");
                messageBodyPart.setHeader("Content-Type", "application/octet-stream");
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);

                // Put parts in message
                msg.setContent(multipart);

                // -- Send the message --
                try {
                    Transport.send(msg);
                    result = false;
                    break;
                } catch (Exception e) {
                    TDSLogger.println(e);
                    if (j == serverList.length - 1) {
                        throw e;
                    }
                }
            }
            return result;
            // TDSLogger.println("Message["+ subject +"] send to " + to + " .");
        } catch (Exception ex) {
            TDSLogger.println(ex);
            reportInvalidMail(ex, subject, to, null, from);
            return false;
        }
    }

    public static boolean sendCCHTML(String smtpServer, String to, String cc, String from, String subject, String body, String filename, File sourcefile) {
        boolean result = false;	
        try {

            Properties props = System.getProperties();
            for (int j=0; j<serverList.length; j++) {
                smtpServer = serverList[j];

                // -- Attaching to default Session, or we could start a new one --
                props.put("mail.smtp.host", smtpServer);
                props.put("mail.smtp.sendpartial","true");

                Session session = Session.getDefaultInstance(props, null);
                // session.setDebug(true) ;
                // session.setDebug(false) ;
                // -- Create a new message --
                MimeMessage msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress(from));
                // msg.setRecipients(Message.RecipientType.TO,
                // InternetAddress.parse(to, false));
                String[] tos = StringUtil.parse2StringsStr(to,",");
                //20130530-lai-start
                if ((tos == null) || (tos.length == 0)) {
                    return result;
                }
                //20130530-lai-end
                tos = convertAddress(tos);

                if(tos!=null && tos.length>1){
                    ArrayList<InternetAddress> tmp = new ArrayList<>();
                    InternetAddress[] address;
                    for(int i=0;i<tos.length;i++){
                        tmp.add(new InternetAddress(tos[i]));
                    }
                    address = (InternetAddress[])tmp.toArray(new InternetAddress[0]);
                    msg.addRecipients(Message.RecipientType.TO, address);
                }else{
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                }

                String[] ccs = StringUtil.parse2StringsStr(cc,",");
                ccs = convertAddress(ccs);
                if(ccs!=null && ccs.length>1){
                    ArrayList<InternetAddress> tmp = new ArrayList<>();
                    InternetAddress[] address;
                    for(int i=0;i<ccs.length;i++){
                        //tmp.add(new InternetAddress(ccs[i], false));
                        tmp.add(new InternetAddress(ccs[i]));
                    }
                    address = (InternetAddress[])tmp.toArray(new InternetAddress[0]);
                    msg.setRecipients(Message.RecipientType.CC, address);
                    //}else{
                    //	msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
                }

                //if (cc != null)
                //	msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc, false));

                // -- Set the subject and body text --
                //subject = new String(subject.getBytes("Big5"), "ISO8859_1");
                //msg.setSubject(subject, "Big5");
                msg.setSubject(subject, "utf-8");

                //body = new String(body.getBytes("Big5"), "ISO8859_1");	    		
                //msg.setText(body, "Big5");
                //msg.setText(body, "utf-8");

                // -- Set some other header information --
                msg.setHeader("X-Mailer", "CITMailSender");
                //msg.setSentDate(new Date());
                //msg.setContent(body,"text/html");

                MimeBodyPart htmlPart = new MimeBodyPart();
                //htmlPart.setDataHandler(new DataHandler(body, "text/html; charset=Big5"));
                htmlPart.setDataHandler(new DataHandler(body, "text/html; charset=utf-8"));
                htmlPart.setContent(body, "text/html;charset=utf-8");


                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();
                // Fill the message
                messageBodyPart.setText("");
                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                //DataSource source = new FileDataSource("c:/analysisFile/report117.xls");
                //messageBodyPart.setDataHandler(new DataHandler(source));
                ((MimeBodyPart)messageBodyPart).attachFile(sourcefile);
                //                    messageBodyPart.setHeader("Content-Type", "application/x-zip");
                messageBodyPart.setHeader("Content-Type", "application/octet-stream");
                messageBodyPart.setFileName(filename);

                Multipart multipart = new MimeMultipart("related");
                multipart.addBodyPart(htmlPart);
                multipart.addBodyPart(messageBodyPart);
                // Put parts in message
                msg.setContent(multipart);  		
                // -- Send the message --
                try {
                    Transport.send(msg);
                    result = true;
                    break;
                } catch (Exception e) {
                    TDSLogger.println(e);
                    if (j == serverList.length - 1) {
                        throw e;
                    }
                }
            }
            return result;
            // TDSLogger.println("Message["+ subject +"] send to " + to + " .");
        } catch (Exception ex) {
            //	    		TDSLogger.println(ex);
            reportInvalidMail(ex, subject, to, cc, from);
            return false;
        }
    } 
}