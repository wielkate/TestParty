package com.github.wielkate.testparty.run;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class ConfettiPanel extends JPanel {
    private static final int ITEM_SIZE = 10;
    private static final int TOTAL_ITEMS = 500;

    private final List<ConfettiItem> items = new ArrayList<>();
    private Timer timer;

    public ConfettiPanel(int width, int height) {
        setOpaque(false);
        setFocusable(false);
        setBounds(0, 0, width, height);
        for (int i = 0; i < TOTAL_ITEMS; i++)
            items.add(new ConfettiItem(width));
    }

    public static void showOn(Project project) {
        JFrame frame = WindowManager.getInstance().getFrame(project);
        if (frame == null) return;

        JLayeredPane layeredPane = frame.getRootPane().getLayeredPane();
        ConfettiPanel panel = new ConfettiPanel(layeredPane.getWidth(), layeredPane.getHeight());
        layeredPane.add(panel, JLayeredPane.POPUP_LAYER);
        panel.start(layeredPane);
    }

    private void start(JLayeredPane parent) {
        timer = new Timer(0, e -> {

            items.removeIf(ConfettiItem::isDead);
            items.forEach(i -> i.update(getHeight()));
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

        for (ConfettiItem item : items) {
            AffineTransform old = g2d.getTransform();
            g2d.translate(item.x, item.y);
            g2d.rotate(Math.toRadians(item.rotation));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, item.opacity)));
            g2d.setColor(item.color);
            g2d.fillRect(-ITEM_SIZE / 2, -ITEM_SIZE / 2, ITEM_SIZE, ITEM_SIZE);
            g2d.setTransform(old);
        }
        g2d.dispose();
    }
}