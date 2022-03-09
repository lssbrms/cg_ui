/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package ui;

import shape3d.Ray3D;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 * This is the main application which is used for all exercises. Only adjust the
 * scene in the main method.
 */
public class ComputergraphicsJMEApp extends SimpleApplication {

  public static final String MOUSE_MOVE_RIGHT = "MOUSE_MOVE_RIGHT";
  public static final String MOUSE_MOVE_LEFT = "MOUSE_MOVE_LEFT";
  public static final String MOUSE_MOVE_UP = "MOUSE_MOVE_UP";
  public static final String MOUSE_MOVE_DOWN = "MOUSE_MOVE_DOWN";
  public static final String MOUSE_LEFT_BUTTON = "MOUSE_LEFT_BUTTON";
  public static final String MOUSE_RIGHT_BUTTON = "MOUSE_RIGHT_BUTTON";
  public static final String PICKING = "PICKING";
  public static final String KEY_SPACE = "KEY_SPACE";
  public static final String KEY_LEFT = "KEY_LEFT";
  public static final String KEY_RIGHT = "KEY_RIGHT";
  public static final String KEY_UP = "KEY_UP";
  public static final String KEY_DOWN = "KEY_DOWN";
  public static final String KEY_W = "KEY_W";
  public static final String KEY_S = "KEY_S";

  /**
   * Camera controller.
   */
  private AbstractCameraController cameraController;

  /**
   * Scene providing the content.
   */
  private Scene3D scene;

  /**
   * Mouse click state
   */
  private boolean mouseLeftPressed, mouseRightPressed, mouseMiddlePressed;

  private Runnable jMonkeyTask;

  private ColorRGBA backgroundColor = ColorRGBA.LightGray;

  public ComputergraphicsJMEApp() {
  }

  public void replaceScene(Scene3D scene) {
    jMonkeyTask = () -> {
      rootNode.detachAllChildren();
      scene.provideJMEApp(this);
      scene.init(assetManager, rootNode, cameraController);
      scene.setupLights(rootNode, viewPort);
      viewPort.setBackgroundColor(backgroundColor);
      this.scene = scene;
      System.err.println("scene replaced");
    };
  }

  @Override
  public void simpleInitApp() {
    setupInput();

    // Debug
    setDisplayFps(false);
    setDisplayStatView(false);

    viewPort.setBackgroundColor(backgroundColor);
  }

