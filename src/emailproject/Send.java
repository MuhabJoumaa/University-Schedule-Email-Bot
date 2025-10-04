package emailproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import static emailproject.EmailProject.viewResponse;
import javax.swing.JOptionPane;

public class Send {

    private final String user;
    private final String pwd;
    private final String host;
    private final String to;
    private final int port;
    private final Properties properties;
    private Session session;
    private Authenticator auth;
    private final PasswordAuthentication pwd_auth;
    private Message msg;
    private Date date;
    private int i = 1;
    private final String know;
    private SSLSocketFactory sslsocketfactory;
    private InetAddress inetaddress;
    private SSLSocket sslsocket;

    public Send(String know, String user, String pwd, String host, String to, int port) {
        this.user = user;
        this.pwd = pwd;
        this.host = host;
        this.to = to;
        this.port = port;
        this.properties = new Properties();
        this.pwd_auth = new PasswordAuthentication(this.user, this.pwd);
        this.know = know;
        if (know.equalsIgnoreCase("bot")) {
            this.init();
        }
    }

    private void init() {
        this.properties.put("mail.smtp.host", this.host);
        this.properties.put("mail.smtp.ssl.enable", "true");
        this.properties.put("mail.smtp.port", this.port);
        this.properties.put("mail.smtp.auth", "true");
        this.auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return pwd_auth;
            }
        };
        this.session = Session.getDefaultInstance(properties, this.auth);
    }

    public void sendMessage(String subject, String body) {
        if (this.know.equalsIgnoreCase("bot")) {
            if (body.contains("Предмет: ----")) {
                body = body.replaceAll("Предмет: ----", "Нет занятий.");
            }
            try {
                this.date = new Date();
                this.msg = new MimeMessage(this.session);
                this.msg.setFrom(new InternetAddress(this.user));
                this.msg.setRecipient(Message.RecipientType.TO, new InternetAddress(this.to));
                this.msg.setSubject(subject);
                this.msg.setSentDate(date);
                this.msg.setText(body);
                Transport.send(msg);
                this.printResponse("Ответ успешно отправлен отправителю");
                JOptionPane.showMessageDialog(null, "Ответ успешно отправлен отправителю");
            } catch (AddressException ex) {
                Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
                System.exit(0);
            } catch (MessagingException ex) {
                Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                this.printResponse("Произошла ошибка при принятии сообщения или сообщение было отклонено");
                System.exit(0);
            }
        } else if (this.know.equalsIgnoreCase("sockets")) {
            this.sendMessageBySockets(subject, body);
        }
    }

    public void sendMessageBySockets(String subject, String body) {
        try {
            this.sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.inetaddress = InetAddress.getByName(this.host);
            this.sslsocket = (SSLSocket) sslsocketfactory.createSocket(inetaddress, this.port);
            this.sslsocket.startHandshake();
            this.printResponse("Подключено успешно");
            this.Write_Read(sslsocket, subject, body);
        } catch (UnknownHostException ex) {
            try {
                Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
                this.sslsocket.close();
            } catch (IOException ex1) {
                Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    private void Write_Read(SSLSocket sslsocket, String subject, String body) {
        String username = Base64.getEncoder().encodeToString(this.user.getBytes());
        String password = Base64.getEncoder().encodeToString(this.pwd.getBytes());
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;
        String response;
        int delay = 200;
        int code;
        try {
            isr = new InputStreamReader(sslsocket.getInputStream());
            osw = new OutputStreamWriter(sslsocket.getOutputStream(), "UTF-8");
            bufferedReader = new BufferedReader(isr);
            bufferedWriter = new BufferedWriter(osw);
            printWriter = new PrintWriter(bufferedWriter, false);
            //1
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 220) {
                throw new RuntimeException("Произошла ошибка при установлении соединения");
            }
            //2
            this.sendCommand(printWriter, "HELO yandex.ru");
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 250) {
                throw new RuntimeException("Error");
            }
            Thread.sleep(delay);
            //3
            this.sendCommand(printWriter, "AUTH LOGIN");
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 334) {
                throw new RuntimeException("Ошибка аутентификации");
            }
            Thread.sleep(delay);
            //4
            this.sendCommand(printWriter, username);
            //System.out.println(this.user);
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 334) {
                throw new RuntimeException("Ошибка аутентификации");
            }
            Thread.sleep(delay);
            //5
            this.sendCommand(printWriter, password);
            //System.out.println(this.pwd);
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 235) {
                throw new RuntimeException("Ошибка аутентификации");
            }
            Thread.sleep(delay);
            //6
            this.sendCommand(printWriter, "MAIL FROM:" + this.user);
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 250) {
                throw new RuntimeException("Ошибка");
            }
            Thread.sleep(delay);
            //7
            this.sendCommand(printWriter, "RCPT TO:" + this.to);
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 250) {
                throw new RuntimeException("Ошибка");
            }
            Thread.sleep(delay);
            //8
            this.sendCommand(printWriter, "DATA");
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 354) {
                throw new RuntimeException("Произошла ошибка (отклонение по причинам политики)");
            }
            Thread.sleep(delay);
            //9
            this.sendCommand(printWriter, "Date: " + new Date().toString());
            Thread.sleep(delay);
            //10
            this.sendCommand(printWriter, "From: " + this.user.substring(0, this.user.indexOf("@")) + "<" + this.user + ">");
            Thread.sleep(delay);
            //11
            this.sendCommand(printWriter, "To: " + this.to);
            Thread.sleep(delay);
            //12
            this.sendCommand(printWriter, "Subject: " + subject);
            Thread.sleep(delay);
            //13
            this.sendCommand(printWriter, "Content-Type: text/plain; charset=UTF-8");
            Thread.sleep(delay);
            //14
            this.sendCommand(printWriter, "");
            Thread.sleep(delay);
            //15
            this.sendCommand(printWriter, body);
            Thread.sleep(delay);
            //16
            this.sendCommand(printWriter, "");
            this.sendCommand(printWriter, ".");
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code == 250) {
                this.printResponse("Сообщение принято к передаче");
            } else {
                throw new RuntimeException("Произошла ошибка при принятии сообщения или сообщение было отклонено");
            }
            Thread.sleep(delay);
            //17
            this.sendCommand(printWriter, "QUIT");
            response = this.receiveResponse(bufferedReader);
            this.printResponseOptional(response);
            code = this.getResponseCode(response);
            this.printResponse("Code: " + code);
            if (code != 221) {
                throw new RuntimeException("Произошла ошибка при закрытии соединения");
            }
            this.printResponse("Ответ успешно отправлен отправителю");
            JOptionPane.showMessageDialog(null, "Ответ успешно отправлен отправителю");
        } catch (IOException | InterruptedException | RuntimeException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            this.printResponse(ex.getMessage());
            System.exit(0);
        } finally {
            try {
                printWriter.close();
                bufferedReader.close();
                bufferedWriter.close();
                isr.close();
                osw.close();
                sslsocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
        }
    }

    private void sendCommand(PrintWriter printWriter, String command) {
        printWriter.println(command);
        printWriter.flush();
    }

    private String receiveResponse(BufferedReader bufferedReader) {
        String response = "";
        try {
            this.printResponseOptional(this.i + " : ");
            response = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        this.i++;
        return response;
    }

    private int getResponseCode(String response) {
        int code = Integer.parseInt(response.substring(0, 3));
        return code;
    }

    private void printResponse(String response) {
        viewResponse.appendResponse(response);
    }

    private void printResponseOptional(String response) {
        System.out.println(response);
    }
}
