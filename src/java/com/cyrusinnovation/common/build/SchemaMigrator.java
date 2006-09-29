package com.cyrusinnovation.common.build;

import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.*;

import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:21:20 PM
 */
public class SchemaMigrator {
   private SchemaUpdater schemaUpdater;
   private SchemaScriptManager scriptManager;
   private boolean hasChanges;

   public SchemaMigrator(SchemaUpdater schemaUpdater, SchemaScriptManager scriptManager) {
      this.schemaUpdater = schemaUpdater;
      this.scriptManager = scriptManager;

   }

   public static void main(String args[]) {

      try {
         Connection connection = null;


         // Load the JDBC driver
         String driverName = args[0]; // MySQL MM JDBC driver
         Class.forName(driverName);

         // Create a connection to the database
         String url = args[1]; // a JDBC url
         String scriptFolderName = args[2];
         String username = args[3];
         String password = "";
         if (args.length == 5) password = args[4];

         connection = DriverManager.getConnection(url, username, password);

         Statement stmt = connection.createStatement();


         SQLExecer task = new SQLExecer();
         task.setDriver(driverName);
         task.setUrl(url);
         task.setUserid(username);
         task.setPassword(password);



         SchemaMigrator migrator = new SchemaMigrator(new SchemaUpdaterImpl(stmt, task), new SchemaScriptManagerImpl(new File(scriptFolderName)));
         migrator.applyChanges();
         if (migrator.hasChanges()) task.execute();
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   }

   public boolean hasChanges() {
      return hasChanges;
   }


   static final class SQLExecer extends SQLExec {
      public SQLExecer() {
         Project project = new Project();
         setProject(project);
         project.init();
         setTaskType("sql");
         setTaskName("sql");
         setOwningTarget(new Target());

      }
   }



   public void applyChanges() throws SQLException, IOException {


      List scriptsToExecute = scriptManager.scriptsWithVersionAbove(schemaUpdater.currentVersion());
      if (scriptsToExecute.isEmpty()) {
         hasChanges = false;
         return;
      }

      hasChanges = true;

      SchemaUpdateScript lastScript = null;
      for (Iterator i = scriptsToExecute.iterator(); i.hasNext();) {
         lastScript = (SchemaUpdateScript) i.next();
         schemaUpdater.runScript(lastScript);
      }


   }
}
