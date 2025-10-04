package emailproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Properties;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static emailproject.EmailProject.viewResponse;
import java.awt.Toolkit;
import java.util.Arrays;
import org.jsoup.select.Elements;

public class Receive extends TimerTask {

    private final String user;
    private final String pwd;
    private final String host;
    private final Properties properties;
    private Store store;
    private Folder folder;
    private Message message;
    private int count = 1;
    private final ArrayList<Integer> arr;
    private boolean first = true;
    private int j = 0;
    private Send send;
    private Services services;
    private SSLSocket sslsocket = null;
    private InputStreamReader isr = null;
    private OutputStreamWriter osw = null;
    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;
    private PrintWriter printWriter = null;
    private SSLSocketFactory sslsocketfactory = null;
    private InetAddress inetaddress = null;
    private String response = null;
    private String[] takeNumber = null;
    private final int delay = 10;
    private final String protocol;
    private final String know;
    private final Viewer viewer;

    public Receive(String know, String user, String pwd, String host, String protocol) {
        this.user = user;
        this.pwd = pwd;
        this.host = host;
        this.properties = new Properties();
        this.arr = new ArrayList<>();
        this.protocol = protocol;
        this.know = know;
        this.viewer = new Viewer();
        if (this.know.equalsIgnoreCase("bot")) {
            this.init();
        }
    }

