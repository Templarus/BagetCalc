package com.bcalc.Utils;

import com.bcalc.core.Core;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author kukushkin.dv
 */
public class Log4jDBConnector {

    private Log4jDBConnector() {
    }

    public static Connection openConnectionToDB() {
        try {
            Class.forName("org.postgresql.Driver");
            //  return DriverManager.getConnection(Core.ppCollector.getPOST_URL(), Core.ppCollector.getPOST_USER(), Core.ppCollector.getPOST_USERPR());
        } catch (Exception e) {
            System.err.println("Logger Сonnection failed " + e.getMessage());
        }
        return null;
    }
}
