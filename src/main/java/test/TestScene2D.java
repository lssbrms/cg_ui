/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package test;

import com.jme3.math.Vector2f;
import math.MathF;
import ui.Scene2D;

import java.awt.*;

/**
 * This class can be used to test the Scene2D functionality.
 */
public class TestScene2D extends Scene2D {
  public TestScene2D(int width, int height) {
    super(width, height);
    setRenderArea(new Vector2f(-1, -1), new Vector2f(1, 1));
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D graphics2D = (Graphics2D) g;
    drawLine(graphics2D, new Vector2f(-0.5f, MathF.random() * 2 - 1), new Vector2f(0.5f, MathF.random() * 2 - 1), Color.BLUE);
  }

  @Override
  public String getTitle() {
    return "Scene 2D";
  }
}
