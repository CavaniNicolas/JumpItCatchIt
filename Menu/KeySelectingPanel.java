package Menu;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KeySelectingPanel extends JPanel {
	private JButton selectingButton;
	private JLabel label;
	private JButton resetButton;
	private ArrayList<KeySelectingPanel> similarKeySelectingPanels;
	private MainMenu mainMenu;

	public KeySelectingPanel(KeyBinding keyBinding, int position, String path, MainMenu mainMenu) {
		similarKeySelectingPanels = new ArrayList<KeySelectingPanel>();
		this.mainMenu = mainMenu;

		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(250, 30));

		label = new JLabel();
		label.setPreferredSize(new Dimension(100,25));
		selectingButton = new JButton();
		//selectingButton.setBorderPainted(false);
		selectingButton.setOpaque(true);
		selectingButton.setPreferredSize(new Dimension(50, 25));
		selectingButton.addKeyListener(new keyButtonListener());
		resetButton = new JButton("Reset");
		resetButton.setPreferredSize(new Dimension(70, 25));
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				String defaultKeyValue = intToString(FileFunctions.getBindings(path).getKeyBindings().get(position).getKeyValue());
				selectingButton.setText(defaultKeyValue);
				checkAvailability();
			}
		});
		setBinding(keyBinding);

		this.add(label);
		this.add(selectingButton);
		this.add(resetButton);
	}

	public void setBinding(KeyBinding keyBinding) {
		label.setText(keyBinding.getKeyActionDescription());
		selectingButton.setText(intToString(keyBinding.getKeyValue()));
		selectingButton.setBackground(Color.white);
		similarKeySelectingPanels.clear();
	}

	/** changes the color of the button if the bindings are already used */
	public void checkAvailability() {
		//if changing to an available state, check the former similar panel to change it as well
		for (KeySelectingPanel similarKeySelectingPanel : similarKeySelectingPanels) {
			similarKeySelectingPanel.getSimilarKeySelectingPanels().clear();
			similarKeySelectingPanel.getButton().setBackground(Color.white);
		}
		//resets to available before testing again
		similarKeySelectingPanels.clear();
		selectingButton.setBackground(Color.white);

		//create an array list of all KeySelectingPanels
		ArrayList<KeySelectingPanel> allKeySelectingPanels = new ArrayList<KeySelectingPanel>();
		allKeySelectingPanels.addAll(mainMenu.getRedPlayerBindingMenu().getKeySelectingPanels());
		allKeySelectingPanels.addAll(mainMenu.getBluePlayerBindingMenu().getKeySelectingPanels());
		
		//check all the other panels for similar panels
		for (KeySelectingPanel keySelectingPanel : allKeySelectingPanels) {
			if (keySelectingPanel != this && keySelectingPanel.getButton().getText().equals(this.selectingButton.getText())) {
				//add the similar panel to the list of similar panels
				similarKeySelectingPanels.add(keySelectingPanel);
				keySelectingPanel.getSimilarKeySelectingPanels().add(this);
				//gives a red color to same bindings
				selectingButton.setBackground(Color.red);
				keySelectingPanel.getButton().setBackground(Color.red);
			}
		}
	}

	public ArrayList<KeySelectingPanel> getSimilarKeySelectingPanels() {
		return similarKeySelectingPanels;
	}

	/** return selectingButton's text */
	public JButton getButton() {
		return selectingButton;
	}

	/** returns the key binding described currently by the button and key description */
	public KeyBinding getCurrentKeyBinding() {
		return new KeyBinding(stringToInt(selectingButton), label.getText());
	}

	/** returns the ascii value of the first char of a string */
	public int stringToInt(JButton button) {
		String str = button.getText();
		char [] ch = str.toCharArray();
		return (int)ch[0];
	}

	/** returns a single caracter string = ascii caractere of an int */
	public String intToString(int number) {
		return String.valueOf((char)number);
	}

	public class keyButtonListener implements KeyListener {	
		public void keyPressed(KeyEvent event) {
		}
	
		public void keyReleased(KeyEvent event) {
		}
	
		public void keyTyped(KeyEvent event) {	
			String cara = String.valueOf((char)event.getKeyChar());
			//if (cara in validCaracters) {
				selectingButton.setText(cara);
			//}
			checkAvailability();
		}
	}
}