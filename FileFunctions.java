import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class FileFunctions {
	/** sets the players bindings to their correct value */
	public static String getPathFileToUse(String color) {
		//check if non default key settings exist
		File f = new File(color + "KeyBindings.txt");
		if(!f.exists() || f.isDirectory()) {
			return color + "KeyBindingsDefault.txt";
		}
		return color + "KeyBindings.txt";
	}

	/** return a KeyBinding object from a String path to a file */
	public static KeyBindings getBindings(String path) {
		KeyBindings keyBindings = null;
		ObjectInputStream ois;
		// create an input flux to read an object from a file
		try {
			ois = new ObjectInputStream(
					new BufferedInputStream(
						new FileInputStream(
							new File(path))));
			try {
				//create the object from the file
				keyBindings = (KeyBindings)ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			//close the flux
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyBindings;
	}

	/** saves a KeyBindings object to a file designated by a given path string */
	public static void saveBindings(KeyBindings keyBindings, String path) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(
					new BufferedOutputStream(
						new FileOutputStream(
							new File(path))));
					
			//write the bindings in a file
			oos.writeObject(keyBindings);
			//close the flux
			oos.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/** create default key bindings files in case they're deleted */
	public static void createDefaultBindings() {
		deleteNonDefaultBindings();
		String pathRedKeyBindings = "redKeyBindingsDefault.txt";
		String pathBlueKeyBindings = "blueKeyBindingsDefault.txt";

		File file = new File(pathRedKeyBindings);
		if(file.exists() && !file.isDirectory()) {
			file.delete();
		}
		file = new File(pathBlueKeyBindings);
		if(file.exists() && !file.isDirectory()) {
			file.delete();
		}
		// Touches joueur rouge
		int a=97, z=122, e=101, s=115, d=100, f=102;
		KeyBindings redKeyBindings = new KeyBindings();
		redKeyBindings.addBinding(new KeyBinding(a, "Left"));
		redKeyBindings.addBinding(new KeyBinding(e, "Right"));
		redKeyBindings.addBinding(new KeyBinding(z, "Jump"));
		redKeyBindings.addBinding(new KeyBinding(s, "Grab"));
		redKeyBindings.addBinding(new KeyBinding(d, "Shield"));
		redKeyBindings.addBinding(new KeyBinding(f, "Shoot and push"));

		// Touches joueur bleu
		int u=117, i=105, o=111, k=107, l=108, m=109;
		KeyBindings blueKeyBindings = new KeyBindings();
		blueKeyBindings.addBinding(new KeyBinding(u, "Left"));
		blueKeyBindings.addBinding(new KeyBinding(o, "Right"));
		blueKeyBindings.addBinding(new KeyBinding(i, "Jump"));
		blueKeyBindings.addBinding(new KeyBinding(k, "Grab"));
		blueKeyBindings.addBinding(new KeyBinding(l, "Shield"));
		blueKeyBindings.addBinding(new KeyBinding(m, "Shoot and push"));

		//saves the new default bindings
		saveBindings(redKeyBindings, pathRedKeyBindings);
		saveBindings(blueKeyBindings, pathBlueKeyBindings);
	}

	public static void deleteNonDefaultBindings() {
		File f = new File("redKeyBindings.txt");
		if(f.exists() && !f.isDirectory()) {
			f.delete();
		}
		f = new File("blueKeyBindings.txt");
		if(f.exists() && !f.isDirectory()) {
			f.delete();
		}
	}
	public static void main(String[] args) {
		FileFunctions.createDefaultBindings();
	}
}