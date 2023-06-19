/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package ui;

import base.ModelViewer;
import base.ParameterEditor;
import com.google.common.base.Preconditions;
import com.jme3.system.AppSettings;
import misc.Logger;

import javax.swing.*;

public class GenericCGApplication {

    private boolean defaultContentIsInUse;

    private ComputergraphicsWidget computergraphicsWidget;
    ComputergraphicsJMEApp app;

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    public GenericCGApplication(String title) {
        this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
    }

    public GenericCGApplication(String title, Scene3D scene3D) {
        this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT, scene3D);
    }

    public GenericCGApplication(String title, int width, int height) {
        this(title, width, height, null);
    }

    public GenericCGApplication(String title, int width, int height, Scene3D scene3D) {
        this.app = new ComputergraphicsJMEApp();
        this.defaultContentIsInUse = false;
        this.computergraphicsWidget = new ComputergraphicsWidget(title, width, height);
        if (scene3D != null) {
            this.app.enqueue(() -> init3D(scene3D));
        }

        AppSettings appSettings = new AppSettings(true);
        appSettings.setTitle("Generic CG Application");
        appSettings.setResolution(800, 600);
        appSettings.setFullscreen(false);
        appSettings.setAudioRenderer(null);
        app.setSettings(appSettings);
        app.setShowSettings(false);
        app.setDisplayStatView(false);
        app.setShowSettings(false);

        this.app.start();
    }

    private void init3D(Scene3D scene3D) {
        while (app.getViewPort() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setScene3D(scene3D);
        app.getFlyByCamera().setDragToRotate(true);
    }

    protected void setScene3D(Scene3D scene3D) {
        Preconditions.checkNotNull(scene3D);
        app.replaceScene(scene3D);
        if (!defaultContentIsInUse) {
            // Default currently unused.
            //mainTabbedPane.add(scene3D.getTitle(), defaultJMonkeyContent);
            defaultContentIsInUse = true;
        }

        // TODO: Update ui
        /*
        getContentPane().invalidate();
        for (StatusBar.StatusBarItem statusBarItem : scene3D.getStatusBarItems()) {
            statusBar.add(statusBarItem);
        }
        */
    }

    protected void addScene3D(Scene3D scene3D) {
        Logger.getInstance().error("addScene3D(): use setScene3D() instead. This method will soon be deprecated.");
        setScene3D(scene3D);
    }

    protected void addScene2D(Scene2D scene2D) {
        computergraphicsWidget.addScene2D(scene2D);
    }

    protected void addPanel(JPanel panel, String title) {
        computergraphicsWidget.addPanel(panel, title);
    }

    protected void removeAllScenes() {
        computergraphicsWidget.removeAllScenes();
        defaultContentIsInUse = false;
    }

    /**
     * Add editors and viewers
     */
    public void setup(ParameterEditor parameterEditor, ModelViewer modelViewer,
                      Scene3D sceneViewer) {
        if (parameterEditor != null) {
            addPanel(parameterEditor, "Model Editor");
        }
        if (modelViewer != null) {
            addPanel(modelViewer, "Model Viewer");
        }
        if (sceneViewer != null) {
            setScene3D(sceneViewer);
        }
    }

    protected StatusBar getStatusBar() {
        return computergraphicsWidget.getStatusBar();
    }
}
