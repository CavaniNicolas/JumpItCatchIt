package Menu;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;

public class Menu extends JPanel {
	private static final long serialVersionUID = -5397491825495901493L;

	protected double width = 0;
	protected double height = 0;
	protected final int widthMargin = 15;
	protected final int heightMargin = 15;
	protected int borderMargin = 0;
	protected Menu self = this;
	protected JPanel backgroundPanel;
	protected Menu menu;
	protected JFrame frame;

	public Menu() {
		this(null, null, null);
	}

	public Menu(JPanel backgroundPanel, Menu menu, JFrame frame) {
		this.setBackground(Color.white);
		this.backgroundPanel = backgroundPanel;
		this.menu = menu;
		this.frame = frame;
	}

	public void displayBorder(String name) {
		this.setBorder(BorderFactory.createTitledBorder(name));
		borderMargin = 15;
	}

	/** adds a button with the given action listener*/
	public void addNewButton(String name, ActionListener actionListener) {
		JButton button = new JButton(name);
		button.addActionListener(actionListener);
		this.add(button);
	}

	/** adds a button redirecting to another menu */
	public void addNewButton(String name, Menu menu) {
		JButton button = new JButton(name);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				menuInteraction(backgroundPanel, menu);
			}
		});
		this.add(button);
	}

	/** adds the back button, needs the menu to be constructed the long way */
	public void addNewButton(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				menuInteraction();
			}
		});
		this.add(button);
	}

	/** back interaction */
	public void menuInteraction() {
		menuInteraction(backgroundPanel, menu);
	}

	/** go to a specfic menu */
	public void menuInteraction(JPanel backgroundPanel, Menu menu) {
		backgroundPanel.remove(self);
		backgroundPanel.add(menu);
		frame.setContentPane(backgroundPanel);
		frame.setVisible(true);
	}

	/** sets all component size to the size of the biggest component*/
	public void setDimensions() {
		getMaxDimensions();
		for (Component component : this.getComponents()) {
			component.setPreferredSize(new Dimension((int)width, (int)height));
		}
	}

	public void getMaxDimensions() {
		for (Component component : this.getComponents()) {
			width = Math.max(width, component.getPreferredSize().getWidth());
			height = Math.max(height, component.getPreferredSize().getHeight());
		}
	}

	/** orders the JPanel vertically (true) or horizontally */
	public void setOrder(Boolean vertical) {
		getMaxDimensions();
		if (vertical) {
			int totalHeight = 0;
			for (Component component : this.getComponents()) {
				totalHeight += component.getPreferredSize().getHeight() + heightMargin/2;
			}
			this.setPreferredSize(new Dimension(widthMargin + (int)width, borderMargin + heightMargin + totalHeight));
		} else {
			int totalWidth = 0;
			for (Component component : this.getComponents()) {
				totalWidth += component.getPreferredSize().getWidth() + widthMargin/2;
			}
			this.setPreferredSize(new Dimension(widthMargin + totalWidth, borderMargin + heightMargin + (int)height));
		}
	}
}