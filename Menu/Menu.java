package Menu;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;

public class Menu extends JPanel {
	private double width = 0;
	private double height = 0;
	private final int widthMargin = 15;
	private final int heightMargin = 15;
	private int borderMargin = 0;

	public void displayBorder(String name) {
		this.setBorder(BorderFactory.createTitledBorder(name));
		borderMargin = 15;
	}

	public void createPanel() {
		this.setBackground(Color.white);
	}

	public void addNewButton(String name, ActionListener actionListener) {
		JButton button = new JButton(name);
		button.addActionListener(actionListener);
		this.add(button);
	}

	/** sets all component size to the size of the biggest component*/
	public void setDimensions() {
		for (Component component : this.getComponents()) {
			width = Math.max(width, component.getPreferredSize().getWidth());
			height = Math.max(height, component.getPreferredSize().getHeight());
		}
		for (Component component : this.getComponents()) {
			component.setPreferredSize(new Dimension((int)width, (int)height));
		}
	}

	/** orders the JPanel vertically (true) or horizontally */
	public void setOrder(Boolean vertical) {
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