/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */
package ui;

import com.jme3.math.Vector2f;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.util.List;

/**
 * Base scene canvas class for all 2D implementations.
 */
public abstract class Scene2D extends JPanel {

  protected static final int POINT_SIZE = 6;

  /**
   * Lower left corner in world coordinates.
   */
  private Vector2f ll;

  /**
   * Upper right corner in world coordinates.
   */
  private Vector2f ur;

  /**
   * Last mouse position.
   */
  protected Vector2f lastMousePosition;

  public Scene2D(int width, int height, Vector2f ll, Vector2f ur) {
    setSize(width, height);
    setPreferredSize(new Dimension(width, height));
    lastMousePosition = null;
    setRenderArea(ll, ur);
  }

  /**
   * Draw the scene content.
   */
  public abstract void paint(Graphics g);

  /**
   * Returns the user interface for the scene
   *
   * @return null, if the scene has no user interface
   */
  public JPanel getUserInterface() {
    // Default: none
    return null;
  }

  public abstract String getTitle();

  /**
   * Set the displayed render area.
   */
  public void setRenderArea(Vector2f ll, Vector2f ur) {
    this.ll = ll;
    this.ur = ur;
  }

  public Scene2D(int width, int height) {
    this(width, height, new Vector2f(-3, -3), new Vector2f(3, 3));
  }

  /**
   * Draw a line from a to be using the given color.
   */
  protected void drawLine(Graphics gc, Vector2f a, Vector2f b, Color color) {
    Vector2f a_ = world2Pixel(a);
    Vector2f b_ = world2Pixel(b);
    gc.setColor(color);
    gc.drawLine((int) a_.x, (int) a_.y, (int) b_.x, (int) b_.y);
  }

  protected void drawPoly(Graphics gc, List<Vector2f> poly, Color lineColor) {
    drawPoly(gc, poly, lineColor, null);
  }

  /**
   * Draw a cubic curve using the given control points.
   */
  protected void drawCubicCurve(Graphics2D gc, Vector2f c0, Vector2f c1, Vector2f c2, Vector2f c3, Color color) {
    CubicCurve2D curveShape = new CubicCurve2D.Float();
    c0 = world2Pixel(c0);
    c1 = world2Pixel(c1);
    c2 = world2Pixel(c2);
    c3 = world2Pixel(c3);
    curveShape.setCurve(c0.x, c0.y, c1.x, c1.y, c2.x, c2.y, c3.x, c3.y);
    gc.setColor(color);
    gc.draw(curveShape);
  }

  protected void drawPoly(Graphics gc, List<Vector2f> poly, Color lineColor, Color fillColor) {
    int[] xPoints = new int[poly.size()];
    int[] yPoints = new int[poly.size()];
    for (int i = 0; i < poly.size(); i++) {
      Vector2f p_ = world2Pixel(poly.get(i));
      xPoints[i] = (int) p_.x;
      yPoints[i] = (int) p_.y;
    }

    // Fill
    if (fillColor != null) {
      gc.setColor(fillColor);
      gc.fillPolygon(xPoints, yPoints, poly.size());
    }

    // Outline
    if (lineColor != null) {
      gc.setColor(lineColor);
      gc.drawPolygon(xPoints, yPoints, poly.size());
    }
  }

  /**
   * Draw a point at the posisition p using the given color.
   */
  protected void drawPoint(Graphics gc, Vector2f p, Color color) {
    Vector2f pixelPoint = world2Pixel(p);
    int x = (int) pixelPoint.x - POINT_SIZE / 2;
    int y = (int) pixelPoint.y - POINT_SIZE / 2;
    gc.setColor(color);
    gc.fillArc(x, y, POINT_SIZE, POINT_SIZE, 0, 360);
    gc.setColor(Color.BLACK);
    gc.drawArc(x, y, POINT_SIZE, POINT_SIZE, 0, 360);
  }

  protected void drawText(Graphics2D g2d, String text, Vector2f p, Color color) {
    Vector2f pPix = world2Pixel(p);
    g2d.setColor(color);
    g2d.drawString(text, (int) pPix.x, (int) pPix.y);
  }

  /**
   * World -> pixel coordinates.
   */
  protected Vector2f world2Pixel(Vector2f pWorld) {
    return unit2Pixel(world2Unit(pWorld));
  }

  /**
   * Pixel coordinates -> world coordinates.
   */
  protected Vector2f pixel2World(Vector2f pPixel) {
    return unit2World(pixel2Unit(pPixel));
  }

