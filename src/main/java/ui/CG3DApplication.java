package ui;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import misc.Logger;
import shape3d.Ray3D;

/**
 * JME application to handle a 3D scene.
 */
public class CG3DApplication extends SimpleApplication {

    /**
     * This is the scene to be rendered
     */
    private Scene3D scene3D;

    /**
     * Camera controller.
     */
    private AbstractCameraController cameraController;

    private ColorRGBA backgroundColor = ColorRGBA.LightGray;

    private Runnable jMonkeyTask;

    private boolean sceneIsInitialized = false;

    public CG3DApplication() {
    }

    protected void setScene3D(Scene3D scene3D) {
        this.scene3D = scene3D;
        sceneIsInitialized = false;
    }

    public void simpleInitApp() {
        setupInput();
        setDisplayFps(false);
        setDisplayStatView(false);
        viewPort.setBackgroundColor(backgroundColor);
    }

    public void simpleUpdate(float tpf) {
        if (scene3D != null && sceneIsInitialized) {
            scene3D.invokeRunlaterTasks();
            scene3D.update(tpf);
            if (scene3D.hasNewCameraController()) {
                AbstractCameraController cameraController = scene3D.getAndResetNewCameraController();
                if (cameraController != null) {
                    Logger.getInstance().msg("Switched to camera controller " + cameraController);
                    this.cameraController = cameraController;
                }
            }
            cameraController.update();
        }
    }

    public void simpleRender(RenderManager rm) {
        if (jMonkeyTask != null) {
            jMonkeyTask.run();
            jMonkeyTask = null;
        }
        if (scene3D != null) {
            if (!sceneIsInitialized) {
                // First rendering of the scene -> initialize
                scene3D.init(assetManager, rootNode, cameraController);
                scene3D.setupLights(rootNode, viewPort);
                sceneIsInitialized = true;
            }
            scene3D.invokeRunlaterTasks();
            scene3D.render();
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

    /**
     * Mouse click state
     */
    private boolean mouseLeftPressed, mouseRightPressed;

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
        inputManager.addMapping(KEY_1, new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping(KEY_2, new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping(KEY_3, new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping(KEY_4, new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping(KEY_A, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(KEY_B, new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping(KEY_C, new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping(KEY_S, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(KEY_W, new KeyTrigger(KeyInput.KEY_W));

        inputManager.addListener(analogListener, new String[]{
                MOUSE_MOVE_RIGHT, MOUSE_MOVE_LEFT, MOUSE_MOVE_UP, MOUSE_MOVE_DOWN});
        inputManager.addListener(actionListener, new String[]{MOUSE_LEFT_BUTTON,
                MOUSE_RIGHT_BUTTON, PICKING, KEY_SPACE, KEY_LEFT, KEY_RIGHT,
                KEY_UP, KEY_DOWN, KEY_1, KEY_2, KEY_3, KEY_4, KEY_A, KEY_B, KEY_C,
                KEY_S, KEY_W});
    }

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
    public static final String KEY_A = "KEY_A";
    public static final String KEY_B = "KEY_B";
    public static final String KEY_C = "KEY_C";
    public static final String KEY_1 = "KEY_1";
    public static final String KEY_2 = "KEY_2";
    public static final String KEY_3 = "KEY_3";
    public static final String KEY_4 = "KEY_4";

    /**
     * This listener is used for discrete input events.
     */
    private ActionListener actionListener = (name, keyPressed, tpf) -> {
        switch (name) {
            case MOUSE_LEFT_BUTTON -> {
                mouseLeftPressed = keyPressed;
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
                    scene3D.handlePicking(new Ray(cam.getLocation(), dir));
                }
            }
            case KEY_SPACE, KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN, KEY_1, KEY_2,
                    KEY_3, KEY_4, KEY_A, KEY_B, KEY_C, KEY_S, KEY_W -> {
                if (!keyPressed) {
                    scene3D.handleKey(name);
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
                if (scene3D != null) {
                    scene3D.handleMouseMove(click2d, new Ray3D(cam.getLocation(), dir));
                }
            }
        }
    };
}
