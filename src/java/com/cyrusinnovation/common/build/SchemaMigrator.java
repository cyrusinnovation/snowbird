package com.cyrusinnovation.common.build;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:21:20 PM
 */
public class SchemaMigrator {
    private AntSqlExecerImpl task;
    private SchemaUpdaterImpl schemaUpdater;
    private SchemaScriptManagerImpl scriptManager;
    private boolean hasChanges = false;

    protected SchemaMigrator(AntSqlExecerImpl task, SchemaUpdaterImpl schemaUpdater, SchemaScriptManagerImpl scriptManager) {
        this.task = task;
        this.schemaUpdater = schemaUpdater;
        this.scriptManager = scriptManager;
    }

    @Deprecated
    public static void main(String args[]) {
        // For backwards compatibility with old builds.
        Main.main(args);
    }

    public void run() throws SQLException, IOException {
        applyChanges();
        if (hasChanges()) task.execute();
    }

    public boolean hasChanges() {
        return hasChanges;
    }

    public void applyChanges() throws SQLException, IOException {
        List<SchemaUpdateScript> scriptsToExecute = scriptManager.scriptsWithVersionAbove(schemaUpdater.currentVersion());
        if (scriptsToExecute.isEmpty()) return;

        hasChanges = true;

        for (SchemaUpdateScript script : scriptsToExecute) {
            schemaUpdater.runScript(script);
        }
    }
}
