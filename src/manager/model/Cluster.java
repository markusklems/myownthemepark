package manager.model;

public class Cluster {
	
	private final String name;

	public Cluster(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node && obj.toString().equals(this.toString()))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		final String id = this.toString();
		if(id != null)
			return id.hashCode();
		else
			return super.hashCode();
	}

}
