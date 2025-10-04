package emailproject;

import static emailproject.EmailProject.props;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import org.apache.commons.validator.routines.EmailValidator;

public class Sender extends JFrame {
    
    private EmailValidator emailValidator = EmailValidator.getInstance();
    private Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 3), BorderFactory.createEmptyBorder(5, 5, 5, 5));
    private UserInterface menu = null;
    private final Insets insets = new Insets(10, 10, 10, 10);

    public Sender() {
        initComponents();
        this.setTitle("Отправка");
        this.setIconImage((new ImageIcon(this.getClass().getClassLoader().getResource("emailproject/icon.png"))).getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setLocation(200, 100);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            for (UIManager.LookAndFeelInfo info : getInstalledLookAndFeels()) {
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
        jLabel1 = new JLabel();
        email = new JTextField();
        jLabel2 = new JLabel();
        subject = new JTextField();
        jLabel3 = new JLabel();
        content = new JTextPane();
        send = new JButton();
        back = new JButton();
        jScrollPane = new JScrollPane();
        
        Font font = jLabel1.getFont().deriveFont(15f);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Введите E-mail получателя:");
        jLabel1.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel1.setFont(font);

        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Введите тему сообщения:");
        jLabel2.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel2.setFont(font);

        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("Введите текст сообщения:");
        jLabel3.setVerticalTextPosition(SwingConstants.CENTER);
        jLabel3.setFont(font);

        send.setBackground(Color.BLACK);
        send.setForeground(Color.WHITE);
        send.setText("Отправить");
        send.addActionListener(this::sendActionPerformed);
        send.addMouseListener(new MouseAdapter() {
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
                send.setBackground(Color.WHITE);
                send.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                send.setBackground(Color.BLACK);
                send.setForeground(Color.WHITE);
            }
        });
        
        email.setBorder(border);
        email.setBounds(this.jLabel1.getBounds().x + 115, this.jLabel1.getBounds().y + 60, 210, 30);
        email.setMargin(this.insets);
        
        subject.setBorder(border);
        subject.setBounds(this.jLabel1.getBounds().x + 115, this.jLabel1.getBounds().y + 110, 210, 30);
        subject.setMargin(this.insets);
        
        content.setBorder(border);
        content.setMargin(this.insets);
                
        back.setText("Вернуться в главное меню");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setBounds(this.jLabel1.getBounds().x + 280, this.jLabel1.getBounds().y + 5, 180, 25);
        back.setActionCommand("back");
        back.addActionListener(this::backToMenu);
        back.addMouseListener(new MouseAdapter() {
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
                back.setBackground(Color.WHITE);
                back.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                back.setBackground(Color.BLACK);
                back.setForeground(Color.WHITE);
            }
        });
        
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(this.jLabel1.getBounds().x + 115, this.jLabel1.getBounds().y + 163, 210, 100);
        jScrollPane.setViewportView(content);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                    .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(108, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(send, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(send, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        this.getContentPane().setBackground(UserInterface.blue);
        this.getContentPane().add(back);
        this.getContentPane().add(email);
        this.getContentPane().add(subject);
        this.getContentPane().add(jScrollPane);
        pack();
    }     
    
    private void sendActionPerformed(ActionEvent evt) {
        String to = this.email.getText();
        if (to.isEmpty() || !emailValidator.isValid(to)) {
            this.border = BorderFactory.createLineBorder(Color.RED, 3);
            this.email.setBorder(this.border);
            JOptionPane.showMessageDialog(this, "Адрес электронной почты получателя недействителен, попробуйте еще раз.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } else {
            this.border = BorderFactory.createLineBorder(Color.BLACK, 3);
            this.email.setBorder(this.border);
            Send send = new Send("sockets", props.getProperty("mail.user"), props.getProperty("mail.password"), "smtp.yandex.ru", to, 465);
            send.sendMessage(this.subject.getText(), this.content.getText());
        }
    }
    
    private void backToMenu(ActionEvent evt) {
        this.setVisible(false);
        this.menu = new UserInterface();
        this.dispose();
    }

    private JTextPane content;
    private JTextField email;
    private JScrollPane jScrollPane;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JButton send;
    private JTextField subject;
    private JButton back;
}
