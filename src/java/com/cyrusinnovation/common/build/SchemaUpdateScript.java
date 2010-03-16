package com.cyrusinnovation.common.build;

import java.io.File;
import java.util.regex.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:30:46 PM
 */
public class SchemaUpdateScript implements Comparable {
   static final Pattern FILENAME_PATTERN = Pattern.compile("(\\d+)(-(.+))?\\.sql");
   private File srcFile;
   private int version;
   private String scriptGroup;

   public static SchemaUpdateScript scriptFor(File file) {
      Matcher matcher = FILENAME_PATTERN.matcher(file.getName());
      matcher.matches();
      int version = Integer.parseInt(matcher.group(1));

      String scriptGroup = null;
      if (matcher.groupCount() > 1) scriptGroup = matcher.group(3);

      return new SchemaUpdateScript(file, version, scriptGroup);
   }

   private SchemaUpdateScript(File srcFile, int version, String scriptGroup) {
      this.srcFile = srcFile;
      this.version = version;
      this.scriptGroup = scriptGroup;
   }

   public int version(){
      return version;
   }

   public File getSrcFile() {
      return srcFile;
   }

   @SuppressWarnings({"SimplifiableIfStatement"})
   public boolean isIn(String scriptGroup) {
      if (this.scriptGroup == null) return true;

      return scriptGroup.equals(this.scriptGroup);
   }

   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final SchemaUpdateScript that = (SchemaUpdateScript) o;

      return !(srcFile != null ? !srcFile.equals(that.srcFile) : that.srcFile != null);
   }

   public int hashCode() {
      return (srcFile != null ? srcFile.hashCode() : 0);
   }

   public int compareTo(Object o) {
      return (new Integer(version)).compareTo(((SchemaUpdateScript) o).version);
   }

   public String toString(){
      return "File: "+srcFile.toString();
   }
}
