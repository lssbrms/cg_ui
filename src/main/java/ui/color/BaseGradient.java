/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package ui.color;

import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A gradient maps from an continuous key in the interval [0, 1]
 * to values at predefined positions in this interval.
 * Values for keys which do not equal said predefined position are linearly interpolated
 * between the values at the next lesser and greater predefined positions.
 * <p>
 * This class is the base class for all gradients. For any value type there will be one subclass
 * with the template parameter {@code T} set to that value type. The reason subclasses are necessary is that
 * interpolation is dependent on the value type.
 *
 * @param <T> the type of the values
 */
public abstract class BaseGradient<T> {
  private final NavigableMap<Float, T> values = new TreeMap<>();

  /**
   * Creates a new empty gradient.
   */
  public BaseGradient() {
  }

  /**
   * Creates a new gradient, copying the entries from a given map.
   *
   * @param map must be non-null, all keys must be within [0, 1], all values must not be null
   */
  public BaseGradient(Map<Float, T> map) {
    addAll(map);
  }

  private static void validatePosition(float pos) {
    Validate.inclusiveBetween(0f, 1f, pos, "position must be in [0, 1]");
  }

  /**
   * Adds or replaces a value at a predefined position.
   *
   * @param pos   the position that maps to the value, must be within [0, 1]
   * @param value the value to add to the mapping, must not be null
   */
  public void add(float pos, T value) {
    Validate.notNull(value, "value must not be null");
    validatePosition(pos);
    values.put(pos, value);
  }

  /**
   * Adds all entries from a given map.
   *
   * @param map must be non-null, all keys must be within [0, 1], all values must not be null
   */
  public void addAll(Map<Float, T> map) {
    map.forEach(this::add);
  }

  /**
   * Removes a predefined position and the value mapped to.
   *
   * @param pos the position, must be within [0, 1]
   */
  public void remove(float pos) {
    validatePosition(pos);
    values.remove(pos);
  }

  /**
   * Gets the value mapped to at a predefined position, or if no such value exists,
   * interpolates between the values at the next two predefined positions.
   *
   * @param pos the position to get the value from
   * @return the mapped or interpolated value, always non-null
   */
  public T get(float pos) {
    validatePosition(pos);
    var minPos = values.floorKey(pos);
    var maxPos = values.ceilingKey(pos);

    // check if either minPos or maxPos is null
    if (maxPos != null && minPos == null) {
      return values.get(maxPos);
    }
    if (minPos != null && maxPos == null) {
      return values.get(minPos);
    }
    // check if both are null
    if (minPos == null) {
      return null;
    }

    // skip interpolation when both pos are equal
    var delta = maxPos - minPos;
    if (delta == 0) {
      return values.get(minPos);
    }

    // interpolate between minPos and maxPos
    var i = (pos - minPos) / delta;
    var minPosVal = values.get(minPos);
    var maxPosVal = values.get(maxPos);
    return interpolate(minPosVal, maxPosVal, i);
  }

  /**
   * Linearly interpolates between two values.
   *
   * @param a the first value, must not be null
   * @param b the second value, must not be null
   * @param i the interpolation parameter, must be within [0, 1]
   * @return the interpolated value, always non-null
   */
  protected abstract T interpolate(T a, T b, float i);
}
