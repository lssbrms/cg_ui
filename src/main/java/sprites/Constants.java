/**
 * Diese Datei ist Teil des Vorgabeframeworks zur Lehrveranstaltung Einführung in die Computergrafik
 * an der Hochschule für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package sprites;

/**
 * Constants used for the sprite rendering
 */
public class Constants {
  /**
   * Drawing framerate
   */
  public static final int RENDER_FPS = 6;

  /**
   * Movement speed of the sprite.
   */
  public static final float SPEED = 5f;

  /**
   * Width of the sprites on screen
   */
  public static int renderWidth = 75;

  /**
   * Animation ids, ordered clockwise, starting at south.
   */
  public static final String[] WALK_ANIMATION_IDS = {"WALK_S", "WALK_SW", "WALK_W", "WALK_NW", "WALK_N",
          "WALK_NE", "WALK_E", "WALK_SE"};
}
