package Menu.Options;

import java.awt.event.ActionListener;
import javax.swing.JLabel;

import Menu.BackgroundPanel;
import Menu.FileFunctions;
import Menu.Menu;

import java.awt.event.ActionEvent;

/** this is actually a jpanel to be used to have overlapping menus + option menu */
public class OptionMenu extends Menu {
	// attributes relative to the option menu
	private Menu saveQuitOptionsPanel;
	private Menu saveFailedPanel;
	private Menu buttonPanel;
	private Menu optionMenu;

	/** the menu containing the elements to be saved.. (implements an interface) */
	private OptionContentMenu optionContentMenu;

	public OptionMenu(OptionContentMenu optionContentMenu, BackgroundPanel backgroundPanel, Menu menu) {
		super(backgroundPanel, menu);

		this.optionContentMenu = optionContentMenu;
		optionMenu = new Menu();
		buttonPanel = new Menu();

		createOptionButtons();
		createSaveQuitOptionsPanel();
		createSaveFailedPanel();

		optionMenu.add(optionContentMenu);
		optionMenu.add(buttonPanel);
		optionMenu.setOrder(true);

		this.add(optionMenu);
	}

	public void createOptionButtons() {

		/** save bindings */
		buttonPanel.addNewButton("SAVE", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {   
				saveOptions(false);
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
		saveQuitOptionsPanel = new Menu();
		saveQuitOptionsPanel.displayBorder("WARNING");

		saveQuitOptionsPanel.add(new JLabel("Some changes have not been saved"));

		Menu buttonPanel = new Menu();

		/** resume game */
		buttonPanel.addNewButton("SAVE AND QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				saveOptions(true);
			}
		});

		/** go back to the main menu */
		buttonPanel.addNewButton("CANCEL AND QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				menuInteraction(menu);
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
		saveFailedPanel = new Menu();
		saveFailedPanel.displayBorder("WARNING");

		JLabel info = new JLabel("There's a problem with your options");
		saveFailedPanel.add(info);

		saveFailedPanel.addNewButton("OK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				backgroundPanel.removeMenu(saveFailedPanel);
			}
		});

		saveFailedPanel.setDimensions();		
		saveFailedPanel.setOrder(true);
	}

	@Override
	/** back interaction */
	public void menuInteraction() {
		if (!optionContentMenu.checkChanges()) {
			backgroundPanel.addMenu(saveQuitOptionsPanel, true);
		} else {
			backgroundPanel.addMenu(menu, false);
		}
	}

	/** saves options if they are valid, if they are and quit == true, we go back to main menu */
	public void saveOptions(Boolean quit) {
		//saves the current options if they are all valid
		if (optionContentMenu.checkValidity()) {
			optionContentMenu.saveOptions();
			if (quit) {
				backgroundPanel.addMenu(menu, false);
			}
		} else {
			backgroundPanel.addMenu(saveFailedPanel, true);
		}
	}

	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setOptions() {
		optionContentMenu.setAllOptions();
	}
}