  /**
   * Register the handlers for the mouse and key input (also used for the camera
   * controller).
   */
  private void setupInput() {
    cam.setLocation(new Vector3f(-2, 2, 2));
    stateManager.detach(stateManager.getState(FlyCamAppState.class));
    cameraController = new ObserverCameraController(cam);
    cameraController.lookAt(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
    viewPort.setBackgroundColor(backgroundColor);
    inputManager.addMapping(MOUSE_MOVE_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping(MOUSE_MOVE_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping(MOUSE_MOVE_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
    inputManager.addMapping(MOUSE_MOVE_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    inputManager.addMapping(MOUSE_LEFT_BUTTON, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addMapping(MOUSE_RIGHT_BUTTON, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    inputManager.addMapping(PICKING, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addMapping(KEY_SPACE, new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping(KEY_LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
    inputManager.addMapping(KEY_RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
    inputManager.addMapping(KEY_UP, new KeyTrigger(KeyInput.KEY_UP));
    inputManager.addMapping(KEY_DOWN, new KeyTrigger(KeyInput.KEY_DOWN));
    inputManager.addMapping(KEY_W, new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping(KEY_S, new KeyTrigger(KeyInput.KEY_S));

    inputManager.addListener(analogListener, new String[]{
            MOUSE_MOVE_RIGHT, MOUSE_MOVE_LEFT, MOUSE_MOVE_UP, MOUSE_MOVE_DOWN});
    inputManager.addListener(actionListener, new String[]{MOUSE_LEFT_BUTTON, MOUSE_RIGHT_BUTTON, PICKING, KEY_SPACE,
            KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN, KEY_W, KEY_S});
  }

  /**
   * This listener is used for discrete input events.
   */
  private ActionListener actionListener = (name, keyPressed, tpf) -> {
    switch (name) {
      case MOUSE_LEFT_BUTTON -> {
        mouseLeftPressed = keyPressed;
        return;
      }
      case MOUSE_RIGHT_BUTTON -> {
        mouseRightPressed = keyPressed;
      }
      case PICKING -> {
        if (!keyPressed) {
          Vector2f click2d = inputManager.getCursorPosition().clone();
          Vector3f click3d = cam.getWorldCoordinates(
                  click2d, 0f).clone();
          Vector3f dir = cam.getWorldCoordinates(
                  click2d, 1f).subtractLocal(click3d).normalizeLocal();
          scene.handlePicking(new Ray(cam.getLocation(), dir));
        }
      }
      case KEY_SPACE, KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN, KEY_W, KEY_S -> {
        if (!keyPressed) {
          scene.handleKey(name);
        }
      }
    }
  };

  /**
   * This listener is used for continuous input events.
   */
  private AnalogListener analogListener = (name, value, tpf) -> {
    float rotateFactor = 200.0f;
    float zoomFactor = 800.0f;

    if (mouseLeftPressed || mouseRightPressed) {
      switch (name) {
        case MOUSE_MOVE_LEFT:
          if (mouseLeftPressed) {
            rotateHorizontal(-rotateFactor * value);
          }
          break;
        case MOUSE_MOVE_RIGHT:
          if (mouseLeftPressed) {
            rotateHorizontal(rotateFactor * value);
          }
          break;
        case MOUSE_MOVE_UP:
          if (mouseLeftPressed) {
            rotateVertical(rotateFactor * value);
          } else if (mouseRightPressed) {
            zoom(zoomFactor * value);
          }
          break;
        case MOUSE_MOVE_DOWN:
          if (mouseLeftPressed) {
            rotateVertical(-rotateFactor * value);
          } else if (mouseRightPressed) {
            zoom(-zoomFactor * value);
          }
          break;
      }
    } else {
      // no mouse button pressed
      if (name.equals(MOUSE_MOVE_LEFT) || name.equals(MOUSE_MOVE_RIGHT) || name.equals(MOUSE_MOVE_UP) || name.equals(MOUSE_MOVE_DOWN)) {
        Vector2f click2d = inputManager.getCursorPosition().clone();
        Vector3f click3d = cam.getWorldCoordinates(
                click2d, 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
                click2d, 1f).subtractLocal(click3d).normalizeLocal();
        if (scene != null) {
          scene.handleMouseMove(click2d, new Ray3D(cam.getLocation(), dir));
        }
      }
    }
  };

  @Override
  public void simpleUpdate(float tpf) {
    if (scene != null) {
      scene.invokeRunlaterTasks();
      scene.update(tpf);
//    if (scene.hasNewCameraController()) {
//      ObserverCameraController cameraController = scene.getCameraController(cam);
//      if (cameraController != null) {
//        Logger.getInstance().msg("Switched to camera controller " + cameraController);
//        this.cameraController = cameraController;
//      }
//    }
    }
    cameraController.update();
  }

  @Override
  public void simpleRender(RenderManager rm) {
    if (jMonkeyTask != null) {
      jMonkeyTask.run();
      jMonkeyTask = null;
    }
    if (scene != null) {
      scene.render();
    }
  }

  public void zoom(float delta) {
    cameraController.zoom(delta);
  }

  public void rotateHorizontal(float delta) {
    cameraController.rotateAroundUp(delta);
  }

  public void rotateVertical(float delta) {
    cameraController.rotateAroundLeft(delta);
  }

  public Scene3D getScene() {
    return scene;
  }

  public void setCameraController(AbstractCameraController cameraController) {
    this.cameraController = cameraController;
  }

//  jmePanel.addFocusListener(new
//  FocusAdapter() {
//    @Override
//    public void focusLost (FocusEvent e){
//      onFocusLost(e);
//    }
//  });
//
//  private void onFocusLost(FocusEvent e) {
//    if (e.isTemporary()) {
//      return;
//    }
//    // if this component looses its focus, the JME app will continue to fire input events,
//    // even if the keys are no longer pressed.
//    // FIXME: this is not a real solution, just a hacky work-around
//    app.getInputManager().reset();
//  }
}