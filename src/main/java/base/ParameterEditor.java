/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package base;

import ui.StatusBar;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This is the base class for the editor of the parameter input for the pcg system.
 */
public abstract class ParameterEditor extends JPanel {

  /**
   * Can be used to send a message to the status bar.
   */
  private Consumer<String> statusBarNotifier;

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

  public void setStatusBarNotifier(Consumer<String> statusbarNotifier) {
    this.statusBarNotifier = statusbarNotifier;
  }

  public void statusBarMessage(String msg) {
    if (statusBarNotifier == null) {
      return;
    }
    statusBarNotifier.accept(msg);
  }
}