package Menu;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.metal.MetalButtonUI;

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
	protected BackgroundPanel backgroundPanel;
	protected Boolean removesAllPanel = false;
	protected Menu menu;

	protected BackMenuInteraction backMenuInteraction;

	public Menu() {
		this(null, null);
	}

	public Menu(BackgroundPanel backgroundPanel, Menu menu) {
		this.setBackground(Color.white);
		this.backgroundPanel = backgroundPanel;
		this.menu = menu;
		backMenuInteraction = new BackMenuInteraction(){
			@Override
			public void backInteraction() {}
		};
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
				menuInteraction(menu);
			}
		});
		this.add(button);
	}

	/*button.setUI(new MetalButtonUI() {
			protected Color getDisabledTextColor() {
				return Color.BLACK;
			}
		});
		button.setEnabled(false);*/

	/** adds the back button, needs the menu to be constructed the long way */
	public void addNewButton(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				backInteraction();
			}
		});
		this.add(button);
	}

	/** changes the default backing interaction */
	public void setBackInteraction(BackMenuInteraction backMenuInteraction) {
		this.backMenuInteraction = backMenuInteraction;
	}

	/** back interaction */
	public void backInteraction() {
		backMenuInteraction.backInteraction();
		backgroundPanel.addMenu(menu, false);
	}

	/** go to a specfic menu */
	public void menuInteraction(Menu menu) {
		backgroundPanel.addMenu(menu, false);
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
			this.setMaximumSize(new Dimension(widthMargin + (int)width, borderMargin + heightMargin + totalHeight));
		} else {
			int totalWidth = 0;
			for (Component component : this.getComponents()) {
				totalWidth += component.getPreferredSize().getWidth() + widthMargin/2;
			}
			this.setPreferredSize(new Dimension(widthMargin + totalWidth, borderMargin + heightMargin + (int)height));
			this.setMaximumSize(new Dimension(widthMargin + totalWidth, borderMargin + heightMargin + (int)height));
		}
	}
}