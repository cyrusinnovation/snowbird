package com.cyrusinnovation.common.build;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 3:04:31 PM
 */
public class SchemaUpdaterImpl implements SchemaUpdater {
    private Statement statement;
    private AntSqlExecer task;
    private String schema;

   public SchemaUpdaterImpl(Statement statement, AntSqlExecer task, String schema) {
        this.statement = statement;
        this.task = task;
        this.schema = schema;
    }

    public int currentVersion() throws SQLException {
        ResultSet rs = statement.executeQuery(buildVersionQuery());
        rs.next();
        int version = rs.getInt("version");
        System.out.println("Current version :" + version);
        return version;
    }

    protected String buildVersionQuery() {
       String tableName = "schema_version";
       if (null != schema) tableName = schema + "." + tableName;

       return String.format("SELECT * from " + tableName);
    }

    public void runScript(SchemaUpdateScript script) throws SQLException {
        task.createTransaction().setSrc(script.getSrcFile());
        System.out.println("running script " + script.version());
        updateVersionTo(script.version());
    }

    private void updateVersionTo(int newVersion) throws SQLException {
        task.createTransaction().addText("UPDATE schema_version set version='" + newVersion + "'");
    }
}
