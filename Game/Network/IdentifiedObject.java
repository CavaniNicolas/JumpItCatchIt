package Game.Network;

public class IdentifiedObject {
	private int id;
	private Object obj;
	
	public IdentifiedObject(int id, Object obj) {
		this.id = id;
		this.obj = obj;
	}

	public int getId() {
		return id;
	}

	public Object getObj() {
		return obj;
	}	
}
