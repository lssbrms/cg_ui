/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package ui;

import misc.Logger;
import misc.Observable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * Automatically generated pcg.frontend.app.ui for all settings.
 */
public class SettingsEditor extends JPanel {

    private final Observable settingsObject;

    public SettingsEditor(Observable settingsObject) {
        this.settingsObject = settingsObject;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Field[] fields = settingsObject.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getType() == boolean.class) {
                    add(createBooleanEdit(field));
                } else if (field.getType() == String.class) {
                    add(createStringEdit(field));
                } else {
                    JPanel enumPanel = createEnumPanel(field);
                    if (enumPanel != null) {
                        add(enumPanel);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            Logger.getInstance().exception("Failed to auto-generate settings editor", e);
        }
    }

    /**
     * Creates an editor panel for an enum if possible. Return null if not.
     */
    private JPanel createEnumPanel(Field field) {
        if (!field.getType().isEnum()) {
            return null;
        }

        field.setAccessible(true);
        String name = field.getName();

        JComboBox<String> comboBox = new JComboBox<>();
        JPanel panel = new JPanel();
        panel.add(new JLabel(name + ": "));
        for (Object enumConstant : field.getType().getEnumConstants()) {
            comboBox.addItem(enumConstant.toString());
        }

        try {
            Object value = field.get(settingsObject);
            comboBox.setSelectedItem(value.toString());
            comboBox.addActionListener(e -> {
                Object constant = null;
                for (Object enumConstant : field.getType().getEnumConstants()) {
                    if (enumConstant.toString().equals(comboBox.getSelectedItem())) {
                        constant = enumConstant;
                    }
                }
                if (constant != null) {
                    try {
                        field.set(settingsObject, constant);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        panel.add(comboBox);

        return panel;
    }

    /**
     * Create an edit panel for a boolean field.
     */
    private JPanel createBooleanEdit(Field field) throws IllegalAccessException {
        field.setAccessible(true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        String name = field.getName();
        boolean value = field.getBoolean(settingsObject);
        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setSelected(value);
        checkBox.addActionListener(e -> {
            try {
                field.setBoolean(settingsObject, checkBox.isSelected());
                settingsObject.notifyAllObservers();
            } catch (IllegalAccessException ex) {
                Logger.getInstance().exception("Failed to set boolean setting.", ex);
            }
        });
        panel.add(checkBox);
        return panel;
    }

    /**
     * Create an edit panel for a boolean field.
     */
    private JPanel createStringEdit(Field field) throws IllegalAccessException {
        field.setAccessible(true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        String name = field.getName();
        String value = (String) field.get(settingsObject);
        JTextField textField = new JTextField(name);
        textField.setPreferredSize(new Dimension(200, 20));
        textField.setText(value);
        textField.addActionListener(e -> {
            try {
                field.set(settingsObject, textField.getText());
                settingsObject.notifyAllObservers();
            } catch (IllegalAccessException ex) {
                Logger.getInstance().exception("Failed to set String setting.", ex);
            }
        });
        panel.add(new JLabel(name + ": "));
        panel.add(textField);
        return panel;
    }
}
