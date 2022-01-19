package ui;

import datastructures.AxisAlignedBoundingBox;
import math.MathF;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * This camera controller enables the used to rotate the camera around the scene und move closer and further away.
 */
public class ObserverCameraController extends AbstractCameraController {

  private static final float ROTATE_SPEED = MathF.toRadians(5);
  private static final float ZOOM_SPEED = 0.01f;

  /**
   * Reference point where the camera looks to
   */
  private Vector3f ref;

  public ObserverCameraController(Camera cam, Vector3f pos, Vector3f ref) {
    super(cam);
    this.ref = new Vector3f(ref);
    cam.setLocation(pos);
    cam.lookAt(ref, Vector3f.UNIT_Y);
  }

  public ObserverCameraController(Camera cam) {
    this(cam, new Vector3f(0, 0, -1), new Vector3f(0, 0, 0));
  }

  /**
   * Rotate the camera around the up vector.
   */
  public void rotateAroundUp(float sign) {
    Vector3f eye = cam.getLocation();
    Vector3f dir = ref.subtract(eye);
    Matrix3f m = new Matrix3f();
    m.fromAngleAxis(ROTATE_SPEED * sign, cam.getUp());
    Vector3f newDir = m.mult(dir);
    Vector3f newEye = ref.subtract(newDir);
    cam.setLocation(newEye);
    cam.lookAt(ref, cam.getUp());
  }

  /**
   * Rotate the camera around the left vector.
   */
  public void rotateAroundLeft(float sign) {
    Vector3f eye = cam.getLocation();
    Vector3f dir = ref.subtract(eye);
    Matrix3f m = new Matrix3f();
    m.fromAngleAxis(ROTATE_SPEED * sign, cam.getLeft());
    Vector3f newDir = m.mult(dir);
    Vector3f newEye = ref.subtract(newDir);
    cam.setLocation(newEye);
    cam.lookAt(ref, cam.getUp());
  }

  /**
   * Zoom in (positive zoom value) or out (negative zoom value)
   */
  public void zoom(float zoom) {
    Vector3f eye = cam.getLocation();
    Vector3f dir = ref.subtract(eye);
    Vector3f newEye = eye.add(dir.mult(zoom * ZOOM_SPEED));
    cam.setLocation(newEye);
    cam.lookAt(ref, cam.getUp());
  }

  @Override
  public void update() {
  }

  @Override
  public void adjustViewTo(AxisAlignedBoundingBox bbox) {
    if (bbox == null) {
      return;
    }

    Vector3f dir = bbox.getCenter().subtract(getCamera().getLocation()).normalize();
    float bboxDiameter = bbox.getExtent().length();
    float distScale = 0.7f;
    dir = dir.mult(bboxDiameter * distScale);
    lookAt(bbox.getCenter());
    getCamera().setLocation(bbox.getCenter().subtract(dir));
    getCamera().update();
  }

  public void lookAt(Vector3f center) {
    cam.lookAt(ref, cam.getUp());
    this.ref = new Vector3f(center);
  }

  @Override
  public void lookAt(Vector3f ref, Vector3f up) {
    cam.lookAt(ref, up);
    this.ref = new Vector3f(ref);
  }

  @Override
  public void moveForwards(float value) {
  }

  @Override
  public void moveLeft(float value) {
  }

  @Override
  public void setup(Vector3f eye, Vector3f ref, Vector3f up) {
    super.setup(eye, ref, up);
    this.ref = new Vector3f(ref);
  }

}
