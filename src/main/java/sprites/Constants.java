/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
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
     * Constants for the walk animations corresponding to the String array
     * WALK_ANIMATIONS.
     */
    public enum WalkAnimations {
        WALK_S, WALK_SW, WALK_W, WALK_NW, WALK_N, WALK_NE, WALK_E, WALK_SE
    }

    /**
     * Animation ids, ordered clockwise, starting at south.
     */
    public static final String[] WALK_ANIMATION_IDS = {"WALK_S", "WALK_SW", "WALK_W", "WALK_NW", "WALK_N",
            "WALK_NE", "WALK_E", "WALK_SE"};
}
