package com.cyrusinnovation.common.build;

import junit.framework.TestCase;

import java.io.*;
import java.util.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 4:49:34 PM
 */
@SuppressWarnings({"ResultOfMethodCallIgnored"})
public class SchemaScriptManagerImplTest extends TestCase {
   private static final String TEST_SCRIPT_FOLDER_NAME = "testscripts";
   private static final String DEFAULT_SCRIPT_GROUP = "bc";
   private static final int CURRENT_SCHEMA_VERSION = 5;
   private File testScriptFolder;
   private SchemaScriptManagerImpl scriptManager;

   @Override
   protected void setUp() throws Exception {
      super.setUp();

      createScriptFolder();

      scriptManager = new SchemaScriptManagerImpl(testScriptFolder, DEFAULT_SCRIPT_GROUP);
   }

   public void testFindsAllOfTheScriptsFromTheScriptDirectoryWithFileNamesGreaterThanTheVersionPassed() throws IOException {
      withMigrationScripts("4.sql", "5.sql", "6.sql", "8.sql");

      assertScriptsWithVersionAboveCurrent("6.sql", "8.sql");
   }

   public void testShouldNotIncludeNonSqlFilesInScriptList() throws IOException {
      withMigrationScripts("6.sql", "7.not_sql", "8.sql");

      assertScriptsWithVersionAboveCurrent("6.sql", "8.sql");
   }

   public void testShouldIncludeFilesForMigrationGroup() throws IOException {
      scriptManager = new SchemaScriptManagerImpl(testScriptFolder, "group");

      withMigrationScripts("6.sql", "7-group.sql", "8-other.sql");

      assertScriptsWithVersionAboveCurrent("6.sql", "7-group.sql");
   }

   private void createScriptFolder() {
      testScriptFolder = new File(TEST_SCRIPT_FOLDER_NAME);
      if (!testScriptFolder.exists()) testScriptFolder.mkdir();
   }

   private void withMigrationScripts(String... scriptNames) throws IOException {
      for (String scriptName : scriptNames) {
         File script = new File(testScriptFolder, scriptName);
         if (!script.exists()) script.createNewFile();
      }
   }

   private void assertScriptsWithVersionAboveCurrent(String... filenames) throws IOException {
      List<String> expectedFilenames = Arrays.asList(filenames);

      List<SchemaUpdateScript> scripts = scriptManager.scriptsWithVersionAbove(CURRENT_SCHEMA_VERSION);
      ArrayList<String> actualFilenames = new ArrayList<String>();
      for (SchemaUpdateScript schemaUpdateScript : scripts) {
         actualFilenames.add(schemaUpdateScript.getSrcFile().getName());
      }

      assertEquals(expectedFilenames, actualFilenames);
   }

   @Override
   protected void tearDown() throws Exception {
      for (File file : testScriptFolder.listFiles()) {
         file.delete();
      }
      testScriptFolder.delete();
      super.tearDown();
   }
}
