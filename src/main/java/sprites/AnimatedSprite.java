/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package sprites;

import com.jme3.math.Vector2f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sprites.Constants.WALK_ANIMATION_IDS;

/**
 * Animated sprite rendering
 */
public class AnimatedSprite {

    private final int SPRITE_ANIMATION_SPEED = 1;

    /**
     * Current frame in the animation.
     */
    private int currentFrame;

    /**
     * Counts the number of draw calls, used to select the next frame.
     */
    private int renderCallCounter;

    /**
     * Contains the supported states and the image for each state.
     */
    private Map<String, List<BufferedImage>> animations;

    /**
     * Current animation id.
     */
    private String animationId;

    /**
     * Aspect ratio of the sprite.
     */
    private float aspect;

    public AnimatedSprite(float aspect) {
        this.renderCallCounter = 0;
        this.aspect = aspect;
        animations = new HashMap<>();
        animationId = WALK_ANIMATION_IDS[0];
    }

    public void add(String animationId, List<BufferedImage> frameImages) {
        animations.put(animationId, frameImages);
    }

    /**
     * Draw the current frame of the animation.
     */
    public void draw(Graphics gc, Vector2f pos) {
        if (!animations.containsKey(animationId)) {
            return;
        }

        List<BufferedImage> animationImages = animations.get(animationId);
        renderCallCounter++;
        if (renderCallCounter % SPRITE_ANIMATION_SPEED == 0) {
            currentFrame = (currentFrame + 1) % animationImages.size();
        }
        currentFrame = currentFrame % animationImages.size();
        int renderHeight = (int) (Constants.renderWidth / aspect);
        gc.drawImage(animationImages.get(currentFrame),
                (int) pos.x - Constants.renderWidth / 2, (int) pos.y - renderHeight / 2,
                Constants.renderWidth, renderHeight,
                null);
    }

    public void setAnimationId(String animationId) {
        this.animationId = animationId;
    }

    /**
     * Set the current walking animation
     */
    public void setAnimationId(Constants.WalkAnimations walkAnimation) {
        this.animationId = WALK_ANIMATION_IDS[walkAnimation.ordinal()];
    }
}
