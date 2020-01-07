package com.bcalc.Utils;

import com.gpn.hypmon.Comprator.CompareRow;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import com.gpn.hypmon.CoreObjects.Core;
import static com.gpn.hypmon.CoreObjects.Core.MONMARKER;
import static com.gpn.hypmon.CoreObjects.Core.loggerConsole;
import static com.gpn.hypmon.CoreObjects.Core.loggerDB;

/**
 * Класс выполняет соединенение с PostgreSQL для выгрузки отчётов и аналитик.
 *
 * @author wa-DVKukushkin
 */
public class ServerDB {

    protected Connection conn;
    private Statement st;
    private String sql;
    private ResultSet rs;

    private String url;

    public ResultSet rsClientFind;
    private final String dbUser;
    private final String password;
    public boolean isReady = false;
    String prefix;

    public Connection getConn() {
        loggerConsole.info("DB.ServerDb.getConn()");
        return conn;
    }

    /**
     * Получаем коннект с базой
     *
     * conn
     */
    public void setConn(Connection conn) {
        loggerConsole.info("DB.ServerDb.setConn()");
        this.conn = conn;
    }

    /**
     * Конструктор - инициализация подключения к СУБД
     */
    public StoreDB() {
        loggerConsole.info("DB.ServerDb.<init>()");

        this.url = Core.properties.getPOST_URL();
        this.dbUser = Core.properties.getPOST_USER();
        this.password = Core.properties.getPOST_USERPR();
        if (connect(url, dbUser, password) != DbConstants.READY) {
            JOptionPane.showMessageDialog(null, "Не удается устрановить соединение с базой", "Внимание", 0);

        } else {
            isReady = true;
        }
        loggerConsole.debug("StoreDB(): constructor");
    }

    public StoreDB(String url, String user, String password) {
        loggerConsole.debug("DB.ServerDb.<init>(3)");
        this.url = url;
        this.dbUser = user;
        this.password = password;
        if (connect() != DbConstants.READY) {
            JOptionPane.showMessageDialog(null, "Не удается устрановить соединение с postgres", "Внимание", 0);
        }
        loggerConsole.debug("SERVERDb: constructor");
    }

