package com.cyrusinnovation.common.build;


import java.util.*;
import java.io.*;

import org.jmock.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 4:49:34 PM
 */
public class SchemaScriptManagerImplTest extends MockObjectTestCase {
   private static final String TEST_SCRIPT_FOLDER_NAME = "./testscripts";


   public void testFindsAllOfTheScriptsFromTheScriptDirectoryWithFileNamesGreaterThanTheVersionPassed()
         throws IOException {


      File scriptFolder = new File(SchemaScriptManagerImplTest.class.getResource(TEST_SCRIPT_FOLDER_NAME).getFile());
      System.out.println(scriptFolder.getAbsolutePath());
      SchemaScriptManagerImpl scriptManager = new SchemaScriptManagerImpl(scriptFolder);
      List<SchemaUpdateScript> updateScripts = scriptsMatching(scriptFolder, Arrays.asList("6.sql", "8.sql"));
      assertEquals(updateScripts, scriptManager.scriptsWithVersionAbove(5));


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
