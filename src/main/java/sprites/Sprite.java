package sprites;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import math.Matrix2f;
import math.Vectors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Load and display a sprite image
 */
public class Sprite {

    /**
     * Image with the sprite.
     */
    private BufferedImage image;

    /**
     * Size of sprite on screen.
     */
    private int renderWidth, renderHeight;

    public Sprite(String filename, int renderWidth, int renderHeight) {
        load(filename);
        this.renderWidth = renderWidth;
        this.renderHeight = renderHeight;
    }

    /**
     * Load sprite image from file.
     */
    private void load(String filename) {
        try {
            String path = "src/main/resources/" + filename;
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Error loading sprite image from " + filename
                    + ": " + e.getMessage());
        }
    }

    /**
     * Draw the sprite using the given homogenious transformation matrix.
     */
    public void draw(Graphics gc, Matrix2f rotation, Vector2f pos) {
        float[] flatMatrix = {rotation.get(0, 0), rotation.get(0, 1),
                rotation.get(1, 0), rotation.get(1, 1),
                renderWidth / 2.0f, renderHeight / 2.0f};
        AffineTransform R = new AffineTransform(flatMatrix);
        /*AffineTransform R = AffineTransform.getRotateInstance(
                Math.acos(-rotation.get(0, 0)),
                renderWidth / 2.0f,
                renderHeight / 2.0f);*/
        AffineTransformOp op = new AffineTransformOp(R, AffineTransformOp.TYPE_BICUBIC);
        gc.drawImage(op.filter(image, null),
                (int) (pos.x - renderWidth / 2), (int) (pos.y - renderHeight / 2),
                renderWidth, renderHeight,
                null);
    }

}
