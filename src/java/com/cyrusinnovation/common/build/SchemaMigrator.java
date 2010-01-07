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
    private AntSqlExecer task;
    private SchemaUpdater schemaUpdater;
    private SchemaScriptManager scriptManager;
    private boolean hasChanges;

    protected SchemaMigrator(AntSqlExecer task, SchemaUpdater schemaUpdater, SchemaScriptManager scriptManager) {
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
        List scriptsToExecute = scriptManager.scriptsWithVersionAbove(schemaUpdater.currentVersion());
        if (scriptsToExecute.isEmpty()) {
            hasChanges = false;
            return;
        }

        hasChanges = true;

        SchemaUpdateScript lastScript;
        for (Object aScriptsToExecute : scriptsToExecute) {
            lastScript = (SchemaUpdateScript) aScriptsToExecute;
            schemaUpdater.runScript(lastScript);
        }


    }
}
