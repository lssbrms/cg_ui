/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package ui;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import shape3d.Ray3D;
import com.jme3.asset.AssetManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This scene class is used to represent the scene content.
 */
public abstract class Scene3D {

  public static final String MSG_SET_CAMERA_CONTROLLER = "MSG_SET_CAMERA_CONTROLLER";

  public List<StatusBar.StatusBarItem> getStatusBarItems() {
    return new ArrayList<>();
  }

  /**
   * This list of tasks is scheduled to be invoked by the JME thread.
   */
  private List<Runnable> runLaterTasks;

  /**
   * This flag indicates that the scene wants to set the new camera controller in newCameraController
   */
  private boolean hasNewCameraController;

  /**
   * This camera controller wants to be used by the jme app.
   */
  private AbstractCameraController newCameraController;

  public Scene3D() {
    runLaterTasks = new ArrayList<>();
    hasNewCameraController = false;
    newCameraController = null;
  }

  /**
   * Returns the user interface for the scene
   *
   * @return null, if the scene has no user interface
   */
  public JPanel getUserInterface() {
    // Default: none
    return null;
  }

  /**
   * The init method is called once at the beginning of the runtime.
   */
  public abstract void init(AssetManager assetManager, Node rootNode, AbstractCameraController cameraController);

  /**
   * The update method is used to update the simulation state.
   */
  public abstract void update(float time);

  /**
   * This method is called once if the scene is rerendered.
   */
  public abstract void render();

  /**
   * Return the (descriptive) title of the scene.
   */
  public abstract String getTitle();

  /**
   * Enqueue a task to the list which will be processed when the jMonkey Thread is active.
   */
  protected synchronized void runLater(Runnable task) {
    runLaterTasks.add(task);
  }

  public synchronized void invokeRunlaterTasks() {
    runLaterTasks.forEach(task -> task.run());
    runLaterTasks.clear();
  }

  /**
   * Handle a picking event using the given ray.
   */
  public void handlePicking(Ray pickingRay) {
    // Default: ignore
  }

  /**
   * The mouse was moved over the window and no button is clicked.
   */
  public void handleMouseMove(Vector2f click2d, Ray3D ray) {
    // Default: ignore
  }

  /**
   * Load an animated character node.
   *
   * @return Scene graph node with the animated character
   */
  protected Node loadCharacter(AssetManager assetManager, Node rootNode,
                               String gltfFilename) {
    Node node = (Node) assetManager.loadModel(gltfFilename);
    node = (Node) node.getChild("knight");
    node.setShadowMode(RenderQueue.ShadowMode.Cast);
    rootNode.attachChild(node);
    return node;
  }

  public void handleKey(String keyId) {
    // Default: ignore
  }

  /**
   * This is the default lighting - can be altered in inheriting scene classes.
   */
  public void setupLights(Node rootNode, ViewPort viewPort) {
    // Clear lights
    for (Light light : rootNode.getLocalLightList()) {
      rootNode.removeLight(light);
    }
    for (Light light : rootNode.getWorldLightList()) {
      rootNode.removeLight(light);
    }

    // Sun
    DirectionalLight sun = new DirectionalLight();
    sun.setColor(ColorRGBA.White);
    sun.setDirection(new Vector3f(0.25f, -1, 0.1f));
    rootNode.addLight(sun);

    AmbientLight ambientLight = new AmbientLight();
    ColorRGBA darkAmbientColor = ColorRGBA.DarkGray;
    ambientLight.setColor(darkAmbientColor);
    rootNode.addLight(ambientLight);
  }

  /**
   * Provide the JME application object to the scene (if it needs it).
   */
  public void provideJMEApp(ComputergraphicsJMEApp app) {
    // usually not required
  }

  public void setCameraController(AbstractCameraController cameraController) {
    this.newCameraController = cameraController;
    hasNewCameraController = true;
  }

  /**
   * Returns the the camera controller and resets the hasNewCameraController flag.
   */
  public AbstractCameraController getAndResetNewCameraController() {
    hasNewCameraController = false;
    AbstractCameraController cameraController = newCameraController;
    newCameraController = null;
    return cameraController;
  }

  public boolean hasNewCameraController() {
    return hasNewCameraController;
  }
}
