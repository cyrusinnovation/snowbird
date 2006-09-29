package com.cyrusinnovation.common.build;

import java.io.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:30:46 PM
 */
public class SchemaUpdateScript implements Comparable{
   private int version;
   private File srcFile;

   public SchemaUpdateScript(File srcFile) {
      this.srcFile = srcFile;
      this.version = Integer.parseInt(srcFile.getName().split("\\.")[0]);
   }

   public int version(){
      return version;

   }

   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final SchemaUpdateScript that = (SchemaUpdateScript) o;

      if (srcFile != null ? !srcFile.equals(that.srcFile) : that.srcFile != null) return false;

      return true;
   }

   public int hashCode() {
      return (srcFile != null ? srcFile.hashCode() : 0);
   }

   public int compareTo(Object o) {
      return (new Integer(version)).compareTo(new Integer(((SchemaUpdateScript) o).version));
   }

   public String toString(){

      return "File: "+srcFile.toString();
   }


   public File getSrcFile() {
      return srcFile;
   }

}
