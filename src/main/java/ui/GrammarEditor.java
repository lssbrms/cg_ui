package ui;

import base.ParameterEditor;
import misc.grammar.backend.GrammarParameters;
import misc.AssetPath;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Grammar (parameter) editor for an L-System.
 */
public class GrammarEditor<T> extends ParameterEditor {

  public GrammarEditor(GrammarParameters<T> params, String dir) {
    super(params);

    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(350, 600));

    // This text field contains the grammar as text.
    String grammarText = params.getGrammarText();
    JTextArea grammarArea = new JTextArea(grammarText);
    JScrollPane scrollPane = new JScrollPane(grammarArea);
    add(scrollPane, BorderLayout.CENTER);

    // File selection
    JComboBox<String> comboBoxFilename = new JComboBox<>();
    List<String> grammarFilenames = AssetPath.getInstance().getFilesInDir(dir, "misc/grammar");
    for (String filename : grammarFilenames) {
      comboBoxFilename.addItem(filename);
    }
    comboBoxFilename.addActionListener(e -> {
      params.readGrammarFromFile((String) comboBoxFilename.getSelectedItem());
      grammarArea.setText(params.getGrammarText());
    });
    add(comboBoxFilename, BorderLayout.NORTH);

    // Parse-button
    JButton button = new JButton("Parse + Regenerate");
    button.addActionListener(e -> params.readGrammarFromString(grammarArea.getText()));
    add(button, BorderLayout.SOUTH);
  }
}
