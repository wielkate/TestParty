package com.github.wielkate.testparty.run;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsAdapter;
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;

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

        final var context = ConfigurationContext.getFromContext(e.getDataContext(), e.getPlace());
        final var selected = context.getConfiguration();

        if (selected == null) {
            System.out.println("No run configuration selected.");
            return;
        }

        installResultSoundListener(project);

        try {
            Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            ExecutionEnvironmentBuilder builder =
                    ExecutionEnvironmentBuilder.create(project, executor, selected.getConfiguration());
            ExecutionManager.getInstance(project).restartRunProfile(builder.build());
        } catch (Exception ex) {
            System.out.println("Failed to run configuration: " + ex.getMessage());
        }
    }

    private void installResultSoundListener(@NotNull Project project) {
        final var successClip = loadClip("sounds/success.wav");
        final var errorClip = loadClip("sounds/error.wav");
        final var ignoreClip = loadClip("sounds/skip.wav");

        MessageBusConnection connection = project.getMessageBus().connect();
        connection.subscribe(SMTRunnerEventsListener.TEST_STATUS, new SMTRunnerEventsAdapter() {
            @Override
            public void onTestFinished(@NotNull SMTestProxy test) {
                if (!test.isLeaf()) return; // skip suite-level events, only individual tests
                if (test.isPassed()) {
                    ConfettiPanel.showOn(project);
                    play(successClip);
                } else if (test.isIgnored()) {
                    play(ignoreClip);
                } else {
                    SmokePanel.showOn(project);
                    play(errorClip);
                }
                sleep(5000);
            }
        });
    }

    private Clip loadClip(String resourcePath) {
        var inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            System.out.println("Sound resource not found: " + resourcePath);
            return null;
        }
        var buffered = new BufferedInputStream(inputStream);
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(buffered);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
        } catch (Exception ex) {
            System.out.println("Failed to load sound: " + resourcePath + " - " + ex.getMessage());
            return null;
        }
    }

    private void play(Clip clip) {
        if (clip == null) return;
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final var project = e.getProject();
        if (project == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }
        ConfigurationContext context = ConfigurationContext.getFromContext(e.getDataContext(), e.getPlace());
        e.getPresentation().setEnabledAndVisible(context.getConfiguration() != null);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}