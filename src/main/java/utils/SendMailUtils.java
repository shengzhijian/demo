package utils;

import bean.MailBean;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * 1、将发送邮件需要的信息赋值给MailBean对象（存储这些信息的方式可以写到properties文件中）
 * 2、调用sendMail方法
 */
public class SendMailUtils {
    public String toChinese(String text){
        try {
            text= MimeUtility.encodeText(new String(text.getBytes(), "GB2312"), "GB2312", "B");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 发送邮件的具体方法
     * @param mb 包含发送邮件的相关信息
     * @return
     */
    public boolean sendMail(MailBean mb){
        String host=mb.getHost();//获取SMTP主机
        final String username=mb.getUsername();// 获取发件人的用户名
        final String password=mb.getPassword();//获取发件人的密码
        String from=mb.getFrom(); //获取发件人
        String to=mb.getTo();//获取收件人
        String subject=mb.getSubject();//获取邮件主题
        String content=mb.getContent();// 邮件正文
        String fileName=mb.getFilename();// 附件的文件名
        Vector<String> file=mb.getFile();//获取多个附件
        Properties props=new Properties();
        props.put("mail.smtp.host", host);//设置smtp的主机
        props.put("mail.smtp.auth", "true");//需要验证
        Session session=Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage msg=new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = { new InternetAddress(to) };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(toChinese(subject));

            Multipart mp = new MimeMultipart();
            MimeBodyPart mbpContent = new MimeBodyPart();
            mbpContent.setText(content);
            mp.addBodyPart(mbpContent);
            if (file!=null){
                Enumeration<String> efile = file.elements();
                while (efile.hasMoreElements()){
                    MimeBodyPart mbpFile = new MimeBodyPart();
                    fileName = efile.nextElement().toString();
                    FileDataSource fds = new FileDataSource(fileName);
                    mbpFile.setDataHandler(new DataHandler(fds));
                    mbpFile.setFileName(toChinese(fds.getName()));
                    mp.addBodyPart(mbpFile);
                }
            }
            msg.setContent(mp);
            msg.setSentDate(new Date());
            Transport.send(msg);

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
