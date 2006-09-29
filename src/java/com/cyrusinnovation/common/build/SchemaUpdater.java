package com.cyrusinnovation.common.build;

import java.sql.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:26:21 PM
 */
public interface SchemaUpdater {

   int currentVersion() throws SQLException;

   void runScript(SchemaUpdateScript script) throws SQLException;

}
