package ui;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import projects.urban_mobility.frontend.scene.Materials;

/**
 * A utility class for creating geometries useful in debugging directional/positional vectors.
 */
public final class JmeDebug {
  private JmeDebug() {
  }

  /**
   * Creates an arrow at {@code pos} pointing along {@code dir}.
   * <p>
   * The returned geometry is not yet attached to the scene graph.
   */
  public static Geometry visualizeDirection(Vector3f pos, Vector3f dir, ColorRGBA color, AssetManager am) {
    var geometry = new Geometry("DEBUG_ARROW", new Arrow(dir));
    geometry.setMaterial(Materials.createUnshaded(am, color));
    geometry.setLocalTranslation(pos);
    return geometry;
  }

  /**
   * Creates a small sphere at {@code pos}.
   * <p>
   * The returned geometry is not yet attached to the scene graph.
   */
  public static Geometry visualizePosition(Vector3f pos, ColorRGBA color, AssetManager am) {
    var geometry = new Geometry("DEBUG_SPHERE", new Sphere(8, 8, .1f));
    geometry.setMaterial(Materials.createUnshaded(am, color));
    geometry.setLocalTranslation(pos);
    return geometry;
  }
}
