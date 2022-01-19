package ui;

import misc.AxisAlignedBoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * This is the base class for all camera controllers.
 */
public abstract class AbstractCameraController {
  /**
   * Camera opening angle in degrees.
   */
  protected static final float FOV_Y = 90;

  /**
   * Reference to the jMonkey camera
   */
  protected final Camera cam;

  protected AbstractCameraController(Camera cam) {
    this.cam = cam;
    float near = 0.1f;
    float far = 30.0f;
    cam.setFrustumPerspective(
            FOV_Y,
            cam.getWidth() / (float) cam.getHeight(),
            near,
            far);
  }

  /**
   * Set the camera eye point.
   */
  public void setEye(Vector3f eye) {
    cam.setLocation(eye);
  }

  /**
   * Camera looks at this reference point.
   */
  public abstract void lookAt(Vector3f ref);

  /**
   * Camera looks at this reference point.
   */
  public abstract void lookAt(Vector3f ref, Vector3f up);

  /**
   * Move the camera forward (when value positive) or backwards (when value negative).
   */
  public abstract void moveForwards(float value);

  /**
   * Move the camera to the left (when value positive) or to the right (when value negative).
   */
  public abstract void moveLeft(float value);

  /**
   * Rotate the camera around a vector perpendicular to ref and up, centered at the ref point.
   */
  public abstract void rotateAroundLeft(float sign);

  /**
   * Rotate the camera around the up vector centered at the ref point.
   */
  public abstract void rotateAroundUp(float sign);

  /**
   * Move to closer to/further away from the reference point.
   */
  public abstract void zoom(float zoom);

  /**
   * Update the jMonkey camera based on the camera controller state.
   */
  public abstract void update();

  /**
   * Adjust the current view to match this bounding box.
   */
  public abstract void adjustViewTo(AxisAlignedBoundingBox bbox);

  /**
   * Gets the controlled camera.
   */
  public Camera getCamera() {
    return cam;
  }

  /**
   * Set the camera coordinate system using eye, ref, up
   */
  public void setup(Vector3f eye, Vector3f ref, Vector3f up) {
    getCamera().setLocation(eye);
    getCamera().lookAt(ref, up);
  }

}
