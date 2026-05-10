package com.github.wielkate.testparty.run;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class SmokePanel extends JPanel {
    private static final int TOTAL_ITEMS = 25;
    private static final Random RANDOM = new Random();

    private final ArrayList<float[]> items = new ArrayList<>();
    private Timer timer;

    public SmokePanel(int width, int height) {
        setOpaque(false);
        setFocusable(false);
        setBounds(0, 0, width, height);

        for (int i = 0; i < TOTAL_ITEMS; i++) {
            float angle = RANDOM.nextFloat() * 6.28f;
            float speed = 0.5f + RANDOM.nextFloat() * 2f;

            // {x, y, vx, vy, radius, alpha}
            items.add(new float[]{
                    width / 2f, height / 2f,
                    (float) Math.cos(angle) * speed,
                    (float) Math.sin(angle) * speed - 1.5f,
                    50f,
                    0.5f
            });
        }
    }

    public static void showOn(Project project) {
        JFrame frame = WindowManager.getInstance().getFrame(project);
        if (frame == null) return;

        JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();
        var panel = new SmokePanel(layeredPane.getWidth(), layeredPane.getHeight());
        layeredPane.add(panel, JLayeredPane.POPUP_LAYER);
        panel.start(layeredPane);
    }

    private void start(JLayeredPane parent) {
        timer = new Timer(16, e -> {
            items.removeIf(item -> (item[5] -= 0.012f) <= 0);
            items.forEach(item -> {
                item[0] += item[2];
                item[1] += item[3];
                item[3] -= 0.03f;
                item[4] += 0.4f;
            });
            repaint();
            if (items.isEmpty()) {
                timer.stop();
                parent.remove(this);
                parent.repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (float[] item : items) {
            RadialGradientPaint gradient = new RadialGradientPaint(
                    item[0], item[1], item[4],
                    new float[]{0f, 1f},
                    new Color[]{
                            new JBColor(new Color(0f, 0f, 0f, item[5]), new Color(1f, 1f, 1f, item[5])),   // center: opaque
                            new JBColor(new Color(0f, 0f, 0f, 0f), new Color(1f, 1f, 1f, 0f))      // edge: fully transparent
                    }
            );
            g2d.setPaint(gradient);
            g2d.fill(new Ellipse2D.Float(item[0] - item[4], item[1] - item[4], item[4] * 2, item[4] * 2));
        }
        g2d.setPaint(null);
    }
}
