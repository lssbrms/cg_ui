package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import static ui.CG3DApplication.KEY_1;

/**
 * Base class for 2D applications.
 */
public abstract class CG2DApplication extends JFrame implements KeyListener {

    private final JTabbedPane tabbedPane;

    private final List<Scene2D> scenes;

    public CG2DApplication(String title) {
        scenes = new ArrayList<>();

        setTitle(title);
        setSize(800, 600);
        setVisible(true);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(this);

        tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane);
		
		//Terminate programm on window close
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Add a scene.
     */
    protected void addScene2D(Scene2D scene) {
        JPanel scenePanel = new JPanel();
        scenePanel.setLayout(new BorderLayout());
        scenePanel.add(scene, BorderLayout.CENTER);
        JPanel ui = scene.getUserInterface();
        if ( ui != null){
            scenePanel.add(ui, BorderLayout.EAST);
        }
        tabbedPane.addTab(scene.getTitle(), scenePanel);
        scenes.add(scene);
    }

    protected void addPanel(JPanel panel, String title) {
        JPanel scenePanel = new JPanel();
        scenePanel.setLayout(new BorderLayout());
        scenePanel.add(panel, BorderLayout.CENTER);
        tabbedPane.addTab(title, scenePanel);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 49:
                scenes.forEach(s -> s.handleKey(KEY_1));
        }
    }
}
