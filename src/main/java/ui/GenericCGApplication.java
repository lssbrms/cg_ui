package ui;

import base.ModelViewer;
import base.ParameterEditor;
import com.google.common.base.Preconditions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericCGApplication extends JFrame {

  private JTabbedPane mainTabbedPane;
  private JMonkeyContent defaultJMonkeyContent;
  private boolean defaultContentIsInUse;
  private final StatusBar statusBar;

  private static final int DEFAULT_WIDTH = 800;
  private static final int DEFAULT_HEIGHT = 600;

  public GenericCGApplication(String title) {
    this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  public GenericCGApplication(String title, int width, int height) {
    super(title);
    this.defaultJMonkeyContent = new JMonkeyContent(width, height);
    this.defaultContentIsInUse = false;

    // Layout
    getContentPane().setLayout(new BorderLayout());
    mainTabbedPane = new JTabbedPane();
    getContentPane().add(mainTabbedPane, BorderLayout.CENTER);

    statusBar = new StatusBar();
    getContentPane().add(statusBar, BorderLayout.SOUTH);

    setSize(width, height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }

  protected void setScene3D(Scene3D scene3D) {
    Preconditions.checkNotNull(scene3D);
    defaultJMonkeyContent.replaceScene(scene3D);
    if (!defaultContentIsInUse) {
      // Default currently unused.
      mainTabbedPane.add(scene3D.getTitle(), defaultJMonkeyContent);
      defaultContentIsInUse = true;
    }
    getContentPane().invalidate();
  }

  protected void addScene3D(Scene3D scene3D) {
    Preconditions.checkNotNull(scene3D);
    if (!defaultContentIsInUse) {
      // Reuse default content scene
      defaultJMonkeyContent.replaceScene(scene3D);
      mainTabbedPane.add(scene3D.getTitle(), defaultJMonkeyContent);
      defaultContentIsInUse = true;
    } else {
      // Make new content scene
      JMonkeyContent jMonkeyContent = new JMonkeyContent(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      jMonkeyContent.replaceScene(scene3D);
      mainTabbedPane.add(scene3D.getTitle(), jMonkeyContent);
    }
    getContentPane().invalidate();
  }

  protected void addScene2D(Scene2D scene2D) {
    JPanel scene2DPanel = new JPanel();
    scene2DPanel.setLayout(new BorderLayout());
    scene2DPanel.add(scene2D, BorderLayout.CENTER);

    JPanel ui = scene2D.getUserInterface();
    if (ui != null) {
      ui.setOpaque(true);
      ui.setBackground(Color.WHITE);
      JPanel uiPanel = new JPanel();
      uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));
      uiPanel.setOpaque(true);
      uiPanel.setBackground(Color.WHITE);
      uiPanel.add(ui);
      uiPanel.add(Box.createHorizontalGlue());
      JPanel anotherPanel = new JPanel();
      anotherPanel.add(uiPanel);
      scene2DPanel.add(anotherPanel, BorderLayout.EAST);
    }

    mainTabbedPane.add(scene2D.getTitle(), scene2DPanel);
    getContentPane().invalidate();
  }

  protected void addPanel(JPanel panel, String titel) {
    mainTabbedPane.add(titel, panel);
    getContentPane().invalidate();
  }

  protected void removeAllScenes() {
    mainTabbedPane.removeAll();
    getContentPane().invalidate();
    defaultContentIsInUse = false;
  }

  /**
   * Add editors and viewers
   */
  public void setup(ParameterEditor modelEditor, ModelViewer modelViewer,
                    Scene3D sceneViewer) {
    if (modelEditor != null) {
      addPanel(modelEditor, "Model Editor");
      for (StatusBar.StatusBarItem statusBarItem : modelEditor.getStatusBarItems()) {
        statusBar.add(statusBarItem);
      }
    }
    if (modelViewer != null) {
      addPanel(modelViewer, "Model Viewer");
      for (StatusBar.StatusBarItem statusBarItem : modelViewer.getStatusBarItems()) {
        statusBar.add(statusBarItem);
      }
    }
    if (sceneViewer != null) {
      setScene3D(sceneViewer);
      for (StatusBar.StatusBarItem statusBarItem : sceneViewer.getStatusBarItems()) {
        statusBar.add(statusBarItem);
      }
    }
  }
}
