package com.cyrusinnovation.common.build;

public interface AntSqlExecer {
    public org.apache.tools.ant.taskdefs.SQLExec.Transaction createTransaction();

    public void execute() throws org.apache.tools.ant.BuildException; 
}
