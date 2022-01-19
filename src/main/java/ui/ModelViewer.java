package ui;

import misc.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Base viewer class for the viewers of the abstract model representation.
 */
public abstract class ModelViewer extends JPanel implements Observer {

  /**
   * Model object.
   */
  protected Model model;

  public ModelViewer(Model model) {
    this.model = model;
    model.addObserver(this);
  }

  /**
   * Override this method to get informed if the model changed.
   */
  @Override
  public void update() {
  }

  public Model getModel() {
    return model;
  }

  /**
   * Add a panel for the settings.
   */
  public void setSettingsPanel(Component settingsPanel) {
    if (settingsPanel != null) {
      SwingUtilities.invokeLater(() -> {
        add(settingsPanel, BorderLayout.EAST);
      });
    }
  }

  /**
   * (Optionally) Provide a list of status bar items.
   */
  public List<StatusBar.StatusBarItem> getStatusBarItems() {
    return new ArrayList<>();
  }
}
