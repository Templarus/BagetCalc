/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.core;

import static com.gpn.hypmon.CoreObjects.Core.MONMARKER;
import static com.gpn.hypmon.CoreObjects.Core.loggerConsole;
import static com.gpn.hypmon.CoreObjects.Core.loggerDB;
import com.gpn.hypmon.UI.UICore;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author wa-DVKukushkin
 */
public class QueueManager implements Runnable {

    private UICore uiBase;
    private Hystorian hS = Core.getHystorian();

    public QueueManager(UICore uiBase) {
        this.uiBase = uiBase;
    }

    private void waiter() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            //do smth
            loggerConsole.warn("CoreObjects.QueueManager.waiter()");
        }

    }

    private void runAnalyzator() {

        Core.setMonitoringId(
                Core.getStoredb().setNewMon(Core.monStartTime, Core.getHystorian().getSysName(), false, "full", -1, Core.getHystorian().getHistoryForlder(), UICore.getLoggedUser()));
        //change

        Core.setAnalyzatorStat(1);
        Thread t = Core.startThread("analyzator");
        t.setName("Analyzator");
        t.start();
    }

    private void makeFilterReport() {
        //выгрузить отчёт
        {
            Thread t = Core.startThread("hystorian");
            t.setName("Hystorian");
            t.start();
            hS = Core.getHystorian();
            if (hS.hystorianReady) {
                hS.exportDataFilters();
            }
        }

    }

    private void uploadHistory() {
        // Core.startThread("hystorian").start();
        hS = Core.getHystorian();
        if (hS.hystorianReady) {
            if (hS.writeHistory()) {
            }
        }
    }

    private void readLastShapshot() {
        String lastFileName = "";
        String system = Core.testServerSelected == true ? "OHP_TEST" : "OHP_PROD";

        try {
            ResultSet rs = Core.getStoredb().getLastMon(system); //TBD

            while (rs.next()) {
                lastFileName = rs.getString(1);
            }
            rs.close();
        } catch (SQLException ex) {
            loggerDB.fatal(MONMARKER, "COre.QueueManger.readLastShapshot() lastFileName ", ex);
        }
        String testFileName = Core.getHystorian().getSysName().equals("OHP_PROD") ? Core.properties.getINTPRODCOMPVAL() : Core.properties.getINTTESTCOMPVAL();
        File selectedFile = Core.properties.isINTERNALTEST() ? new File(testFileName) : new File(lastFileName);
        // File selectedFile = new File(lastFileName);
        loggerConsole.debug("lastFileName " + lastFileName);
        loggerConsole.debug("testFileName " + testFileName);
        if (selectedFile.listFiles().length > 1 & selectedFile.getAbsolutePath().contains(Core.getHystorian().getSysName())) {
            Core.getStoredb().setCompareId(selectedFile.getAbsolutePath());
            hS = Core.getHystorian();
            if (hS.hystorianReady) {

                hS.setCompareFolder(selectedFile);
                hS.readHistory();
            }
        } else {
            loggerDB.fatal(MONMARKER, "COre.QueueManger.readLastShapshot() No files in FS ");
            System.exit(0);
        }

    }

    private void compareSnapshots() {
        hS = Core.getHystorian();
        if (hS.hystorianReady) {
            hS.makeCompare();
        }
    }

    private void exportReportToSkeib() {
        if (!Core.testServerSelected) {
            hS = Core.getHystorian();
            if (hS.hystorianReady) {
                hS.exportSkeib();
                loggerConsole.info("CoreObjects.QueueManager ПЕРЕДАЧА ДАННЫХ В СКЭИБ.. TBD");
            }
        }
    }

    public void start() {
        runAnalyzator();
        // ждём окончания работы анализатора
        while (Core.getAnCore().inUse) {
            waiter();
        }
        UICore.setProgress(0, "Выгружаю отчёт");
        makeFilterReport();
        UICore.setProgress(100, "Отчёт выгружен");
        // ждём окончания автоматического анализа
        //выгрузить историю
        UICore.setProgress(0, "Выгружаю историю");
        uploadHistory();
        UICore.setProgress(100, "История выгружена");

        //выгрузить отчёт в СЭИБ
        UICore.setProgress(0, "Выгружаю отчёт СКЭИБ");
        exportReportToSkeib();
        UICore.setProgress(100, "Отчёт в СКЭИБ выгружен");
        while (Core.getHystorian().filterExport | Core.getHystorian().writeHistory) {
            waiter();
        }

        //считать последнюю запись
        UICore.setProgress(0, "Чтение предыдущего состояния");
        readLastShapshot();
        UICore.setProgress(100, "Считано");
        //ждём окончания чтения истории
        while (Core.getHystorian().readHistory) {
            waiter();
        }

        if (!(Core.getAnCore().inUse & Core.getHystorian().readHistory & Core.getHystorian().writeHistory)) {
            UICore.setProgress(0, "Выполнение сравнения");
            compareSnapshots();
            UICore.setProgress(100, "Сравнение выполнено");

            //ждём окончания сравнения?
        }
        UICore.monStarted = false;
    }

    @Override
    public void run() {
        this.start();
    }
}
