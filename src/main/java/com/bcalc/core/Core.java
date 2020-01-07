package com.bcalc.core;

import com.bcalc.Utils.PropertyCollector;
import java.util.HashMap;

/**
 *
 * @author RusTe
 */
public class Core {

    private UICore uiCore;
    private GoogleData ggData;
    private PropertyCollector ppCollector;
    private HashMap<String, Thread> threadPool;
    private PropertyCollector properties;

    public static void main(String[] args) {
        //  Core thisCore = new Core();
        //  thisCore.run();

    }

    private void initComponents() {
        properties = new PropertyCollector();
        threadPool = new HashMap<>();
        ppCollector = new PropertyCollector();
        uiCore = new UICore(this);
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

    public void run() {
        initComponents();

    }

}
