package com.github.wielkate.testparty.run;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MyAction extends AnAction {
    private static final String ACTION_TEXT = "Run '%s()' with Effects";
    private static final String ACTION_DESCRIPTION = "Run Test with Effects";

    // Action class must have a no-argument constructor
    public MyAction() {
        super(String.format(ACTION_TEXT, "test"), ACTION_DESCRIPTION, AllIcons.RunConfigurations.TestCustom);
    }

    public MyAction(String testName) {
        super(String.format(ACTION_TEXT, testName), ACTION_DESCRIPTION, AllIcons.RunConfigurations.TestCustom);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        RunManager runManager = RunManager.getInstance(project);
        RunnerAndConfigurationSettings selected = runManager.getSelectedConfiguration();

        if (selected == null) {
            System.out.println("No run configuration selected.");
            return;
        }

        try {
            Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            ExecutionEnvironmentBuilder builder =
                    ExecutionEnvironmentBuilder.create(project, executor, selected.getConfiguration());
            ExecutionManager.getInstance(project).restartRunProfile(builder.build());
        } catch (Exception ex) {
            System.out.println("Failed to run configuration: " + ex.getMessage());
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        boolean hasConfig = project != null &&
                RunManager.getInstance(project).getSelectedConfiguration() != null;
        e.getPresentation().setEnabledAndVisible(hasConfig);
    }

}