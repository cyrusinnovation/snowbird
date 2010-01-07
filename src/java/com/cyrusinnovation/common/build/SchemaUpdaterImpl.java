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
    private Statement stmt;
    private AntSqlExecer task;
    private String schema;

    public SchemaUpdaterImpl(Statement stmt, AntSqlExecer task, String schema) {
        this.stmt = stmt;
        this.task = task;
        this.schema = schema;
    }

    public int currentVersion() throws SQLException {
        ResultSet rs = stmt.executeQuery(buildVersionQuery());
        rs.next();
        int version = rs.getInt("version");
        System.out.println("Current version :" + version);
        return version;

    }

    protected String buildVersionQuery() {
        return String.format("SELECT * from %sschema_version",
                schema == null ? "" : schema.concat(".")
        );
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
