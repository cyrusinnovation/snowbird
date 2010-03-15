package com.cyrusinnovation.common.build;

import junit.framework.TestCase;

import java.io.*;
import java.util.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 4:49:34 PM
 */
public class SchemaScriptManagerImplTest extends TestCase {
   private static final String TEST_SCRIPT_FOLDER_NAME = "testscripts";

   public void testFindsAllOfTheScriptsFromTheScriptDirectoryWithFileNamesGreaterThanTheVersionPassed() throws IOException {
      File scriptFolder = new File(TEST_SCRIPT_FOLDER_NAME);
      SchemaScriptManagerImpl scriptManager = new SchemaScriptManagerImpl(scriptFolder);

      List<SchemaUpdateScript> expectedScripts = scriptsMatching(scriptFolder, Arrays.asList("6.sql", "8.sql"));
      assertEquals(expectedScripts, scriptManager.scriptsWithVersionAbove(5));
   }

   private List<SchemaUpdateScript> scriptsMatching(File scriptFolder, final List<String> filenames) {
      File[] files = scriptFolder.listFiles(new FilenameFilter() {
         public boolean accept(File dir, String name) {
            return filenames.contains(name);
         }
      });

      List<SchemaUpdateScript> updateScripts = new ArrayList<SchemaUpdateScript>();
      for (File file : files) {
         updateScripts.add(new SchemaUpdateScript(file));
      }
      return updateScripts;
   }
}
