package manager.rest.client;

import manager.zk.ZooKeeperClient;

import org.restlet.data.Form;

import org.restlet.resource.ClientResource;

import cloud.node.zk.NodeManager;

public class ClientCreateInstanceTest {
	
	public static void main(String[] args) {
		
		final ClientResource cluster = new ClientResource("http://localhost:8080/myownthemepark/clustytheclown");
		Form f = new Form();
		cluster.post(f.getWebRepresentation());
		
		final ClientResource node = new ClientResource("http://localhost:8080/myownthemepark/clustytheclown/newNode");
		node.post(f.getWebRepresentation());
		
	}

}