    /**
     * Метод создающий коннект с базой
     *
     * url user password
     */
    private int connect(String url, String user, String password) {
        loggerConsole.info("StoreDB connect method");
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);//Подключаемся к баде данных
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//Делаем многонаправленный ResultSet
            loggerConsole.info("SUCCESSFULLY connected with database!");
        } catch (SQLException | ClassNotFoundException ex) {
            loggerDB.error(MONMARKER, "StoreDB:getData():Ошибка подключения PSQL или создание Statement - ", ex);
            return DbConstants.ERROR;
        }
        prefix = Core.testServerSelected ? "TEST" : "PROD";
        return DbConstants.READY;
    }

    /**
     * Основной метод, получающий на вход сформированный SQL и возвращающий
     * ResultSet из СУБД
     *
     * sql
     *
     */
    public ResultSet selectDb(String sql) {
        loggerConsole.trace("DB.ServerDb.selectDb()");
        try {
            st = conn.createStatement();
            rs = st.executeQuery(sql);
        } catch (SQLException ex) {
            loggerDB.error(MONMARKER, "ServerDb:selectDb():Ошибка подключения или создание Statement sql: " + sql, ex);
        }
        return rs;
    }

    public int insertDb(String sql) {
        try {
            st = conn.createStatement();
            return st.executeUpdate(sql);
        } catch (SQLException ex) {
            loggerDB.error(MONMARKER, "ServerDb:insertDb():Ошибка подключения или создание Statement sql: " + sql, ex);
        }
        return -1;
    }

    public int updateDb(String sql) {
        try {
            st = conn.createStatement();
            return st.executeUpdate(sql);
        } catch (SQLException ex) {
            loggerDB.error(MONMARKER, "ServerDb:updateDb():Ошибка подключения или создание Statement sql: " + sql, ex);
        }
        return -1;
    }

    private int connect() {
        loggerConsole.info("DB.ServerDb.connect()");
        if (this.connect(this.url, this.dbUser, this.password) == DbConstants.READY) {
            return DbConstants.READY;
        }
        return DbConstants.ERROR;
    }

    public int setNewMon(String currenttime, String sysName, boolean monStatus, String monType, int compareId, String monFolder, String user) {
        String sql = "insert into monitoringlog\n"
                + "values('" + currenttime + "',"
                + "'" + sysName + "',"
                + monStatus + ","
                + "'" + monType + "',"
                + "'" + compareId + "',"
                + "'" + monFolder + "',"
                + "'" + user + "')";
        insertDb(sql);
        sql = "select id \n"
                + "from monitoringlog mon\n"
                + "where 1=1\n"
                + "and mon.\"systemName\"='" + sysName + "'\n"
                + "and mon.\"monStatus\"=" + monStatus + "\n"
                + "and mon.folder='" + monFolder + "'\n"
                + "and mon.date='" + currenttime + "';";
        ResultSet rs = selectDb(sql);
        int monitoringID = -1;
        try {
            while (rs.next()) {
                monitoringID = rs.getInt(1);
            }
        } catch (SQLException ex) {
            loggerDB.error(MONMARKER, "DB.StoreDB.setnewMon()", ex);
        }
        return monitoringID;

    }

    public ResultSet getLastMon(String sys) {
        String sql = "select folder\n"
                + "from monitoringlog monlog\n"
                + "where 1=1\n"
                + "and monlog.compareid != '-1'\n"
                + "and monlog.\"systemName\"='" + sys + "'\n"
                + "and monlog.\"monStatus\"=true\n"
                + "Order by date Desc\n"
                + "limit 1";
        return selectDb(sql);
    }

    public ResultSet getfilterIds() {
        String sql = "select filtercode from filtertypes;";
        return selectDb(sql);
    }

    public int putCompareResults(CompareRow cr) {
        String sql = "insert  into monbasetable\n"
                + "values ("
                + Core.getMonitoringId() + ","
                + "'" + cr.getCompareRes() + "',"
                + "'" + 0 + "',"
                + "'" + cr.getParentType() + "',"
                + "'" + cr.getParentName() + "',"
                + "'" + cr.getObjectData() + "',"
                + cr.getResult() + ")";
        return insertDb(sql);

    }

    public int setCompareId(String folder) {
        int compId = -1;

        String sql = "select id from monitoringlog\n"
                + "where folder='" + folder + "\\'";
        ResultSet rs = selectDb(sql);
        try {
            while (rs.next()) {
                compId = rs.getInt(1);
            }
        } catch (SQLException ex) {
            loggerDB.error(MONMARKER, "DB.StoreDB.setCompareId()", ex);
        }
        sql = "update monitoringlog\n"
                + "set compareid=" + compId + "\n"
                + "where id='" + Core.getMonitoringId() + "' ";
        return updateDb(sql);
    }

    public int putFilterResult(String row) {
        String[] values = row.split("#");
        String sql = "insert into checkstory\n"
                + "values ("
                + Core.getMonitoringId() + ","
                + values[1] + ","
                + "'" + values[2] + "',"
                + "'" + values[3] + "');";
        return insertDb(sql);
    }

    public int setMonStatus() {
        sql = "update monitoringlog\n"
                + "set \"monStatus\"=" + true + "\n"
                + "where id=" + Core.getMonitoringId() + ";";
        return updateDb(sql);
    }

    public int storeMetric(String met, int val) {
        String sql = "insert into metrics\n"
                + "values ("
                + +Core.getMonitoringId() + ","
                + "'" + met + "',"
                + val + ")";
        return insertDb(sql);
    }

    public int storeAnalytic(String an, int val) {

        String sql = "insert into analytics\n"
                + "values ("
                + +Core.getMonitoringId() + ","
                + "'" + an + "',"
                + val + ")";
        return insertDb(sql);
    }

    public ResultSet getCurrentAccessRecord() {
        String sql = "select * \n"
                + "from accesscontrol acc\n"
                + "where 1=1\n"
                + "and acc.Date>='2019-02-01'::DATE\n"
                + "and acc.Date<='2019-03-01'::DATE\n"
                + "and acc.Dest='Prod'";
        return selectDb(sql);
    }

    public ResultSet getTechChangesFromDB(String toDate, String fromDate) {
//new SimpleDateFormat("YYYY-MM-dd-HHmmss").format(new Date()) + "_" + sysName + ".csv";
        String sql = "select tec.ActionType, tec.ObjectType, tec.ObjectName,tec.Change,tec.\"comment\",tec.app\n"
                + "from techanalyse tec\n"
                + "where 1=1\n"
                + "and tec.Env='" + prefix + "'\n"
                + "and tec.Date::date BETWEEN '" + fromDate + "' and '" + toDate + "'";
        return selectDb(sql);
    }

    public ResultSet getAccChangesFromDB(String toDate, String fromDate) {
        String sql = "Select acc.ActionType,  acc.ObjectType,acc.ObjectName,acc.Change, acc.\"comment\" \n"
                + "from accesscontrol acc\n"
                + "where 1=1\n"
                + "and acc.Env='" + prefix + "'"
                + "and acc.Date::date BETWEEN '" + fromDate + "' and '" + toDate + "'";

        return selectDb(sql);
    }

    public ResultSet getAuthChangesFromDB(String toDate, String fromDate) {
        String sql = "Select aut.ActionType,  aut.ObjectType,aut.ObjectName,aut.Change,aut.App,aut.\"comment\", aut.comment2\n"
                + "from authority aut\n"
                + "where 1=1\n"
                + "and aut.Env='" + prefix + "'\n"
                + "and aut.Date::date BETWEEN '" + fromDate + "' and '" + toDate + "'";

        return selectDb(sql);
    }
}
