package ui;

import datastructures.AxisAlignedBoundingBox;
import math.Angles;
import math.MathF;
import misc.Logger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import static projects.urban_mobility.backend.UrbanMobilityWorld.METER_IN_UNITS;

public class DroneCameraController extends AbstractCameraController {
  // TODO: use constant vertical angle to get from radius to height

  private static final float MOVE_SPEED = 100 * METER_IN_UNITS;
  private static final float ROTATE_SPEED = MathF.toRadians(180);
  private static final float ZOOM_SPEED = 50 * METER_IN_UNITS;
  private static final float DEFAULT_HEIGHT = 50 * METER_IN_UNITS;

  private Vector3f reference = new Vector3f();
  private float height = DEFAULT_HEIGHT;
  private float radius = 60 * METER_IN_UNITS;
  private float angle = 0; // in radians

  public DroneCameraController(Camera cam, Vector3f reference) {
    super(cam);
    setReference(reference);
  }

  @Override
  public void lookAt(Vector3f ref) {
    setReference(ref);
  }

  @Override
  public void lookAt(Vector3f ref, Vector3f up) {
    setReference(ref);
  }

  @Override
  public void moveForwards(float value) {
    move(value, false);
  }

  @Override
  public void moveLeft(float value) {
    move(value, true);
  }

  private void move(float value, boolean sideways) {
    // TODO: decouple movement direction from camera.getLeft()/getDirection()
    var step = (sideways ? cam.getLeft() : cam.getDirection()).clone();
    step.setY(0).normalizeLocal();
    step.multLocal(value * (MOVE_SPEED * height / DEFAULT_HEIGHT));
    setReference(reference.add(step));
  }

  @Override
  public void rotateAroundLeft(float sign) {
    // not-supported
  }

  @Override
  public void rotateAroundUp(float sign) {
    var newAngle = angle - (sign * ROTATE_SPEED);
    setAngle(newAngle);
  }

  @Override
  public void zoom(float zoom) {
    radius -= zoom * ZOOM_SPEED;
    height -= zoom * ZOOM_SPEED;
    sync();
  }

  @Override
  public void update() {
  }

  public void setReference(Vector3f reference) {
    this.reference = reference.clone();
    sync();
  }

  public void setAngle(float angle) {
    this.angle = Angles.normalizeAngle(angle);
    sync();
  }

  private void sync() {
    // TODO: use a sequence of translation and rotation rather
    var offset = new Vector3f(radius * MathF.cos(angle), height, radius * MathF.sin(angle));
    var position = reference.add(offset);
    cam.setLocation(position);
    cam.lookAt(reference, Vector3f.UNIT_Y);
  }

  @Override
  public void adjustViewTo(AxisAlignedBoundingBox bbox) {
    Logger.getInstance().error("InCarCameraController::adjustViewTo() not implemented.");
  }

  @Override
  public void setup(Vector3f add, Vector3f ref, Vector3f unitY) {
    throw new IllegalArgumentException("unsupported operation");
  }
}
