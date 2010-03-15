package com.cyrusinnovation.common.build;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:21:37 PM
 */
public class SchemaMigratorTest extends MockObjectTestCase {
    private Mock schemaUpdater;
    private Mock schemaScriptManager;
    private SchemaMigrator migrator;

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      schemaUpdater = mock(SchemaUpdater.class);
      schemaScriptManager = mock(SchemaScriptManager.class);
      Mock sqlExecer = mock(AntSqlExecer.class);
      migrator = new SchemaMigrator(
            (AntSqlExecer) sqlExecer.proxy(),
            (SchemaUpdater) schemaUpdater.proxy(),
            (SchemaScriptManager) schemaScriptManager.proxy());
   }

   public void testAppliesEachSchemaScriptThatIsBeyondTheCurrentSchemaVersionAndUpdatesToTheNewVersion()
            throws SQLException, IOException {

        schemaUpdater.stubs().method("currentVersion").will(returnValue(17));
        SchemaUpdateScript script18 = new SchemaUpdateScript(new File("18.sql"));
        SchemaUpdateScript script19 = new SchemaUpdateScript(new File("19.sql"));

        schemaScriptManager.stubs().method("scriptsWithVersionAbove").with(eq(17)).will(
                returnValue(Arrays.asList(script18, script19)));

        schemaUpdater.expects(once()).method("runScript").with(eq(script18)).id("script 18");
        schemaUpdater.expects(once()).method("runScript").with(eq(script19)).after("script 18").id(
                "script 19");

        migrator.applyChanges();
        assertTrue(migrator.hasChanges());
    }

   public void testDoesNothingIfThereAreNoNewScriptsFound() throws SQLException, IOException {
        schemaUpdater.stubs().method("currentVersion").will(returnValue(17));

        schemaScriptManager.stubs().method("scriptsWithVersionAbove").with(eq(17)).will(
                returnValue(new ArrayList()));

        migrator.applyChanges();
        assertFalse(migrator.hasChanges());
    }
}
