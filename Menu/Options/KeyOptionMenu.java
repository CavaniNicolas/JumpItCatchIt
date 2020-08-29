package Menu.Options;

import java.util.ArrayList;

import Menu.FileFunctions;

public class KeyOptionMenu extends OptionContentMenu {
	private static final long serialVersionUID = 7808677089473693746L;

	//key binding sub menus
	private KeyBindingMenu redPlayerBindings;
	private KeyBindingMenu bluePlayerBindings;

	//all the KeySelectingPanels
	private ArrayList<KeySelectingPanel> allKeySelectingPanels = new ArrayList<KeySelectingPanel>();

	public KeyOptionMenu() {
		super();
		createOptionMenu();
	}

	public void createOptionMenu() {
		redPlayerBindings = new KeyBindingMenu("KeyBindings/redKeyBindings.txt", "KeyBindings/redKeyBindingsDefault.txt", this);
		redPlayerBindings.displayBorder("Player movement");
		bluePlayerBindings = new KeyBindingMenu("KeyBindings/blueKeyBindings.txt", "KeyBindings/blueKeyBindingsDefault.txt", this);
		bluePlayerBindings.displayBorder("Second player movement (local only)");

		addAllKeySelectingPanels();

		redPlayerBindings.setDimensions();
		redPlayerBindings.setOrder(true);
		bluePlayerBindings.setDimensions();
		bluePlayerBindings.setOrder(true);
		
		this.add(redPlayerBindings);
		this.add(bluePlayerBindings);
		this.setDimensions();
		this.setOrder(false);
	}

	//create an array list of all KeySelectingPanels
	public void addAllKeySelectingPanels() {
		allKeySelectingPanels.addAll(redPlayerBindings.getKeySelectingPanels());
		allKeySelectingPanels.addAll(bluePlayerBindings.getKeySelectingPanels());
	}

	public ArrayList<KeySelectingPanel> getAllKeySelectingPanels() {
		return allKeySelectingPanels;
	}

	@Override
	public Boolean checkChanges() {
		return redPlayerBindings.checkChanges() && bluePlayerBindings.checkChanges();
	}

	@Override
	/** check if every binding is unique */
	public Boolean checkValidity() {
		for (int i = 0; i < allKeySelectingPanels.size(); i++) {
			for (int j = i + 1; j < allKeySelectingPanels.size(); j++) {
				if (allKeySelectingPanels.get(i).getCurrentKeyBinding().getKeyValue() == allKeySelectingPanels.get(j).getCurrentKeyBinding().getKeyValue()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	/** sets the binding in the bindingMenus to default or personalized bindings according to the existence of personalized bindings */
	public void setAllOptions() {
		redPlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("KeyBindings/redKeyBindings.txt", "KeyBindings/redKeyBindingsDefault.txt"));
		bluePlayerBindings.setAllBindings(FileFunctions.getPathFileToUse("KeyBindings/blueKeyBindings.txt", "KeyBindings/blueKeyBindingsDefault.txt"));
	}

	@Override
	public void saveOptions() {
		FileFunctions.saveObject(redPlayerBindings.getCurrentKeyBindings(), "KeyBindings/redKeyBindings.txt");
		FileFunctions.saveObject(bluePlayerBindings.getCurrentKeyBindings(), "KeyBindings/blueKeyBindings.txt");
	}
}