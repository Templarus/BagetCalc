package com.bcalc.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import com.bcalc.core.Core;
import static com.bcalc.core.Core.loggerConsole;

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
    public ServerDB() {
        loggerConsole.info("DB.ServerDb.<init>()");

        this.url = Core.ppCollector.getDummyData();
        this.dbUser = Core.ppCollector.getDummyData();
        this.password = Core.ppCollector.getDummyData();
        if (connect(url, dbUser, password)) {
            JOptionPane.showMessageDialog(null, "Не удается устрановить соединение с базой", "Внимание", 0);

        } else {
            isReady = true;
        }
        loggerConsole.debug("StoreDB(): constructor");
    }

    public ServerDB(String url, String user, String password) {
        loggerConsole.debug("DB.ServerDb.<init>(3)");
        this.url = url;
        this.dbUser = user;
        this.password = password;
        if (connect()) {
            JOptionPane.showMessageDialog(null, "Не удается устрановить соединение с postgres", "Внимание", 0);
        }
        loggerConsole.debug("SERVERDb: constructor");
    }

    /**
     * Метод создающий коннект с базой
     *
     * url user password
     */
    private boolean connect(String url, String user, String password) {
        loggerConsole.info("StoreDB connect method");
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);//Подключаемся к баде данных
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//Делаем многонаправленный ResultSet
            loggerConsole.info("SUCCESSFULLY connected with database!");
        } catch (SQLException | ClassNotFoundException ex) {
            loggerConsole.error("StoreDB:getData():Ошибка подключения PSQL или создание Statement - ", ex);
            return false;
        }
        return true;
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
            loggerConsole.error("ServerDb:selectDb():Ошибка подключения или создание Statement sql: " + sql, ex);
        }
        return rs;
    }

    public int insertDb(String sql) {
        try {
            st = conn.createStatement();
            return st.executeUpdate(sql);
        } catch (SQLException ex) {
            loggerConsole.error("ServerDb:insertDb():Ошибка подключения или создание Statement sql: " + sql, ex);
        }
        return -1;
    }

    public int updateDb(String sql) {
        try {
            st = conn.createStatement();
            return st.executeUpdate(sql);
        } catch (SQLException ex) {
            loggerConsole.error("ServerDb:updateDb():Ошибка подключения или создание Statement sql: " + sql, ex);
        }
        return -1;
    }

    private boolean connect() {
        loggerConsole.info("DB.ServerDb.connect()");
        if (this.connect(this.url, this.dbUser, this.password) == true) {
            return true;
        }
        return false;
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
            loggerConsole.error("DB.StoreDB.setnewMon()", ex);
        }
        return monitoringID;

    }

}
