package com.cyrusinnovation.common.build;

import org.jmock.*;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:21:37 PM
 */
public class SchemaMigratorTest extends MockObjectTestCase {


   public void testAppliesEachSchemaScriptThatIsBeyondTheCurrentSchemaVersionAndUpdatesToTheNewVersion()
         throws SQLException, IOException {

      Mock schemaUpdater = mock(SchemaUpdater.class);
      Mock schemaScriptManager = mock(SchemaScriptManager.class);
      SchemaMigrator migrator = new SchemaMigrator((SchemaUpdater) schemaUpdater.proxy(),
            (SchemaScriptManager) schemaScriptManager.proxy());

      schemaUpdater.stubs().method("currentVersion").will(returnValue(17));
      SchemaUpdateScript script18 = new SchemaUpdateScript(new File("18.sql"));
      SchemaUpdateScript script19 = new SchemaUpdateScript(new File("19.sql"));

      schemaScriptManager.stubs().method("scriptsWithVersionAbove").with(eq(17)).will(
            returnValue(Arrays.asList(new Object[]{script18, script19})));

      schemaUpdater.expects(once()).method("runScript").with(eq(script18)).id("script 18");
      schemaUpdater.expects(once()).method("runScript").with(eq(script19)).after("script 18").id(
            "script 19");

      migrator.applyChanges();
      assertTrue(migrator.hasChanges());


   }

   public void testDoesNothingIfThereAreNoNewScriptsFound() throws SQLException, IOException {
      Mock schemaUpdater = mock(SchemaUpdater.class);
      Mock schemaScriptManager = mock(SchemaScriptManager.class);
      SchemaMigrator migrator = new SchemaMigrator((SchemaUpdater) schemaUpdater.proxy(),
            (SchemaScriptManager) schemaScriptManager.proxy());

      schemaUpdater.stubs().method("currentVersion").will(returnValue(17));

      schemaScriptManager.stubs().method("scriptsWithVersionAbove").with(eq(17)).will(
            returnValue(new ArrayList()));

      migrator.applyChanges();
      assertFalse(migrator.hasChanges());

   }


}
