package emailproject;

import java.awt.Color;
import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class ViewResponse extends JFrame {

    public ViewResponse() {
        initComponents();
        this.setTitle("Консоль");
        this.setIconImage((new ImageIcon(this.getClass().getClassLoader().getResource("emailproject/icon.png"))).getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setLocation(800, 100);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        try {
            for (LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            getLogger(ViewResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new JScrollPane();
        response = new JTextArea();

        response.setBackground(Color.BLACK);
        response.setColumns(20);
        response.setForeground(Color.WHITE);
        response.setRows(5);
        response.setEditable(false);
        jScrollPane1.setViewportView(response);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                                .addContainerGap())
        );
        pack();
    }

    public void appendResponse(String response) {
        this.response.append(response + "\n");
    }

    private JScrollPane jScrollPane1;
    private JTextArea response;
}