    // IMAP
    private void init() {
        try {
            this.properties.put("mail.store.protocol", this.protocol);
            this.store = Session.getInstance(this.properties).getStore();
            this.store.connect(this.host, this.user, this.pwd);
            this.printResponse("Подключено успешно");
            this.folder = this.store.getFolder("INBOX");
            this.folder.open(Folder.READ_WRITE);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } catch (MessagingException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    public void DisplayMessages() {
        Multipart multipart;
        BodyPart bodypart;
        String content, from, to;
        try {
            this.count = this.folder.getMessageCount();
            this.arr.add(this.count);
            if (!this.first) {
                if (this.arr.get(this.arr.size() - 1) > this.arr.get(this.arr.size() - 2)) {
                    this.message = this.folder.getMessage(this.count);
                    this.printResponse("Номер сообщения: " + this.message.getMessageNumber());
                    this.printResponse(String.valueOf(this.count));
                    this.viewer.setInfo1(String.valueOf(this.count));
                    if (this.isMultipart(this.message.getContent())) {
                        multipart = (Multipart) this.message.getContent();
                        bodypart = multipart.getBodyPart(0);
                        from = ((InternetAddress) this.message.getFrom()[0]).getAddress();
                        to = Arrays.toString(this.message.getAllRecipients());
                        content = bodypart.getContent().toString();
                        this.printResponse(to);
                        this.viewer.setInfo4(to);
                        this.printResponse(multipart.getContentType());
                        this.viewer.setInfo5(multipart.getContentType());
                        this.printResponse(this.message.getSentDate().toString());
                        this.viewer.setInfo2(this.message.getSentDate().toString());
                    } else {
                        from = ((InternetAddress) this.message.getFrom()[0]).getAddress();
                        content = (String) this.message.getContent();
                    }
                    if (from.equalsIgnoreCase(this.user)) {
                        this.printResponse("меня");
                        this.viewer.setInfo3("меня");
                    } else {
                        this.printResponse(from);
                        this.viewer.setInfo3(from);
                    }
                    this.printResponse(this.message.getSubject());
                    this.viewer.setInfo6(this.message.getSubject());
                    this.printResponse(content);
                    this.viewer.setMsgText(content);
                    this.message.setFlag(Flags.Flag.SEEN, true);
                    this.send = new Send(this.know, user, pwd, "smtp.yandex.ru", from, 465);
                    this.Bot(content);
                }
            } else {
                this.message = this.folder.getMessage(this.count);
                this.printResponse("Номер сообщения: " + this.message.getMessageNumber());
                this.printResponse(String.valueOf(this.count));
                this.viewer.setInfo1(String.valueOf(this.count));
                if (this.isMultipart(this.message.getContent())) {
                    multipart = (Multipart) this.message.getContent();
                    bodypart = multipart.getBodyPart(0);
                    from = ((InternetAddress) this.message.getFrom()[0]).getAddress();
                    to = Arrays.toString(this.message.getAllRecipients());
                    content = bodypart.getContent().toString();
                    this.printResponse(to);
                    this.viewer.setInfo4(to);
                    this.printResponse(multipart.getContentType());
                    this.viewer.setInfo5(multipart.getContentType());
                    this.printResponse(this.message.getSentDate().toString());
                    this.viewer.setInfo2(this.message.getSentDate().toString());
                } else {
                    from = ((InternetAddress) this.message.getFrom()[0]).getAddress();
                    content = (String) this.message.getContent();
                }
                if (from.equalsIgnoreCase(this.user)) {
                    this.printResponse("меня");
                    this.viewer.setInfo3("меня");
                } else {
                    this.printResponse(from);
                    this.viewer.setInfo3(from);
                }
                this.printResponse(this.message.getSubject());
                this.viewer.setInfo6(this.message.getSubject());
                this.printResponse(content);
                this.viewer.setMsgText(content);
                this.message.setFlag(Flags.Flag.SEEN, true);
                this.send = new Send(this.know, user, pwd, "smtp.yandex.ru", from, 465);
                this.Bot(content);
                this.first = false;
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    private void initSocket(String protocol) {
        int port = 0;
        try {
            if (protocol.equals("POP3")) {
                port = 995;
            } else if (protocol.equals("IMAP")) {
                port = 993;
            }
            this.sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.inetaddress = InetAddress.getByName(this.host);
            this.sslsocket = (SSLSocket) sslsocketfactory.createSocket(inetaddress, port);
            this.sslsocket.startHandshake();
            this.printResponse("Подключено успешно");
        } catch (UnknownHostException ex) {
            Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    private void closeAll() {
        try {
            this.printWriter.close();
            this.bufferedReader.close();
            this.bufferedWriter.close();
            this.isr.close();
            this.osw.close();
            this.sslsocket.close();
            this.printWriter = null;
            this.bufferedReader = null;
            this.bufferedWriter = null;
            this.isr = null;
            this.osw = null;
            this.sslsocket = null;
            this.sslsocketfactory = null;
            this.inetaddress = null;
        } catch (IOException ex) {
            Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    private void DisplayMessagesWithSockets(String protocol) {
        if (this.first) {
            this.initSocket(protocol);
            this.getMessages(protocol);
            this.first = false;
        } else {
            String stop = JOptionPane.showInputDialog(null, "Вы хотите получить сообщения снова? команда 'Yes'");
            if (stop.equalsIgnoreCase("yes")) {
                this.j = 1;
                this.closeAll();
                this.initSocket(protocol);
                this.getMessages(protocol);
            } else {
                if (protocol.equals("IMAP")) {
                    //14
                    this.sendCommand("tag13 CLOSE");
                    this.response = this.receiveResponse(this.bufferedReader);
                    this.printResponseOptional(response);
                    while (!this.response.contains("tag13 OK ") && !this.response.contains("tag13 NO ") && !this.response.contains("tag13 BAD ")) {
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        if (this.response.contains("tag13 BAD") || this.response.contains("tag13 NO")) {
                            throw new RuntimeException("Произошла ошибка при закрытии почтового ящика");
                        }
                    }
                    //15
                    this.sendCommand("tag14 LOGOUT");
                    this.response = this.receiveResponse(this.bufferedReader);
                    this.printResponseOptional(response);
                    while (!this.response.contains("tag14 OK ") && !this.response.contains("tag14 NO ") && !this.response.contains("tag14 BAD ")) {
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        if (this.response.contains("tag14 BAD") || this.response.contains("tag14 NO")) {
                            throw new RuntimeException("Произошла ошибка при выходе");
                        }
                    }
                }
                this.closeAll();
                this.cancel();
            }
        }
    }

    private void getMessages(String protocol) {
        String date = null, to = null, from = null, subject = null, body = "", temp = "", content_type = null, know, number, sender = null;
        Decoder decoder;
        try {
            this.isr = new InputStreamReader(this.sslsocket.getInputStream());
            this.osw = new OutputStreamWriter(this.sslsocket.getOutputStream());
            this.bufferedReader = new BufferedReader(this.isr);
            this.bufferedWriter = new BufferedWriter(this.osw);
            this.printWriter = new PrintWriter(this.bufferedWriter, false);
        } catch (IOException | RuntimeException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        if (protocol.equals("POP3")) {
            try {
                //1
                this.response = this.receiveResponse(this.bufferedReader);
                this.printResponseOptional(response);
                if (!this.response.startsWith("+OK")) { //-ERR
                    throw new RuntimeException("Ошибка подключения к серверу");
                }
                //2
                this.sendCommand("USER " + this.user);
                this.response = this.receiveResponse(this.bufferedReader);
                this.printResponseOptional(response);
                if (!this.response.startsWith("+OK")) { //-ERR
                    throw new RuntimeException("Ошибка с входом");
                }
                Thread.sleep(this.delay);
                //3
                this.sendCommand("PASS " + this.pwd);
                this.response = this.receiveResponse(this.bufferedReader);
                this.printResponseOptional(response);
                if (!this.response.startsWith("+OK")) { //-ERR
                    throw new RuntimeException("Ошибка при входе (не удалось войти)");
                }
                Thread.sleep(this.delay);
                //4
                this.sendCommand("STAT");
                this.response = this.receiveResponse(this.bufferedReader);
                this.printResponseOptional(response);
                this.takeNumber = this.response.split(" ");
                this.count = Integer.parseInt(this.takeNumber[1]);
                this.arr.add(this.count);
                if (this.count != 0) {
                    this.printResponse(String.valueOf(this.count));
                    this.viewer.setInfo1(String.valueOf(this.count));
                    if (!this.response.startsWith("+OK")) { //-ERR
                        throw new RuntimeException("Ошибка");
                    }
                    Thread.sleep(this.delay);
                    //5
                    number = JOptionPane.showInputDialog(null, "Введите номер сообщения, которое вы хотите получить");
                    if (Integer.parseInt(number) <= this.count) {
                        this.sendCommand("RETR " + number);
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        if (!this.response.startsWith("+OK")) { //-ERR
                            throw new RuntimeException("Ошибка");
                        }
                        while (!this.response.startsWith(".")) {
                            this.printResponseOptional(response);
                            if (this.response.contains("Date: ")) {
                                date = this.response.substring(6, this.response.length());
                            }
                            if (this.response.contains("From: ")) {
                                from = this.response.substring(6, this.response.length());
                                sender = from.substring(from.indexOf("<") + 1, from.length() - 1);
                            }
                            if (this.response.contains("To: ")) {
                                to = this.response.substring(4, this.response.length());
                            }
                            if (this.response.contains("Subject: ")) {
                                subject = this.response.substring(9, this.response.length());
                                if (subject.startsWith("=?UTF-8?B?")) {
                                    decoder = Base64.getMimeDecoder();
                                    byte[] bytes = decoder.decode(subject.substring(10, subject.length() - 2));
                                    String afterDecode = new String(bytes, "UTF-8");
                                    subject = afterDecode;
                                }
                                if (subject.isEmpty()) {
                                    subject = "(Без темы)";
                                }
                            }
                            if (this.response.contains("Content-Type: ")) {
                                content_type = this.response.substring(14, this.response.length());
                            }
                            if (!this.response.contains(":") && this.j == 61) {
                                body = this.response;
                                boolean isBase64 = org.apache.commons.codec.binary.Base64.isArrayByteBase64(body.getBytes());
                                if (isBase64) {
                                    decoder = Base64.getMimeDecoder();
                                    byte[] bytes = decoder.decode(this.response);
                                    String afterDecode = new String(bytes, "UTF-8");
                                    body = afterDecode;
                                }
                                if (body.isEmpty()) {
                                    body = "(Без текста)";
                                }
                            }
                            this.response = this.receiveResponse(bufferedReader);
                        }
                        this.printResponse(date);
                        this.viewer.setInfo2(date);
                        this.printResponse(from);
                        this.viewer.setInfo3(from);
                        this.printResponse(to);
                        this.viewer.setInfo4(to);
                        this.printResponse(content_type);
                        this.viewer.setInfo5(content_type);
                        this.printResponse(subject);
                        this.viewer.setInfo6(subject);
                        this.printResponse(body);
                        this.viewer.setMsgText(body);
                        Thread.sleep(this.delay);
                        know = JOptionPane.showInputDialog(null, "Вы хотите удалить это сообщение? команда 'Yes'");
                        if (know.equalsIgnoreCase("yes")) {
                            this.sendCommand("DELE " + String.valueOf(number));
                            this.response = this.receiveResponse(bufferedReader);
                            this.printResponseOptional(response);
                            if (!response.startsWith("+OK")) { //-ERR
                                throw new RuntimeException("Произошла ошибка при удалении сообщения");
                            }
                            this.printResponse("Сообщение успешно удалено");
                            JOptionPane.showMessageDialog(null, "Сообщение успешно удалено");
                            Thread.sleep(this.delay);
                        }
                        this.Reply(sender);
                    } else {
                        this.printResponse("Вы ввели неверный номер сообщения");
                    }
                } else {
                    this.printResponse("Входящие пусты");
                }
                this.sendCommand("QUIT");
                this.response = this.receiveResponse(bufferedReader);
                this.printResponseOptional(response);
                if (!response.startsWith("+OK")) { //-ERR
                    throw new RuntimeException("Ошибка");
                }
            } catch (RuntimeException | InterruptedException ex) {
                Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
                System.exit(0);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
        } else if (protocol.equals("IMAP")) {
            String folder;
            Document document;
            try {
                //1
                this.response = this.receiveResponse(this.bufferedReader);
                this.printResponseOptional(response);
                if (!this.response.contains("OK")) {
                    throw new RuntimeException("Ошибка подключения к серверу");
                }
                Thread.sleep(this.delay);
                //2
                this.sendCommand("tag2 LOGIN " + this.user + " " + this.pwd);
                this.response = this.receiveResponse(this.bufferedReader);
                this.printResponseOptional(response);
                while (!this.response.contains("tag2 OK ") && !this.response.contains("tag2  NO ") && !this.response.contains("tag2 BAD ")) {
                    this.response = this.receiveResponse(this.bufferedReader);
                    this.printResponseOptional(response);
                    if (this.response.contains("tag2 NO") || this.response.contains("tag2 BAD")) {
                        throw new RuntimeException("Ошибка при входе (ошибка аутентификации)");
                    }
                }
                Thread.sleep(this.delay);
                //3
                this.sendCommand("tag3 NAMESPACE");
                this.response = this.receiveResponse(this.bufferedReader);
                this.printResponse("Доступные папки: " + this.response.substring(this.response.indexOf("NAMESPACE") + 11, this.response.indexOf("NIL") - 1));
                while (!this.response.contains("tag3 OK ")) {
                    this.response = this.receiveResponse(this.bufferedReader);
                    this.printResponseOptional(response);
                }
                Thread.sleep(this.delay);
                //4
                folder = JOptionPane.showInputDialog(null, "Введите имя папки, которое вы хотите");
                this.sendCommand("tag4 SELECT \"" + folder + "\"");
                this.response = this.receiveResponse(this.bufferedReader);
                this.printResponseOptional(response);
                while (!this.response.contains("tag4 OK ") && !this.response.contains("tag4 NO ") && !this.response.contains("tag4 BAD ")) {
                    this.response = this.receiveResponse(this.bufferedReader);
                    this.printResponseOptional(response);
                    if (this.response.contains("tag4 BAD") || this.response.contains("tag4 NO")) {
                        throw new RuntimeException("Недопустимое имя папки");
                    }
                    if (this.response.contains("EXIST")) {
                        this.takeNumber = this.response.split(" ");
                        this.count = Integer.parseInt(this.takeNumber[1]);
                        this.arr.add(this.count);
                    }
                }
                this.printResponse(String.valueOf(this.count));
                this.viewer.setInfo1(String.valueOf(this.count));
                Thread.sleep(this.delay);
                if (this.count != 0) {
                    //5
                    number = JOptionPane.showInputDialog(null, "Введите номер сообщения, которое вы хотите получить");
                    if (Integer.parseInt(number) <= this.count) {
                        this.sendCommand("tag5 FETCH " + number + " (BODY[HEADER.FIELDS (Subject)])");
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        while (!this.response.contains("tag5 OK ") && !this.response.contains("tag5 NO ") && !this.response.contains("tag5 BAD ")) {
                            this.response = this.receiveResponse(this.bufferedReader);
                            this.printResponseOptional(response);
                            if (this.response.contains("tag5 BAD") || this.response.contains("tag5 NO")) {
                                throw new RuntimeException("Неверный номер сообщения или недоступен");
                            }
                            if (this.response.contains("Subject: ")) {
                                subject = this.response.substring(9, this.response.length());
                                if (subject.startsWith("=?UTF-8?B?")) {
                                    decoder = Base64.getMimeDecoder();
                                    byte[] bytes = decoder.decode(subject.substring(10, subject.length() - 2));
                                    String afterDecode = new String(bytes, "UTF-8");
                                    subject = afterDecode;
                                }
                                if (subject.isEmpty()) {
                                    subject = "(Без темы)";
                                }
                                //System.out.println(subject);
                            }
                        }
                        Thread.sleep(this.delay);
                        //6
                        this.sendCommand("tag5 FETCH " + number + " (BODY[HEADER.FIELDS (From)])");
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        while (!this.response.contains("tag5 OK ") && !this.response.contains("tag5 NO ") && !this.response.contains("tag5 BAD ")) {
                            this.response = this.receiveResponse(this.bufferedReader);
                            this.printResponseOptional(response);
                            if (this.response.contains("tag5 BAD") || this.response.contains("tag5 NO")) {
                                throw new RuntimeException("Неверный номер сообщения или недоступен");
                            }
                            if (this.response.contains("From: ")) {
                                from = this.response.substring(6, this.response.length());
                                sender = from.substring(from.indexOf("<") + 1, from.length() - 1);
                                //System.out.println(from);
                            }
                        }
                        Thread.sleep(this.delay);
                        //7
                        this.sendCommand("tag6 FETCH " + number + " (BODY[HEADER.FIELDS (To)])");
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        while (!this.response.contains("tag6 OK ") && !this.response.contains("tag6 NO ") && !this.response.contains("tag6 BAD ")) {
                            this.response = this.receiveResponse(this.bufferedReader);
                            this.printResponseOptional(response);
                            if (this.response.contains("tag6 BAD") || this.response.contains("tag6 NO")) {
                                throw new RuntimeException("Неверный номер сообщения или недоступен");
                            }
                            if (this.response.contains("To: ")) {
                                to = this.response.substring(4, this.response.length());
                                System.out.println(to);
                            }
                        }
                        Thread.sleep(this.delay);
                        //8
                        this.sendCommand("tag7 FETCH " + number + " (BODY[HEADER.FIELDS (Date)])");
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        while (!this.response.contains("tag7 OK ") && !this.response.contains("tag7 NO ") && !this.response.contains("tag7 BAD ")) {
                            this.response = this.receiveResponse(this.bufferedReader);
                            this.printResponseOptional(response);
                            if (this.response.contains("tag7 BAD") || this.response.contains("tag7 NO")) {
                                throw new RuntimeException("Неверный номер сообщения или недоступен");
                            }
                            if (this.response.contains("Date: ")) {
                                date = this.response.substring(6, this.response.length());
                                //System.out.println(date);
                            }
                        }
                        Thread.sleep(this.delay);
                        //9
                        this.sendCommand("tag8 FETCH " + number + " (BODY[HEADER.FIELDS (Content-Type)])");
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        while (!this.response.contains("tag8 OK ") && !this.response.contains("tag8 NO ") && !this.response.contains("tag8 BAD ")) {
                            this.response = this.receiveResponse(this.bufferedReader);
                            this.printResponseOptional(response);
                            if (this.response.contains("tag8 BAD") || this.response.contains("tag8 NO")) {
                                throw new RuntimeException("Неверный номер сообщения или недоступен");
                            }
                            if (this.response.contains("Content-Type: ")) {
                                content_type = this.response.substring(14, this.response.length());
                                //System.out.println(content_type);
                            }
                        }
                        Thread.sleep(this.delay);
                        //10
                        this.sendCommand("tag9 FETCH " + number + " (BODY[2])");
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        if (this.response.contains("tag9 BAD") || this.response.contains("tag9 NO")) {
                            throw new RuntimeException("Неверный номер сообщения или недоступен");
                        }
                        this.response = this.receiveResponse(this.bufferedReader);
                        if (!this.response.equalsIgnoreCase("tag9 OK FETCH Completed.")) {
                            while (!this.response.equalsIgnoreCase("tag9 OK FETCH Completed.")) {
                                temp += this.response;
                                this.response = this.receiveResponse(this.bufferedReader);
                                this.printResponseOptional(response);
                            }
                            decoder = Base64.getMimeDecoder();
                            byte[] bytes = decoder.decode(temp);
                            String afterDecode = new String(bytes, "UTF-8");
                            document = Jsoup.parse(afterDecode);
                            Elements divs = document.select("div");
                            if (divs.size() == 1) {
                                body = divs.text();
                            } else {
                                Element firstLine = document.select("div").first();
                                body = firstLine.ownText() + "\n";
                                for (int i = 1; i < divs.size(); i++) {
                                    if (i != divs.size() - 1) {
                                        body += divs.get(i).text() + "\n";
                                    } else {
                                        body += divs.get(i).text();
                                    }
                                }
                            }
                            try {
                                if (body.isEmpty()) {
                                    body = "(Без текста)";
                                }
                            } catch (NullPointerException ex) {
                                body = "(Без текста)";
                            }
                        } else {
                            document = Jsoup.parse(this.response);
                            if (document.select("div").first() != null) {
                                Element tag = document.select("div").first();
                                body = tag.text();
                            } else {
                                decoder = Base64.getMimeDecoder();
                                byte[] bytes = decoder.decode(this.response);
                                String afterDecode = new String(bytes, "UTF-8");
                                document = Jsoup.parse(afterDecode);
                                Element tag = document.select("div").first();
                                body = tag.text();
                            }
                            if (body.isEmpty()) {
                                body = "(no text)";
                            }
                        }
                        System.out.println("text: " + body);
                        Thread.sleep(this.delay);
                        //11
                        this.sendCommand("tag10 STORE " + number + " +FLAGS" + " (" + "\\" + "Seen)");
                        this.response = this.receiveResponse(this.bufferedReader);
                        this.printResponseOptional(response);
                        while (!this.response.contains("tag10 OK ") && !this.response.contains("tag10 NO ") && !this.response.contains("tag10 BAD ")) {
                            this.response = this.receiveResponse(this.bufferedReader);
                            this.printResponseOptional(response);
                            if (this.response.contains("tag10 BAD") || this.response.contains("tag10 NO")) {
                                throw new RuntimeException("Неверный номер сообщения или недоступен");
                            }
                        }
                        this.printResponse(date);
                        this.viewer.setInfo2(date);
                        this.printResponse(from);
                        this.viewer.setInfo3(from);
                        this.printResponse(to);
                        this.viewer.setInfo4(to);
                        this.printResponse(content_type);
                        this.viewer.setInfo5(content_type);
                        this.printResponse(subject);
                        this.viewer.setInfo6(subject);
                        this.printResponse(body);
                        this.viewer.setMsgText(body);
                        Thread.sleep(this.delay);
                        know = JOptionPane.showInputDialog(null, "Вы хотите удалить это сообщение? команда 'Yes'");
                        if (know.equalsIgnoreCase("yes")) {
                            //12
                            this.sendCommand("tag11 STORE " + number + " +FLAGS" + " (" + "\\" + "Deleted)");
                            this.response = this.receiveResponse(this.bufferedReader);
                            this.printResponseOptional(response);
                            while (!this.response.contains("tag11 OK ") && !this.response.contains("tag11 NO ") && !this.response.contains("tag11 BAD ")) {
                                this.response = this.receiveResponse(this.bufferedReader);
                                this.printResponseOptional(response);
                                if (this.response.contains("tag11 BAD")) {
                                    throw new RuntimeException("Неверный номер сообщения");
                                }
                            }
                            Thread.sleep(this.delay);
                            //13
                            this.sendCommand("tag12 EXPUNGE");
                            this.response = this.receiveResponse(this.bufferedReader);
                            this.printResponseOptional(response);
                            while (!this.response.contains("tag12 OK ") && !this.response.contains("tag12 NO ") && !this.response.contains("tag12 BAD ")) {
                                this.response = this.receiveResponse(this.bufferedReader);
                                this.printResponseOptional(response);
                                if (this.response.contains("tag12 BAD") || this.response.contains("tag12 NO")) {
                                    throw new RuntimeException("Произошла ошибка при удалении сообщения");
                                }
                            }
                            Thread.sleep(this.delay);
                            this.printResponse("Сообщение успешно удалено");
                            JOptionPane.showMessageDialog(null, "Сообщение успешно удалено");
                        }
                        this.Reply(sender);
                    } else {
                        this.printResponse("Вы ввели неверный номер сообщения");
                    }
                } else {
                    this.printResponse("Входящие пусты");
                }
            } catch (RuntimeException | InterruptedException ex) {
                Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
                System.exit(0);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
        }
    }

    private void sendCommand(String command) {
        this.printWriter.println(command);
        this.printWriter.flush();
    }

    private String receiveResponse(BufferedReader bufferedReader) {
        String response = "";
        try {
            this.printResponseOptional(this.j + " : ");
            response = bufferedReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            System.exit(0);
        }
        this.j++;
        return response;
    }

    private void Reply(String sender) {
        this.send = new Send(this.know, this.user, this.pwd, "smtp.yandex.ru", sender, 465);
        String subject = "Email Project";
        String body = "Протоколы электронной почты с JAVA";
        //this.send.sendMessage(subject, body);
    }

    private void printResponse(String response) {
        viewResponse.appendResponse(response);
    }

    private void printResponseOptional(String response) {
        viewResponse.appendResponse(response);
    }

    private void Bot(String content) {
        String temp = content;
        String reply = "";
        String timetable = this.chooseTimeTable(content.toLowerCase());
        Toolkit.getDefaultToolkit().beep();
        if (!content.trim().equalsIgnoreCase("черная") && !content.trim().equalsIgnoreCase("чёрная") && !content.trim().equalsIgnoreCase("красная")
                && !content.trim().equalsIgnoreCase("черная неделя") && !content.trim().equalsIgnoreCase("чёрная неделя") && !content.trim().equalsIgnoreCase("красная неделя")) {
            if (!timetable.equals("")) {
                this.services = new Services(content, timetable);
                reply += this.services.messageAnalyzing();
                this.send.sendMessage("Ответ запроса о расписании", temp + ":" + "\n" + reply);
            } else {
                this.services = new Services(content, "timetable1.json");
                reply += "Красная неделя :" + "\n" + this.services.messageAnalyzing() + "\n";
                this.services = new Services(content, "timetable2.json");
                reply += "Чёрная неделя :" + "\n" + this.services.messageAnalyzing();
                this.send.sendMessage("Ответ запроса о расписании", temp + ":" + "\n" + reply);
            }
        } else {
            if (!timetable.trim().isEmpty()) {
                this.services = new Services("понедельник", timetable);
                reply += "Понедельник :" + "\n" + this.services.messageAnalyzing();
                this.services = new Services("вторник", timetable);
                reply += "Вторник :" + "\n" + this.services.messageAnalyzing();
                this.services = new Services("среда", timetable);
                reply += "Cреда :" + "\n" + this.services.messageAnalyzing();
                this.services = new Services("четверг", timetable);
                reply += "Четверг :" + "\n" + this.services.messageAnalyzing();
                this.services = new Services("пятница", timetable);
                reply += "Пятница :" + "\n" + this.services.messageAnalyzing();
                this.send.sendMessage("Ответ запроса о расписании", temp + ":" + "\n" + reply);
            } else {
                reply += "Пожалуйста, Попробуйте Снова";
                this.send.sendMessage("Ответ запроса о расписании", temp + ":" + "\n" + reply);
            }
        }
        Services.i++;
    }

    private String chooseTimeTable(String content) {
        if (content.contains("красная")) {
            return ("timetable1.json");
        } else if (content.contains("черная") || content.contains("чёрная")) {
            return ("timetable2.json");
        }
        return "";
    }

    private boolean isNumber(String content) {
        if (content == null || content.equals("")) {
            return false;
        }
        try {
            int val = Integer.parseInt(content);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private boolean isMultipart(Object messageContent) {
        return messageContent instanceof Multipart;
    }

    @Override
    public void run() {
        if (!Viewer.flag) {
            this.cancel();
        }
        if (this.know.equals("sockets")) {
            this.DisplayMessagesWithSockets(this.protocol);
        } else if (this.know.equalsIgnoreCase("bot")) {
            this.DisplayMessages();
        }
    }
}