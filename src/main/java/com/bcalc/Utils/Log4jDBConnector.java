
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
            return DriverManager.getConnection(Core.properties.getPOST_URL(), Core.properties.getPOST_USER(), Core.properties.getPOST_USERPR());
        } catch (Exception e) {
            System.err.println("Logger Сonnection failed " + e.getMessage());
        }
        return null;
    }
}
