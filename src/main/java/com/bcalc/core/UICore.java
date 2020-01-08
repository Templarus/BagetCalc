package com.bcalc.core;

import static com.bcalc.core.Core.loggerConsole;

import com.bcalc.core.QueueManager;
import com.bcalc.ui.MainApp;
import javax.swing.SwingUtilities;

/**
 * класс поддерживающий коммуникацию UI и Core
 */
public class UICore implements Runnable {

    static String loggedUser;
    static boolean loginAccepted;
    private MainApp mainApp;
    private Core core;
    static int currentProcc = 0;

    // содержит тайминги последнего пакета для каждого устройства
    public UICore(Core core) {
        loggerConsole.info("UI.UICore.main()");
        this.core = core;

        initObject();

        //System.exit(0);
        //break;
    }

    private void initObject() {
        mainApp = new MainApp(this);
        // loginForm.setBounds(300, 400, loginForm.getPreferredSize().width, loginForm.getPreferredSize().height);
        // mainApp.setVisible(true);
    }

    public static void resetProgress() {
        currentProcc = 0;

        // loginForm.jProgressBar1.setString("reset");
    }

    public static void setProgress(int key, String text) {

        if (currentProcc < 100) {
            currentProcc += key;
        } else {
            currentProcc = key;
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

//                loginForm.jProgressBar1.setString("Stage " + text + "...  " + currentProcc + "%");
                //              loginForm.jProgressBar1.setValue(currentProcc);
            }
        });

    }

    @Override
    public void run() {
        loggerConsole.info("UI.UICore.run()");
    }

    public static String getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(String loggedUser) {
        UICore.loggedUser = loggedUser;
    }
}
