package manager.rest.client;

import org.restlet.data.Form;

import org.restlet.resource.ClientResource;

public class ClientTest {
	
	public static void main(String[] args) {
		
		final ClientResource cr = new ClientResource("http://localhost:8080/myownthemepark/clustytheclown");
		Form f = new Form();
		cr.post(f.getWebRepresentation());
		
	}

}
