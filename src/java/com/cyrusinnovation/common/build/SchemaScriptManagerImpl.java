package com.cyrusinnovation.common.build;

import java.util.*;
import java.io.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 4:49:20 PM
 */
public class SchemaScriptManagerImpl implements SchemaScriptManager {
   private File scriptFolder;

   public SchemaScriptManagerImpl(File scriptFolderName) {
      this.scriptFolder = scriptFolderName;
   }

   public List<SchemaUpdateScript> scriptsWithVersionAbove(int currentVersion) throws IOException {
      File[] files = findAllSqlFiles();

      List<SchemaUpdateScript> allScripts = convertFilesToScriptObjects(files);

      removeScriptsLessThanOrEqualToCurrentVersion(allScripts, currentVersion);

      Collections.sort(allScripts);

      return allScripts;
   }

   private void removeScriptsLessThanOrEqualToCurrentVersion(List allScripts, int currentVersion) {
      for (Iterator i = allScripts.iterator(); i.hasNext();) {
         SchemaUpdateScript script = (SchemaUpdateScript) i.next();
         if (script.version() <= currentVersion) i.remove();
      }
   }

   private List<SchemaUpdateScript> convertFilesToScriptObjects(File[] files) {
      List<SchemaUpdateScript> retVal = new ArrayList<SchemaUpdateScript>();

      for (File file : files) {
         SchemaUpdateScript script = new SchemaUpdateScript(file);
         retVal.add(script);
      }

      return retVal;
   }

   private File[] findAllSqlFiles() {
      System.out.println(scriptFolder.getAbsolutePath());
      return scriptFolder.listFiles(new FilenameFilter(){
         public boolean accept(File dir, String name) {
            return name.endsWith(".sql");
         }
      });
   }

}
