package Menu;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;

public class Menu extends JPanel {
	private String name;
	private Boolean displayBorder;
	private BackgroundPanel backgroundPanel;

	public Menu(String name, Boolean displayBorder, BackgroundPanel backgroundPanel) {
		this.name = name;
		this.displayBorder = displayBorder;
		this.backgroundPanel = backgroundPanel;
	}

	public void createPanel() {
		if (displayBorder) {
			this.setBorder(BorderFactory.createTitledBorder(name));
		}
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(220, 90));
	}

	public void createBackButton(Menu backMenu) {
		Menu self = this;
		//back to main menu
		JButton backButton = new JButton("BACK");
		backButton.setPreferredSize(new Dimension(70, 25));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//displays the back menu panel 
				backgroundPanel.remove(self);
				backgroundPanel.add(backMenu);
			}
		});
		this.add(backButton);
	}
}