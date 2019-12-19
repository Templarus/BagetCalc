package com.bcalc.core;

import com.bcalc.Utils.PropertyCollector;
import java.util.HashMap;

/**
 *
 * @author RusTe
 */
public class Core implements Runnable {

    private UICore uiCore;
    private GoogleData ggData;
    private PropertyCollector ppCollector;
    private HashMap<String, Thread> threadPool;

    public static void main(String[] Args) {
        Core thisCore = new Core();
        thisCore.run();

    }

    private void initComponents() {
        threadPool = new HashMap<>();
        ppCollector = new PropertyCollector();
        uiCore = new UICore();
        threadPool.put("UICore", threadRunner("UICore", uiCore));
        ggData = new GoogleData();
        threadPool.put("GoogleData", threadRunner("GoogleData", ggData));

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

    @Override
    public void run() {
        initComponents();

    }

}
