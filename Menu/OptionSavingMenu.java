package Menu;

import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;

public class OptionSavingMenu extends Menu{
	//attributes relative to the option menu
	private Menu saveQuitOptionsPanel;
	private Menu saveFailedPanel;

	public OptionSavingMenu(BackgroundPanel backgroundPanel) {
		/** save bindings */
		addNewButton("SAVE", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {   
				saveOptions(false);
			}
		});

		/** back to main menu */
		addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				menuInteraction();
			}
		});

		/** reset to default bindings */
		addNewButton("RESET OPTIONS", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				//check if non default key settings exist and delete those files
				FileFunctions.deleteNonDefaultBindings();
				//recreate the menu with default settings 
				setOptions();
			}
		});

		//make it horizontal
		setDimensions();
		setOrder(false);
	}
	
	public void createSaveQuitOptionsPanel() {
		saveQuitOptionsPanel.displayBorder("WARNING");

		saveQuitOptionsPanel.add(new JLabel("Some changes have not been saved"));

		Menu buttonPanel = new Menu();

		/** resume game */
		buttonPanel.addNewButton("SAVE AND QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				menuInteraction(backgroundPanel, menu);
				saveOptions(true);
			}
		});

		/** go back to the main menu */
		buttonPanel.addNewButton("CANCEL AND QUIT", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				menuInteraction(backgroundPanel, menu);
				setBindings();
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

		/** resume game */
		saveFailedPanel.addNewButton("OK");

		saveFailedPanel.setDimensions();		
		saveFailedPanel.setOrder(true);
	}

	/** back interaction */
	public void menuInteraction() {
		if (!checkChanges()) {
			menuInteraction(backgroundPanel, saveQuitOptionsPanel);
		} else {
			menuInteraction(backgroundPanel, menu);
		}
	}

	public Boolean checkChanges() {
		return true;
	}

	public Boolean checkValidity() {
		return true;
	}

	/** saves options if they are valid */
	public void saveOptions(Boolean back) {
		//saves the current (the ones being displayed) keyBindings if they are all unique
		if (checkValidity()) {
			KeyBindings redBindings = redPlayerBindings.getCurrentKeyBindings();
			FileFunctions.saveObject(redBindings, "KeyBindings/redKeyBindings.txt");
		} else {
			menuInteraction(backgroundPanel, saveFailedPanel);
		}
	}

	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setOptions() {
		redPlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("red"));
	}
	
}