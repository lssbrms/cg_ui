/**
 * Diese Datei ist Teil des Vorgabeframeworks zur Lehrveranstaltung Einführung in die Computergrafik
 * an der Hochschule für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package ui;

import datastructures.shape3d.Ray3D;
import com.jme3.asset.AssetManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This scene class is used to represent the scene conent.
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

  public Scene3D() {
    runLaterTasks = new ArrayList<>();
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

  public void setupLights(Node rootNode) {
  }

  /**
   * Provide the JME application object to the scene (if it needs it).
   */
  public void provideJMEApp(ComputergraphicsJMEApp app) {
    // usually not required
  }
}
