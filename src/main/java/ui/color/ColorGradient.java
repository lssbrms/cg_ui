/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package ui.color;

import com.jme3.math.ColorRGBA;

import java.util.Map;

/**
 * The {@link BaseGradient} implementation for {@link ColorRGBA}.
 * <p>
 *
 * @implNote Interpolation between colors are calculated in the linear RGB color space for performance reasons.
 * For more predictable results, interpolation in a perceptually uniform color space may be considered.
 */
public class ColorGradient extends BaseGradient<ColorRGBA> {
  /**
   * Creates a new color gradient.
   */
  public ColorGradient() {
  }

  /**
   * Creates a new gradient, copying the entries from a given map.
   *
   * @param map must be non-null, all keys must be within [0, 1], all values must not be null
   */
  public ColorGradient(Map<Float, ColorRGBA> map) {
    super(map);
  }

  @Override
  protected ColorRGBA interpolate(ColorRGBA a, ColorRGBA b, float i) {
    return a.clone().interpolateLocal(b, i);
  }
}
