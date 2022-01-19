package base;

import ui.StatusBar;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the base class for the editor of the parameter input for the pcg system.
 */
public abstract class ParameterEditor extends JPanel {

  /**
   * Input parameters to the system
   */
  protected Parameters parameters;

  public ParameterEditor(Parameters parameters) {
    this.parameters = parameters;
  }

  public Parameters getParameters() {
    return parameters;
  }

  /**
   * (Optionally) Provide a list of status bar items.
   */
  public List<StatusBar.StatusBarItem> getStatusBarItems() {
    return new ArrayList<>();
  }
}