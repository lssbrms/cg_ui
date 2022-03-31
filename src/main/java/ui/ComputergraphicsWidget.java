package ui;

import base.ModelViewer;
import base.ParameterEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComputergraphicsWidget extends JFrame {

  private final StatusBar statusBar;

  private JTabbedPane mainTabbedPane;

  ComputergraphicsWidget(String title, int width, int height) {
    super(title);

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

  public void addScene2D(Scene2D scene2D) {
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

  public void addPanel(JPanel panel, String title) {
    mainTabbedPane.add(title, panel);
    getContentPane().invalidate();
    if (panel instanceof ParameterEditor) {
      ParameterEditor parameterEditor = (ParameterEditor) panel;
      for (StatusBar.StatusBarItem statusBarItem : parameterEditor.getStatusBarItems()) {
        statusBar.add(statusBarItem);
      }
      parameterEditor.setStatusBarNotifier(text -> statusBar.addMessage(text));
    } else if (panel instanceof ModelViewer) {
      ModelViewer modelViewer = (ModelViewer) panel;
      for (StatusBar.StatusBarItem statusBarItem : modelViewer.getStatusBarItems()) {
        statusBar.add(statusBarItem);
      }
    }
  }

  public void removeAllScenes() {
    mainTabbedPane.removeAll();
    getContentPane().invalidate();
  }

  public StatusBar getStatusBar() {
    return statusBar;
  }
}
