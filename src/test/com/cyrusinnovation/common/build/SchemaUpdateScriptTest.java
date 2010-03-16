package com.cyrusinnovation.common.build;

import junit.framework.TestCase;

import java.io.File;

/**
 * User: Cyrus01
 * Date: Mar 16, 2010
 * Time: 11:03:07 AM
 */
public class SchemaUpdateScriptTest extends TestCase {
   private SchemaUpdateScript script;

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      script = scriptFor("18-group.sql");
   }

   public void testShouldBeInGroupSpecifiedInFilename() {
      assertTrue(script.isIn("group"));
   }

   public void testShouldNotBeInGroupOtherThanTheOneInFileName() {
      assertFalse(script.isIn("otherGroup"));
   }

   public void testShouldBeInAllGroupsWhenNoneInFilename() {
      script = scriptFor("18.sql");
      assertTrue(script.isIn("group"));
      assertTrue(script.isIn("otherGroup"));
   }

   private SchemaUpdateScript scriptFor(String filename) {
      return SchemaUpdateScript.scriptFor(new File(filename));
   }
}
