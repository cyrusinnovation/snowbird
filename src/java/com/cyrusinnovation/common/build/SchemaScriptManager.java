package com.cyrusinnovation.common.build;

import java.util.*;
import java.io.*;

/**
 * User: rex
 * Date: Feb 8, 2005
 * Time: 1:28:40 PM
 */
public interface SchemaScriptManager {

   List scriptsWithVersionAbove(int currentVersion) throws IOException;
}
