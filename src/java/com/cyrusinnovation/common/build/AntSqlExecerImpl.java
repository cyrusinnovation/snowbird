package com.cyrusinnovation.common.build;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.SQLExec;

public class AntSqlExecerImpl extends SQLExec {
    public AntSqlExecerImpl() {
        Project project = new Project();
        setProject(project);
        project.init();
        setTaskType("sql");
        setTaskName("sql");
        setOwningTarget(new Target());
    }
}
