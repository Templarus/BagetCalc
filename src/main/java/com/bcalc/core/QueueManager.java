/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.core;

import static com.bcalc.core.Core.loggerConsole;

/**
 *
 * @author wa-DVKukushkin
 */
public class QueueManager implements Runnable {

    private UICore uiBase;

    public QueueManager(UICore uiBase) {
        this.uiBase = uiBase;
    }

    private void waiter() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            //do smth
            loggerConsole.warn("CoreObjects.QueueManager.waiter()" + ex.getMessage());
        }

    }

//    private void runAnalyzator() {
//
//        Thread t = Core.("analyzator");
//        t.setName("Analyzator");
//        t.start();
//    }
//    private void makeFilterReport() {
//        //выгрузить отчёт
//        {
//            Thread t = Core.startThread("hystorian");
//            t.setName("Hystorian");
//            t.start();
//            hS = Core.getHystorian();
//            if (hS.hystorianReady) {
//                hS.exportDataFilters();
//            }
//        }
//
//    }
//
//    private void uploadHistory() {
//        // Core.startThread("hystorian").start();
//        hS = Core.getHystorian();
//        if (hS.hystorianReady) {
//            if (hS.writeHistory()) {
//            }
//        }
//    }
    public void start() {
        // Core.grabJson();
        // ждём окончания работы анализатора

        // UICore.setProgress(0, "Выгружаю отчёт");
        //  while (Core.getHystorian().filterExport | Core.getHystorian().writeHistory) {
        //      waiter();
        //  }
        //считать последнюю запись
        //    UICore.setProgress(0, "Чтение предыдущего состояния");
        //      readLastShapshot();
//        UICore.setProgress(100, "Считано");
    }

    @Override
    public void run() {
        this.start();
    }
}
