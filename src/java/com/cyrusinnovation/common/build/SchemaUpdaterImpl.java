package com.cyrusinnovation.common.build;

import org.apache.tools.ant.taskdefs.*;

import java.sql.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 3:04:31 PM
 */
public class SchemaUpdaterImpl implements SchemaUpdater {
   private Statement stmt;
   private SQLExec task;

   public SchemaUpdaterImpl(Statement stmt, SQLExec task) {
      this.stmt = stmt;
      this.task = task;
   }

   public int currentVersion() throws SQLException {
      ResultSet rs = stmt.executeQuery("SELECT * from schema_version");
      rs.next();
      int version = rs.getInt("version");
      System.out.println("Current version :"+version);
      return version;

   }

   public void runScript(SchemaUpdateScript script) throws SQLException {
      task.createTransaction().setSrc(script.getSrcFile());
      System.out.println("running script "+script.version());
      updateVersionTo(script.version());
   }

   private void updateVersionTo(int newVersion) throws SQLException {
      task.createTransaction().addText("UPDATE schema_version set version='"+newVersion+"'");

   }
}
