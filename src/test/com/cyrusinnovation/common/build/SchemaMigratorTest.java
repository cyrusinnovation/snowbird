package com.cyrusinnovation.common.build;

import junit.framework.TestCase;
import org.mockito.InOrder;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:21:37 PM
 */
public class SchemaMigratorTest extends TestCase {
    private SchemaUpdaterImpl schemaUpdater;
    private SchemaScriptManagerImpl schemaScriptManager;
    private SchemaMigrator migrator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        schemaUpdater = mock(SchemaUpdaterImpl.class);
        schemaScriptManager = mock(SchemaScriptManagerImpl.class);

        migrator = new SchemaMigrator(mock(AntSqlExecerImpl.class), schemaUpdater, schemaScriptManager);
    }

    public void testAppliesEachSchemaScriptThatIsBeyondTheCurrentSchemaVersionAndUpdatesToTheNewVersion()
            throws SQLException, IOException {
        when(schemaUpdater.currentVersion()).thenReturn(17);

        SchemaUpdateScript script18 = SchemaUpdateScript.scriptFor(new File("18.sql"));
        SchemaUpdateScript script19 = SchemaUpdateScript.scriptFor(new File("19.sql"));
        when(schemaScriptManager.scriptsWithVersionAbove(17)).thenReturn(Arrays.asList(script18, script19));

        migrator.applyChanges();

        InOrder order = inOrder(schemaUpdater);
        order.verify(schemaUpdater).runScript(script18);
        order.verify(schemaUpdater).runScript(script19);
        assertTrue(migrator.hasChanges());
    }

    public void testDoesNothingIfThereAreNoNewScriptsFound() throws SQLException, IOException {
        when(schemaUpdater.currentVersion()).thenReturn(17);
        when(schemaScriptManager.scriptsWithVersionAbove(17)).thenReturn(new ArrayList<SchemaUpdateScript>());

        migrator.applyChanges();
        assertFalse(migrator.hasChanges());
    }
}
