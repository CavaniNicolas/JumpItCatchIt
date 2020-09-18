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
		menuPanel.setLayout(new OverlayLayout(menuPanel));
		//menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuPanel.setOpaque(false);

		this.add(Box.createVerticalGlue());
		this.add(menuPanel);
		this.add(Box.createVerticalGlue());
	}

	/** adds a menu */
	public void addMenu(Menu menu, Boolean overlaying) {
		//on top of the others 
		if (overlaying) {
			/*Component[] components = menuPanel.getComponents();
			for (Component component : components) {
				component.setFocusable(false);
			}*/

			menuPanel.add(menu);
			menu.setVisible(true);
			menu.setFocusable(true);

			/*Component[] components = menuPanel.getComponents();
			menuPanel.removeAll();
			menuPanel.add(menu);
			for (Component component : components) {
				component.setFocusable(false);
				menuPanel.add(component);
			}*/
		//replacing the others
		} else {
			menuPanel.removeAll();
			menuPanel.add(menu);
		}
		frame.setVisible(true);
	}

	public void removeMenu(Menu menu) {
		menuPanel.remove(menu);
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
