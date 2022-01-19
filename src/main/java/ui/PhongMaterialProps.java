package ui;

import com.jme3.math.ColorRGBA;

public class PhongMaterialProps {
  public ColorRGBA ambientColor;
  public ColorRGBA diffuseColor;
  public ColorRGBA specularColor;
  public float shininess;

  public PhongMaterialProps(ColorRGBA color) {
    this.ambientColor = ColorRGBA.DarkGray;
    this.diffuseColor = color;
    this.specularColor = ColorRGBA.DarkGray;
    this.shininess = 20.0f;
  }

  public PhongMaterialProps() {
    this.ambientColor = ColorRGBA.DarkGray;
    this.diffuseColor = ColorRGBA.White;
    this.specularColor = ColorRGBA.DarkGray;
    this.shininess = 20.0f;
  }

  public PhongMaterialProps(ColorRGBA ambientColor, ColorRGBA diffuseColor, ColorRGBA specularColor, float shininess) {
    this.ambientColor = ambientColor;
    this.diffuseColor = diffuseColor;
    this.specularColor = specularColor;
    this.shininess = shininess;
  }
}
