package emailproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class EmailProject {

    static FileInputStream fis;
    static Properties props;
    static UserInterface gui;
    static ViewResponse viewResponse;

    public static void main(String[] args) {
        try {
            File file = new File("props.properties");
            fis = new FileInputStream(file.getAbsolutePath());
            props = new Properties();
            props.load(fis);
            gui = new UserInterface();
            viewResponse = new ViewResponse();
        } catch (IOException ex) {
            Logger.getLogger(EmailProject.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}