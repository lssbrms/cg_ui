/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package ui;

import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;

import javax.swing.*;
import java.awt.*;

public class JMonkeyContent extends JPanel {
  private AwtPanel jmonkeyPanel;
  private static ComputergraphicsJMEApp app;
  private JPanel uiContainer;

  public JMonkeyContent(int width, int height) {
    setLayout(new BorderLayout());
    initJMonkey(width, height);
    add(jmonkeyPanel, BorderLayout.CENTER);
    initUi();
  }

  private void initUi() {
    uiContainer = new JPanel();
    uiContainer.setLayout(new BoxLayout(uiContainer, BoxLayout.Y_AXIS));
    uiContainer.setOpaque(true);
    uiContainer.setBackground(Color.WHITE);
    uiContainer.add(Box.createHorizontalGlue());
    JPanel anotherPanel = new JPanel();
    anotherPanel.add(uiContainer);
    add(anotherPanel, BorderLayout.EAST);
  }

  private void initJMonkey(int width, int height) {
    AppSettings settings = new AppSettings(true);
    settings.setAudioRenderer(null);
    settings.setWidth(width);
    settings.setHeight(height);
    settings.setCustomRenderer(AwtPanelsContext.class);
    try {
      app = new ComputergraphicsJMEApp();
    } catch ( Exception e){
      e.printStackTrace();
      System.exit(0);
    }
    app.setSettings(settings);
    app.setShowSettings(false);
    app.createCanvas();
    app.setPauseOnLostFocus(false);
    AwtPanelsContext jmeContext = (AwtPanelsContext) app.getContext();
    jmeContext.setSystemListener(app);
    app.startCanvas();

    // Wait for OpenGL context
    while (app.getViewPort() == null) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    app.enqueue(() -> app.getFlyByCamera().setDragToRotate(true));

    // Extract canvas and add to Swing UI
    Dimension dim = new Dimension(width, height);
    jmonkeyPanel = jmeContext.createPanel(PaintMode.Accelerated);
    jmeContext.setInputSource(jmonkeyPanel);
    jmonkeyPanel.attachTo(true, app.getViewPort());
    jmonkeyPanel.attachTo(true, app.getGuiViewPort());
    jmonkeyPanel.setPreferredSize(dim);
    jmonkeyPanel.setMinimumSize(dim);
  }

  public void replaceScene(Scene3D scene3D) {
    app.replaceScene(scene3D);
    JPanel ui = scene3D.getUserInterface();
    if (ui != null) {
      ui.setOpaque(true);
      ui.setBackground(Color.WHITE);
      uiContainer.removeAll();
      uiContainer.add(ui);
    }
  }

  public String getTitle() {
    if ( app.getScene() != null ){
      return app.getScene().getTitle();
    } else {
      return "";
    }
  }
}