  /**
   * World coordinates -> unit coordinates.
   */
  private Vector2f world2Unit(Vector2f pWorld) {
    Vector2f diagonal = ur.subtract(ll);
    float scale = Math.max(diagonal.x, diagonal.y);
    Vector2f pUnit = pWorld.subtract(ll).mult(1.0f / scale);
    return pUnit;
  }

  public float getScale() {
    Vector2f diagonal = ur.subtract(ll);
    float scale = (float) Math.min(getWidth(), getHeight()) / (float) Math.max(diagonal.x, diagonal.y);
    return scale;
  }

  /**
   * Unit coordinates -> world coordinates.
   */
  private Vector2f unit2World(Vector2f pUnit) {
    Vector2f diagonal = ur.subtract(ll);
    float scale = Math.max(diagonal.x, diagonal.y);
    return pUnit.mult(scale).add(ll);
  }

  /**
   * Unit coordinates -> pixel coordinates
   */
  private Vector2f unit2Pixel(Vector2f pUnit) {
    float scale = Math.min(getWidth(), getHeight());
    Vector2f offset = new Vector2f((getWidth() - scale) / 2.0f, (getHeight() - scale) / 2.0f);
    Vector2f pPixel = pUnit.mult(scale).add(offset);
    pPixel.y = getHeight() - pPixel.y;
    return pPixel;
  }

  /**
   * Pixel coordinates -> unit coordinates.
   */
  private Vector2f pixel2Unit(Vector2f pPixel) {
    float scale = Math.min(getWidth(), getHeight());
    Vector2f offset = new Vector2f((getWidth() - scale) / 2.0f, (getHeight() - scale) / 2.0f);
    Vector2f pUnit = new Vector2f(pPixel.x, getHeight() - pPixel.y);
    pUnit = pUnit.subtract(offset).mult(1.0f / scale);
    return pUnit;
  }

  public void clear(Graphics2D gc) {
    gc.setBackground(Color.WHITE);
    gc.clearRect(0, 0, getWidth(), getHeight());
  }

  public void drawImage(Graphics2D gc, Image image, int x, int y, int sizeX, int sizeY) {
    gc.drawImage(image, x, y, sizeX, sizeY, null);
  }

  /**
   * Draw the coordinate system axis
   */
  public void drawAxis(Graphics2D gc) {
    drawLine(gc, new Vector2f(ll.getX(), 0), new Vector2f(ur.getX(), 0), Color.BLACK);
    drawLine(gc, new Vector2f(ur.getX() - 0.1f, -0.05f), new Vector2f(ur.getX(), 0), Color.BLACK);
    drawLine(gc, new Vector2f(ur.getX() - 0.1f, 0.05f), new Vector2f(ur.getX(), 0), Color.BLACK);
    drawLine(gc, new Vector2f(0, ll.getY()), new Vector2f(0, ur.getX()), Color.BLACK);
    drawLine(gc, new Vector2f(-0.05f, ur.getY() - 0.1f), new Vector2f(0, ur.getY()), Color.BLACK);
    drawLine(gc, new Vector2f(0.05f, ur.getY() - 0.1f), new Vector2f(0, ur.getY()), Color.BLACK);
    for (int i = (int) ll.getX() + 1; i < (int) ur.getX(); i++) {
      if (i != 0) {
        drawAxisLabelX(gc, i);
      }
    }
    for (int i = (int) ll.getY() + 1; i < (int) ur.getY(); i++) {
      if (i != 0) {
        drawAxisLabelY(gc, i);
      }
    }
  }

  /**
   * Draws the axis label along the x-direction.
   */
  private void drawAxisLabelX(Graphics2D gc, int i) {
    drawLine(gc, new Vector2f(i, -0.05f), new Vector2f(i, 0.05f), Color.BLACK);
    drawText(gc, "" + i, new Vector2f(i - 0.05f, -0.3f), Color.BLACK);
  }

  /**
   * Draws the axis label along the y-direction.
   */
  private void drawAxisLabelY(Graphics2D gc, int i) {
    drawLine(gc, new Vector2f(-0.05f, i), new Vector2f(0.05f, i), Color.BLACK);
    drawText(gc, "" + i, new Vector2f(0.1f, i - 0.05f), Color.BLACK);
  }

  public Vector2f getLL() {
    return ll;
  }

  public Vector2f getUR() {
    return ur;
  }

  public void handleKey(String keyId) {
    // Default: ignore
  }
}
