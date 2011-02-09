package com.cyrusinnovation.common.build;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.cyrusinnovation.common.build.SchemaUpdateScript.scriptFor;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 4:49:20 PM
 */
public class SchemaScriptManagerImpl {
    private File scriptFolder;
    private String scriptGroup;

    public SchemaScriptManagerImpl(File scriptFolderName, String scriptGroup) {
        this.scriptFolder = scriptFolderName;
        this.scriptGroup = scriptGroup;
    }

    public List<SchemaUpdateScript> scriptsWithVersionAbove(int currentVersion) throws IOException {
        File[] sqlFiles = findAllSqlFiles();

        List<SchemaUpdateScript> allScripts = convertFilesToScriptObjects(sqlFiles);

        ArrayList<SchemaUpdateScript> scriptsInGroup = allScriptsInGroup(allScripts);

        removeScriptsLessThanOrEqualToCurrentVersion(scriptsInGroup, currentVersion);

        Collections.sort(scriptsInGroup);

        return scriptsInGroup;
    }

    private File[] findAllSqlFiles() {
        return scriptFolder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".sql");
            }
        });
    }

    private List<SchemaUpdateScript> convertFilesToScriptObjects(File[] files) {
        List<SchemaUpdateScript> retVal = new ArrayList<SchemaUpdateScript>();

        for (File file : files) {
            retVal.add(scriptFor(file));
        }

        return retVal;
    }

    private ArrayList<SchemaUpdateScript> allScriptsInGroup(List<SchemaUpdateScript> allScripts) {
        ArrayList<SchemaUpdateScript> scriptsInGroup = new ArrayList<SchemaUpdateScript>();
        for (SchemaUpdateScript script : allScripts) {
            if (script.isIn(scriptGroup)) {
                scriptsInGroup.add(script);
            }
        }
        return scriptsInGroup;
    }

    private void removeScriptsLessThanOrEqualToCurrentVersion(List allScripts, int currentVersion) {
        for (Iterator i = allScripts.iterator(); i.hasNext();) {
            SchemaUpdateScript script = (SchemaUpdateScript) i.next();
            if (script.version() <= currentVersion) i.remove();
        }
    }
}
