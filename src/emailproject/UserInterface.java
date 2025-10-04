package emailproject;

import static emailproject.EmailProject.props;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class UserInterface extends JFrame {
    
    protected static final Color blue = new Color(0, 191, 255);
        
    public UserInterface() {
        initComponents();
        this.setTitle("Главное Меню");
        this.setIconImage((new ImageIcon(this.getClass().getClassLoader().getResource("emailproject/icon.png"))).getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setLocation(200, 100);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            for (LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if (("Nimbus").equals(info.getName())) {
                    setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        pop3 = new JButton();
        imap = new JButton();
        smtp = new JButton();
        bot = new JButton();
        title = new JLabel();

        pop3.setBackground(Color.BLACK);
        pop3.setForeground(Color.WHITE);
        pop3.setText("Просматривать полученные сообщения по протоколу POP3");
        pop3.setActionCommand("pop3");
        pop3.addActionListener(this::pop3ActionPerformed);
        pop3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                pop3.setBackground(Color.WHITE);
                pop3.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                pop3.setBackground(Color.BLACK);
                pop3.setForeground(Color.WHITE);
            }
        });

        imap.setBackground(Color.BLACK);
        imap.setForeground(Color.WHITE);
        imap.setText("Просматривать полученные сообщения по протоколу IMAP");
        imap.setActionCommand("imap");
        imap.addActionListener(this::imapActionPerformed);
        imap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                imap.setBackground(Color.WHITE);
                imap.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                imap.setBackground(Color.BLACK);
                imap.setForeground(Color.WHITE);
            }
        });

        smtp.setBackground(Color.BLACK);
        smtp.setForeground(Color.WHITE);
        smtp.setText("Передать сообщение на почту по протоколу SMTP");
        smtp.setActionCommand("smtp");
        smtp.addActionListener(this::smtpActionPerformed);
        smtp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                smtp.setBackground(Color.WHITE);
                smtp.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                smtp.setBackground(Color.BLACK);
                smtp.setForeground(Color.WHITE);
            }
        });

        bot.setBackground(Color.BLACK);
        bot.setForeground(Color.WHITE);
        bot.setText("Бот");
        bot.addActionListener(this::botActionPerformed);
        bot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                bot.setBackground(Color.WHITE);
                bot.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                bot.setBackground(Color.BLACK);
                bot.setForeground(Color.WHITE);
            }
        });

        title.setText("Жоумаа Мохамад Мухаб");
        Font font = this.title.getFont().deriveFont(15f);
        title.setFont(font);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pop3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(imap, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(smtp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(bot, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(66, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(title)
                                .addGap(170, 170, 170))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(title, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pop3, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(imap, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(smtp, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                .addComponent(bot, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        title.getAccessibleContext().setAccessibleName("title");
        this.getContentPane().setBackground(blue);
        pack();
    }

    private void pop3ActionPerformed(ActionEvent evt) {
        JButton btn = (JButton) evt.getSource();
        btn.setBackground(Color.GREEN);
        receive("sockets", props.getProperty("mail.host-pop3"), "POP3");
    }

    private void imapActionPerformed(ActionEvent evt) {
        JButton btn = (JButton) evt.getSource();
        btn.setBackground(Color.GREEN);
        receive("sockets", props.getProperty("mail.host-imap"), "IMAP");
    }

    private void smtpActionPerformed(ActionEvent evt) {
        JButton btn = (JButton) evt.getSource();
        btn.setBackground(Color.GREEN);
        send();
    }

    private void botActionPerformed(ActionEvent evt) {
        JButton btn = (JButton) evt.getSource();
        btn.setBackground(Color.GREEN);
        receive("bot", props.getProperty("mail.host-imap"), "imaps");
    }

    private void receive(String know, String host, String protocol) {
        Timer timer = new Timer();
        TimerTask timertask = new Receive(know, props.getProperty("mail.user"), props.getProperty("mail.password"), host, protocol);
        timer.schedule(timertask, 100, 500);
        this.setVisible(false);
        this.dispose();
    }

    private void send() {
        Sender sender = new Sender();
        this.setVisible(false);
        this.dispose();
    }

    private JButton pop3;
    private JButton imap;
    private JButton smtp;
    private JButton bot;
    private JLabel title;
}