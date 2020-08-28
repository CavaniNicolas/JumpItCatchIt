package Menu;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KeyOptionMenu extends Menu {
	//attributes relative to the option menu
	private Menu saveQuitOptionsPanel;
	private Menu saveFailedPanel;

	//key binding sub menus
	private KeyBindingMenu redPlayerBindings;
	private KeyBindingMenu bluePlayerBindings;

	private Boolean unsavedChanges = false;

	//all the KeySelectingPanels
	private ArrayList<KeySelectingPanel> allKeySelectingPanels = new ArrayList<KeySelectingPanel>();

	public KeyOptionMenu(BackgroundPanel backgroundPanel, Menu menu, JFrame frame) {
		super(backgroundPanel, menu, frame);

		saveFailedPanel = new Menu(backgroundPanel, this, frame);
		saveQuitOptionsPanel = new Menu();

		createOptionMenu();
		createSaveFailedPanel();
		createSaveQuitOptionsPanel();
	}

	public void createOptionMenu() {
		Menu playerMovement = new Menu();
		Menu buttonPanel = new Menu();

		redPlayerBindings = new KeyBindingMenu();
		redPlayerBindings.displayBorder("Player movement");
		bluePlayerBindings = new KeyBindingMenu();
		bluePlayerBindings.displayBorder("Second player movement (local only)");

		redPlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("red"), "KeyBindings/redKeyBindingsDefault.txt", this);
		bluePlayerBindings.addKeySelectingPanels(FileFunctions.getPathFileToUse("blue"), "KeyBindings/blueKeyBindingsDefault.txt", this);

		addAllKeySelectingPanels();

		redPlayerBindings.setDimensions();
		redPlayerBindings.setOrder(true);
		bluePlayerBindings.setDimensions();
		bluePlayerBindings.setOrder(true);
		
		playerMovement.add(redPlayerBindings);
		playerMovement.add(bluePlayerBindings);
		playerMovement.setDimensions();
		playerMovement.setOrder(false);

		/** save bindings */
		buttonPanel.addNewButton("SAVE", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {   
				saveOptions(false);
				unsavedChanges = false;
			}
		});

		/** back to main menu */
		buttonPanel.addNewButton("BACK", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				menuInteraction();
			}
		});

		/** reset to default bindings */
		buttonPanel.addNewButton("RESET BINDINGS", new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {        
				//check if non default key settings exist and delete those files
				FileFunctions.deleteNonDefaultBindings();
				//recreate the menu with default settings 
				setBindings();
				unsavedChanges = false;
			}
		});

		//make it horizontal
		buttonPanel.setDimensions();
		buttonPanel.setOrder(false);

		//add the panels/menus
		this.add(playerMovement);
		this.add(buttonPanel);

		this.setOrder(true);
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
		buttonPanel.addNewButton("CANCEL AND QUIT", menu);

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
		if (unsavedChanges) {
			menuInteraction(backgroundPanel, saveQuitOptionsPanel);
		} else {
			menuInteraction(backgroundPanel, menu);
		}
	}

	//create an array list of all KeySelectingPanels
	public void addAllKeySelectingPanels() {
		allKeySelectingPanels.addAll(redPlayerBindings.getKeySelectingPanels());
		allKeySelectingPanels.addAll(bluePlayerBindings.getKeySelectingPanels());
	}

	/** saves options if they are valid */
	public void saveOptions(Boolean back) {
		//saves the current (the ones being displayed) keyBindings if they are all unique
		if (checkUnicity()) {
			KeyBindings redBindings = redPlayerBindings.getCurrentKeyBindings();
			KeyBindings blueBindings = bluePlayerBindings.getCurrentKeyBindings();
			FileFunctions.saveObject(redBindings, "KeyBindings/redKeyBindings.txt");
			FileFunctions.saveObject(blueBindings, "KeyBindings/blueKeyBindings.txt");
		} else {
			menuInteraction(backgroundPanel, saveFailedPanel);
		}
	}


	/** check if every binding is unique */
	public boolean checkUnicity() {
		for (int i = 0; i < allKeySelectingPanels.size(); i++) {
			for (int j = i + 1; j < allKeySelectingPanels.size(); j++) {
				if (allKeySelectingPanels.get(i).getCurrentKeyBinding().getKeyValue() == allKeySelectingPanels.get(j).getCurrentKeyBinding().getKeyValue()) {
					return false;
				}
			}
		}
		return true;
	}

	/** displays main menu and cancel unsaved bindings changes */
	public void backToMainMenuFromOption() {
		//cancels changes if they are not saved
		setBindings();
		unsavedChanges = false;
	}

	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setBindings() {
		redPlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("red"));
		bluePlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("blue"));
	}

	public ArrayList<KeySelectingPanel> getAllKeySelectingPanels() {
		return allKeySelectingPanels;
	}

	public void setUnsavedChanges(Boolean bool) {
		unsavedChanges = bool;
	}

}