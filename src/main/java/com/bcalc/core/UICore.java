package com.bcalc.core;

import static com.bcalc.core.Core.loggerConsole;

import com.bcalc.core.QueueManager;
import javax.swing.SwingUtilities;

/**
 * класс поддерживающий коммуникацию UI и Core
 */
public class UICore implements Runnable {

    static String loggedUser;
    public static boolean monStarted;
    static boolean loginAccepted;
    static boolean UIAnabled = true;

    Core core;
    static int currentProcc = 0;

    // содержит тайминги последнего пакета для каждого устройства
    public UICore(Core core) {
        loggerConsole.info("UI.UICore.main()");
        this.core = core;

        initObject();
        break;

    

    default:
                loggerConsole.fatal("Incorrect launchType! " + Core.properties.getLAUNCH_TYPE());
                loggerDB.fatal("Incorrect launchType! " + Core.properties.getLAUNCH_TYPE());
                System.exit(0);
                break;
        

    }

    private void nonUILaunch() {
        Core.initDBCOnnection();
        if (Core.getSdb().isReady) {
            loggerConsole.info("Login accepted!");
            if (core.msadLoginCheck(Core.properties.getLDAPUSER(), Core.properties.getLDAPPASS().toCharArray())) {
                loggerConsole.info("MSAD check passed ");
                if (core.dbLoginCheck(Core.properties.getLDAPUSER())) {
                    loggerConsole.info("DB request check passed ");
                    setLoggedUser(Core.properties.getLDAPUSER());
                    loginAccepted = true;
                    Core.initCores();
                    Core.initPGCOnnection();
                } else {
                    loggerConsole.fatal("У пользователя " + Core.properties.getLDAPUSER() + " нет разрешения доступа в СУБД");
                    loggerDB.fatal("У пользователя " + Core.properties.getLDAPUSER() + " нет разрешения доступа в СУБД");
                }

            } else {
                loggerDB.warn(MONMARKER, "LoginForm, Неверный логин или пароль");
            }
        }
        if (loginAccepted) {

            if (!monStarted) {
                monStarted = true;
                QueueManager queue = new QueueManager(this);
                Thread t = new Thread(queue);
                t.start();
            } else {
                loggerDB.warn(MONMARKER, "UI.UICore.nonUILaunch() SELECT DA BUTTON");
            }
        }
    }

    private void initObject() {
        loginForm = new LoginForm(this);
        loginForm.setBounds(300, 400, loginForm.getPreferredSize().width, loginForm.getPreferredSize().height);
        loginForm.setVisible(true);
    }

    public static void resetProgress() {
        currentProcc = 0;
        if (UIAnabled) {
            loginForm.jProgressBar1.setString("reset");
        }
    }

    public static void setProgress(int key, String text) {

        if (currentProcc < 100) {
            currentProcc += key;
        } else {
            currentProcc = key;
        }
        if (UIAnabled) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {

                    loginForm.jProgressBar1.setString("Stage " + text + "...  " + currentProcc + "%");
                    loginForm.jProgressBar1.setValue(currentProcc);

                }
            });
        }
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
