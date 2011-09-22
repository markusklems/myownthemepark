package cloud.node.zk;

import manager.zk.ZooKeeperClient;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class NodeManager implements Watcher {
	
	private ZooKeeperClient zkClient;
	private NodeWatcher watcher;
	
	public NodeManager(ZooKeeperClient zkClient) {
		this.zkClient = zkClient;
		this.watcher = new NodeWatcher(zkClient.getZk());
	}
	
	public void connectWithCluster(String clusterName, String nodeName) throws KeeperException, InterruptedException {
		String path = "/" + clusterName + "/" + nodeName;
		// Put a Watch on the zNode.
		Stat stat = zkClient.getZk().exists(path, true);
		if(stat == null) {
			String createdPath = zkClient.getZk().create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			// TODO log4j INFO
			System.out.println("Created "+createdPath);
		}
		else {
			// TODO log4j WARNING
			System.err.println("The zNode at path "+path+" already exists.");
		}
	}
	
	@Override
	public void process(WatchedEvent event) {
		// Pass event to NodeWatcher
		watcher.process(event);
	}
	
	public static void main(String[] args) {
		try {
			new NodeManager(ZooKeeperClient.getInstance()).connectWithCluster("clustytheclown", "testNode");
			Thread.sleep(60000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
