package com.cyrusinnovation.common.build;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaMigratorFactory {

    public SchemaMigrator createMigrator(String[] args) throws ClassNotFoundException, SQLException {

        // Load the JDBC driver
        String driverName = args[0]; // MySQL MM JDBC driver
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

        Statement stmt = connection.createStatement();

        AntSqlExecerImpl task = new AntSqlExecerImpl();
        task.setDriver(driverName);
        task.setUrl(url);
        task.setUserid(username);
        task.setPassword(password);


        return new SchemaMigrator(task, new SchemaUpdaterImpl(stmt, task, schema), new SchemaScriptManagerImpl(new File(scriptFolderName)));
    }

}
