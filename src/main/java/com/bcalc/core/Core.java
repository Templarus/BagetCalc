package com.bcalc.core;

import com.bcalc.Utils.PropertyCollector;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author RusTe
 */
public class Core {

    private static UICore uiCore;
    private static GoogleData ggData;
    public static PropertyCollector ppCollector;
    private static HashMap<String, Thread> threadPool;
    public static Logger loggerConsole;
    public static DataContainer data;

    public static void main(String[] args) {
        //  Core thisCore = new Core();
        //  thisCore.run();

    }

    private void initComponents() {
        initLoggers();
        threadPool = new HashMap<>();

        ppCollector = new PropertyCollector();
        startSequence();
    }

    private void startSequence() {
        uiCore = new UICore(this);
        threadPool.put("UICore", threadRunner("UICore", uiCore));
        data = new DataContainer();
        threadPool.put("data", threadRunner("DataContainer", data));
    }

    private void initLoggers() {

        loggerConsole = LogManager.getLogger("Console");
        loggerConsole.info("CoreObjects.Core.initLog4j()  loggerConsole " + loggerConsole.getLevel().toString());

    }

    private Thread threadRunner(String name, Runnable obj) {
        Thread t = new Thread(obj);
        t.setName(name);
        t.run();
        return t;
    }

    public UICore getUiCore() {
        return uiCore;
    }

    public GoogleData getGgData() {
        return ggData;
    }

    public PropertyCollector getPpCollector() {
        return ppCollector;
    }

    public void run() {
        initComponents();

    }

}
