package com.github.wielkate.testparty.run;

import com.intellij.ui.JBColor;

import java.awt.*;
import java.util.Random;

public class ConfettiItem {
    public double x, y;
    public double speedX, speedY;
    public double rotation, rotationSpeed;
    public Color color;
    public float opacity = 1.0f;

    private static final Color[] COLORS = {
            new JBColor(new Color(255, 80, 80), new Color(255, 80, 80)), // red
            new JBColor(new Color(80, 200, 120), new Color(80, 200, 120)),  // green
            new JBColor(new Color(80, 140, 255), new Color(80, 140, 255)),  // blue
            new JBColor(new Color(255, 210, 60), new Color(255, 210, 60)),  // yellow
            new JBColor(new Color(200, 100, 255), new Color(200, 100, 255)), // purple
            new JBColor(new Color(255, 150, 50), new Color(255, 150, 50)),  // orange
    };

    public ConfettiItem(int panelWidth) {
        x = Math.random() * panelWidth;
        y = -new Random().nextInt(21);
        speedX = (Math.random() - 0.5) * 4;
        speedY = 3 + Math.random() * 5;
        rotation = Math.random() * 360;
        rotationSpeed = (Math.random() - 0.5) * 10;
        color = COLORS[(int) (Math.random() * COLORS.length)];
    }

    public void update(int panelHeight) {
        x += speedX;
        y += speedY;
        rotation += rotationSpeed;
        if (y > panelHeight - 60) {
            opacity -= 0.03f; // fade out near bottom
        }
    }

    public boolean isDead() {
        return opacity <= 0;
    }
}
