package ui;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * This is a rather simple dummy scene which can be used for testing.
 */
public class SimpleScene3D extends Scene3D {
    @Override
    public void init(AssetManager assetManager, Node rootNode, AbstractCameraController cameraController) {
        runLater(() -> {
            Box b = new Box(1, 1, 1);
            Geometry geom = new Geometry("Box", b);
            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            geom.setMaterial(mat);
            rootNode.attachChild(geom);
            cameraController.setup(new Vector3f(-3, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
        });
    }

    @Override
    public void update(float time) {

    }

    @Override
    public void render() {

    }

    @Override
    public String getTitle() {
        return null;
    }
}
