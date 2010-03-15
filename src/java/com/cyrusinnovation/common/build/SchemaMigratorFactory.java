package com.cyrusinnovation.common.build;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaMigratorFactory {
    public SchemaMigrator createMigrator(String[] args) throws ClassNotFoundException, SQLException {
        // Load the JDBC driver
        String driverName = args[0];
        Class.forName(driverName);

        // Create a connection to the database
        String url = args[1]; // a JDBC url
        String scriptFolderName = args[2];
        String username = args[3];
        String password = "";
        String schema = null;
        if (args.length >= 5) password = args[4];
        if (args.length == 6) schema = args[5];

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        AntSqlExecerImpl antSqlTask = new AntSqlExecerImpl();
        antSqlTask.setDriver(driverName);
        antSqlTask.setUrl(url);
        antSqlTask.setUserid(username);
        antSqlTask.setPassword(password);

        return new SchemaMigrator(antSqlTask, 
              new SchemaUpdaterImpl(statement, antSqlTask, schema),
              new SchemaScriptManagerImpl(new File(scriptFolderName)));
    }
}
