package Menu;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.OverlayLayout;

import java.awt.event.ActionEvent;

public class OptionMenu extends Menu {
	// attributes relative to the option menu
	private Menu saveQuitOptionsPanel;
	private Menu saveFailedPanel;
	private Menu buttonPanel;

	/** the menu containing the elements to be saved.. (implements an interface) */
	private OptionContentMenu optionContentMenu;

	public OptionMenu(OptionContentMenu optionContentMenu, BackgroundPanel backgroundPanel, Menu menu, JFrame frame) {
		super(backgroundPanel, menu, frame);

		this.optionContentMenu = optionContentMenu;
		buttonPanel = new Menu();
		saveQuitOptionsPanel = new Menu();
		saveFailedPanel = new Menu();

		createOptionButtons();
		createSaveQuitOptionsPanel();
		createSaveFailedPanel();

		this.add(optionContentMenu);
		this.add(buttonPanel);
		setOrder(true);
	}

	public void createOptionButtons() {

		/** save bindings */
		buttonPanel.addNewButton("SAVE", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {   
				saveOptions();
			}
		});

		/** back to main menu */
		buttonPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				menuInteraction();
			}
		});

		/** reset to default bindings */
		buttonPanel.addNewButton("RESET OPTIONS", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				//check if non default key settings exist and delete those files
				FileFunctions.deleteNonDefaultBindings();
				//recreate the menu with default settings 
				setOptions();
			}
		});

		//make it horizontal
		buttonPanel.setDimensions();
		buttonPanel.setOrder(false);
	}
	
	public void createSaveQuitOptionsPanel() {
		saveQuitOptionsPanel.displayBorder("WARNING");

		saveQuitOptionsPanel.add(new JLabel("Some changes have not been saved"));

		Menu buttonPanel = new Menu();

		/** resume game */
		buttonPanel.addNewButton("SAVE AND QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				menuInteraction(backgroundPanel, menu);
				saveOptions();
			}
		});

		/** go back to the main menu */
		buttonPanel.addNewButton("CANCEL AND QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				menuInteraction(backgroundPanel, menu);
				setOptions();
			}
		});

		buttonPanel.setDimensions();
		buttonPanel.setOrder(false);

		// add all the components
		saveQuitOptionsPanel.add(buttonPanel);

		saveQuitOptionsPanel.setDimensions();
		saveQuitOptionsPanel.setOrder(true);
	}
	public void createSaveFailedPanel() {
		saveFailedPanel.displayBorder("WARNING");

		JLabel info = new JLabel("There's a problem with your bindings");
		saveFailedPanel.add(info);

		saveFailedPanel.addNewButton("OK");

		saveFailedPanel.setDimensions();		
		saveFailedPanel.setOrder(true);
	}

	/** back interaction */
	public void menuInteraction() {
		if (!optionContentMenu.checkChanges()) {
			menuInteraction(backgroundPanel, saveQuitOptionsPanel);
		} else {
			menuInteraction(backgroundPanel, menu);
		}
	}

	/** saves options if they are valid */
	public void saveOptions() {
		//saves the current (the ones being displayed) keyBindings if they are all unique
		if (optionContentMenu.checkValidity()) {
			optionContentMenu.saveOptions();
		} else {
			menuInteraction(backgroundPanel, saveFailedPanel);
		}
	}

	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setOptions() {
		optionContentMenu.setAllOptions();
	}
}