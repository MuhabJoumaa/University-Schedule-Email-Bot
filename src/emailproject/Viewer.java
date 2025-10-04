package emailproject;

import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class Viewer extends JFrame {
    
    private UserInterface menu = null;
    public static boolean flag;

    public Viewer() {
        initComponents();
        this.setTitle("Информация");
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
        Viewer.flag = true;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        numberOfmsg = new JLabel();
        msgDate = new JLabel();
        msgFrom = new JLabel();
        msgTo = new JLabel();
        msgContentType = new JLabel();
        msgSubject = new JLabel();
        info1 = new JLabel();
        info2 = new JLabel();
        info3 = new JLabel();
        info4 = new JLabel();
        info5 = new JLabel();
        info6 = new JLabel();
        jScrollPane1 = new JScrollPane();
        msgText = new JTextArea();
        back = new JButton();

        numberOfmsg.setText("Количество полученных сообщений: ");

        msgDate.setText("Дата сообщения: ");

        msgFrom.setText("Сообщение от: ");

        msgTo.setText("Сообщение кому: ");

        msgContentType.setText("Тип содержимого сообщения: ");

        msgSubject.setText("Тема сообщения: ");

        msgText.setColumns(20);
        msgText.setRows(5);
        msgText.setEditable(false);
        msgText.setBackground(new Color(213, 10, 220));
        msgText.setForeground(Color.BLACK);
        msgText.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 5), "Текст"));
        jScrollPane1.setViewportView(msgText);
        
        info1.setBackground(Color.BLACK);
        info1.setForeground(Color.WHITE);
        
        info2.setBackground(Color.BLACK);
        info2.setForeground(Color.WHITE);
        
        info3.setBackground(Color.BLACK);
        info3.setForeground(Color.WHITE);
        
        info4.setBackground(Color.BLACK);
        info4.setForeground(Color.WHITE);
        
        info5.setBackground(Color.BLACK);
        info5.setForeground(Color.WHITE);
        
        info6.setBackground(Color.BLACK);
        info6.setForeground(Color.WHITE);
        
        back.setText("Вернуться в главное меню");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setBounds(this.info1.getBounds().x + 330, this.info1.getBounds().y + 5, 200, 30);
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

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(numberOfmsg)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(info1, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(msgFrom)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(info3, GroupLayout.PREFERRED_SIZE, 339, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(msgTo, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(msgDate, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(info2, GroupLayout.PREFERRED_SIZE, 331, GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(info4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(4, 4, 4))))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(msgSubject)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(info6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(msgContentType)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(info5, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(numberOfmsg, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                    .addComponent(info1, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(msgDate, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(info2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(msgFrom, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(info3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(msgTo, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(info4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(msgContentType, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(info5, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(msgSubject, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(info6, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        this.getContentPane().setBackground(UserInterface.blue);
        this.getContentPane().add(back);
        pack();
    }
    
    public void setInfo1(String s) {
        this.info1.setText(s);
    }
    
    public void setInfo2(String s) {
        this.info2.setText(s);
    }
    
    public void setInfo3(String s) {
        this.info3.setText(s);
    }
    
    public void setInfo4(String s) {
        this.info4.setText(s);
    }
    
    public void setInfo5(String s) {
        this.info5.setText(s);
    }
    
    public void setInfo6(String s) {
        if (s.trim().isEmpty()) {
            s = "(Без темы)";
        }
        this.info6.setText(s);
    }
    
    public void setMsgText(String s) {
        if (s.trim().isEmpty()) {
            s = "(Без текста)";
        }
        this.msgText.append("Запрос номер " + Services.i + ":" + "\n" + s + "\n");
    }
    
    private void backToMenu(ActionEvent evt) {
        Services.i = 1;
        this.flag = false;
        this.setVisible(false);
        this.menu = new UserInterface();
        this.dispose();
    }
    
    private JLabel info1;
    private JLabel info2;
    private JLabel info3;
    private JLabel info4;
    private JLabel info5;
    private JLabel info6;
    private JScrollPane jScrollPane1;
    private JLabel msgContentType;
    private JLabel msgDate;
    private JLabel msgFrom;
    private JLabel msgSubject;
    protected JTextArea msgText;
    private JLabel msgTo;
    private JLabel numberOfmsg;
    private JButton back;
}