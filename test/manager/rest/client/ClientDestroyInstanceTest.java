package manager.rest.client;

import org.restlet.resource.ClientResource;

public class ClientDestroyInstanceTest {

	static String instanceID = "i-1537d75c";

	public static void main(String[] args) {

		final ClientResource node = new ClientResource(
				"http://localhost:8080/myownthemepark/clustytheclown/"
						+ instanceID);
		node.delete();
	}

}
