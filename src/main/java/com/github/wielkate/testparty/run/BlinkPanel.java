package com.github.wielkate.testparty.run;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public class BlinkPanel extends JPanel {
    private static final int BLINK_DURATION_MS = 100;
    private static final int BLINK_COUNT = 2;
    private static final Color BLINK_COLOR = JBColor.PINK;
    // transparent
    private static final Color OFF_COLOR = new JBColor(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0));

    private Timer timer;

    public BlinkPanel() {
        setOpaque(false);
    }

    public static void showOn(Project project) {
        JFrame frame = WindowManager.getInstance().getFrame(project);
        if (frame == null) return;

        JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();

        BlinkPanel panel = new BlinkPanel();
        panel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());

        layeredPane.add(panel, JLayeredPane.POPUP_LAYER);
        panel.start(layeredPane);
    }

    private void start(JLayeredPane layeredPane) {
        // counter should be array to be final in lambda
        final int[] counter = {0};

        timer = new Timer(BLINK_DURATION_MS, e -> {
            if (counter[0] >= BLINK_COUNT * 2) {
                timer.stop();

                layeredPane.remove(this);
                layeredPane.repaint();
                return;
            }

            boolean isBlinkOn = counter[0] % 2 == 0;
            setBackground(isBlinkOn ? BLINK_COLOR : OFF_COLOR);
            setOpaque(isBlinkOn);
            repaint();
            counter[0]++;
        });

        timer.start();
    }
}