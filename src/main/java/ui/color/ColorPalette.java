package ui.color;

import com.jme3.math.ColorRGBA;

import java.awt.*;

public class ColorPalette {
  public static ColorRGBA COLOR_1 = new ColorRGBA(244.0f / 255.0f, 241.0f / 255.0f, 222.0f / 255.0f, 1);
  public static ColorRGBA COLOR_2 = new ColorRGBA(244.0f / 255.0f, 122 / 255.0f, 95 / 255.0f, 1);
  public static ColorRGBA COLOR_3 = new ColorRGBA(61 / 255.0f, 64 / 255.0f, 91 / 255.0f, 1);
  public static ColorRGBA COLOR_4 = new ColorRGBA(129 / 255.0f, 178 / 255.0f, 154 / 255.0f, 1);
  public static ColorRGBA COLOR_5 = new ColorRGBA(242 / 255.0f, 204 / 255.0f, 143 / 255.0f, 1);

  public static Color toAwtColor(ColorRGBA color) {
    return new Color(color.r, color.g, color.b, color.a);
  }
}
