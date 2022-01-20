package test;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import misc.AxisAlignedBoundingBox;
import ui.AbstractCameraController;
import ui.Scene3D;

public class TestScene3D extends Scene3D {
  @Override
  public void init(AssetManager assetManager, Node rootNode, AbstractCameraController cameraController) {
    Mesh mesh = new Box(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.5f, 0.5f, 0.5f));
    Geometry geometry = new Geometry("Mesh", mesh);
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setTexture("ColorMap", assetManager.loadTexture("textures/sweets.jpg"));
    geometry.setMaterial(mat);
    rootNode.attachChild(geometry);
    AxisAlignedBoundingBox bbox = new AxisAlignedBoundingBox();
    bbox.add(new Vector3f(-0.5f, -0.5f, -0.5f));
    bbox.add(new Vector3f(0.5f, 0.5f, 0.5f));
    cameraController.adjustViewTo(bbox);
  }

  @Override
  public void setupLights(Node rootNode) {
    // Sun
    DirectionalLight sun = new DirectionalLight();
    sun.setColor(new ColorRGBA(1, 1, 1, 1));
    sun.setDirection(new Vector3f(0.25f, -1, 0.1f).normalize());
    rootNode.addLight(sun);
  }

  @Override
  public void update(float time) {

  }

  @Override
  public void render() {

  }

  @Override
  public String getTitle() {
    return "Scene3D";
  }
}
