
package gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author 'Kasun Chamara'
 */
public class SystemTrayUtil {

    private static SystemTrayUtil instance;

    public static SystemTrayUtil getInstance() {
        if (instance == null) {
            instance = new SystemTrayUtil();
        }
        return instance;
    }

    public static void initSystemTray(SystemIntegrationSyncGUI systemIntegrationSyncGUI) {
        final PopupMenu popup = new PopupMenu();
        TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("images/cash.png"));
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");

        MenuItem loggerItem = new MenuItem("Logger");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(loggerItem);
        popup.addSeparator();
        popup.add(exitItem); 
        
        trayIcon.setPopupMenu(popup);

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "ACCOUNT INTERATOR SOFTWARE \n"
                        + "software by supervision technology (PVT) ltd");
            }
        });
       
        loggerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!systemIntegrationSyncGUI.isVisible()) {
                    systemIntegrationSyncGUI.setVisible(true);
                    trayIcon.displayMessage("Account Integration System", "logger viewed.. ", TrayIcon.MessageType.INFO);
                } else {
                    trayIcon.displayMessage("Account Integration System", "logger already exists.. ", TrayIcon.MessageType.WARNING);
                }

            }
        });
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    System.exit(0);
//                if (systemIntegrationSyncGUI.isActive()) {
//                    systemIntegrationSyncGUI.dispose();
//                    trayIcon.displayMessage("Account Integration System", "logger closed.. ", TrayIcon.MessageType.INFO);
//                }else{
//                    trayIcon.displayMessage("Account Integration System", "logger already closed.. ", TrayIcon.MessageType.INFO);
//                }

            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    //Obtain the image URL
    protected static Image createImage(String path) {
        URL imageURL = SystemTrayUtil.class.getResource(path);
        if (imageURL == null) {
            return null;
        } else {
            return (new ImageIcon(imageURL)).getImage();
        }
    }
}
