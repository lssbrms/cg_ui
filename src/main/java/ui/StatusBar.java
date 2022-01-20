/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Function;

/**
 * Debug information can be presented in thie widget. Either bei adden a StatusBarItem or
 * by sending an observer message with id DESCR_DEBUG_STATUS.
 */
public class StatusBar extends JPanel {

  private JTextArea debugStatusTextField;

  public StatusBar() {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    debugStatusTextField = new JTextArea();
    debugStatusTextField.setEditable(false);
    JScrollPane debugScrollPane = new JScrollPane(debugStatusTextField);
    debugScrollPane.setPreferredSize(new Dimension(800, 100));
    add(debugScrollPane);
  }

  public <T extends StatusBarItem> T add(T item) {
    if (getComponentCount() > 0) {
      add(Box.createHorizontalGlue());
    }
    super.add(item);
    return item;
  }

  public void addMessage(String payload) {
    SwingUtilities.invokeLater(() -> {
      debugStatusTextField.setText(debugStatusTextField.getText() + "\n" + (String) payload);
    });
  }

  public static class StatusBarItem extends JLabel {
    private String caption;

    public StatusBarItem() {
    }

    public StatusBarItem(String caption) {
      setCaption(caption);
    }

    public String getCaption() {
      return caption;
    }

    public void setCaption(String caption) {
      this.caption = caption;
      setText(null);
    }

    @Override
    public void setText(String text) {
      if (caption != null) {
        text = caption + ": " + Objects.toString(text, "-");
      }
      super.setText(text);
    }
  }

  public static class ValueStatusBarItem<T> extends StatusBarItem {
    private final Function<T, String> formatter;

    public ValueStatusBarItem(String caption, Function<T, String> formatter) {
      super(caption);
      this.formatter = formatter;
    }

    public void setValue(T value) {
      setText(formatter.apply(value));
    }
  }
}
