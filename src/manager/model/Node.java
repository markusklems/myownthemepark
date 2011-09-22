package manager.model;

public class Node {
	
	private final String imageID;
	private final String instanceID;

	public Node(final String imageID,final String instanceID) {
		this.imageID = imageID;
		this.instanceID = instanceID;
	}

	public String getImageID() {
		return imageID;
	}

	public String getInstanceID() {
		return instanceID;
	}
	
	@Override
	public String toString() {
		StringBuffer toReturn = new StringBuffer();
		toReturn.append(getImageID());
		toReturn.append(".");
		toReturn.append(getInstanceID());
		return toReturn.toString();
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
