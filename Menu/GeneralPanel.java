package Menu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Box;
import java.awt.Component;

public class GeneralPanel extends JPanel {
	private JPanel menuPanel;
	private JFrame frame;
	
	public GeneralPanel(JFrame frame) {
		this.frame = frame;

		menuPanel = new JPanel();
		menuPanel.setOpaque(false);
		menuPanel.setLayout(new OverlayLayout(menuPanel));
		menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		this.add(Box.createVerticalGlue());
		this.add(menuPanel);
		this.add(Box.createVerticalGlue());
	}

	/** adds a menu */
	public void addMenu(Menu menu, Boolean overlaying) {
		//on top of the others 
		if (overlaying) {
			Component[] components = menuPanel.getComponents();
			menuPanel.removeAll();
			menu.setFocusable(true);
			menuPanel.add(menu);
			for (Component component : components) {
				component.setFocusable(false);
				menuPanel.add(component);
			}
		//replacing the others
		} else {
			menuPanel.removeAll();
			menuPanel.add(menu);
		}
		frame.setVisible(true);
	}

	public void removeMenu(Menu menu) {
		menuPanel.remove(menu);
		Component[] components = menuPanel.getComponents();
		menuPanel.removeAll();
		for (Component component : components) {
			menuPanel.add(component);
		}
		frame.setVisible(true);
	}

	@Override
	public boolean isOptimizedDrawingEnabled() {
		return false;
	}


	public JPanel getMenuPanel() {
		return menuPanel;
	}

}
