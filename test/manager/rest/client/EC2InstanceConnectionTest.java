package manager.rest.client;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

import manager.zk.ZooKeeperClient;
import cloud.node.zk.NodeManager;

public class EC2InstanceConnectionTest {
	
	static String instanceID = "i-1537d75c";
	
	public static void main(String[] args) {
	// Mock the EC2 instance creating an ephemeral node:
	try {
		new NodeManager(ZooKeeperClient.getInstance()).addNodeToCluster("clustytheclown", instanceID);
		Thread.sleep(60000);
	} catch (KeeperException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	}

}
