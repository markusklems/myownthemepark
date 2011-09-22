package manager.zk;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ClusterManager implements Watcher {
	
	static Logger logger = Logger.getLogger(ClusterManager.class);
	
	private ZooKeeperClient zkClient;
	private HashMap<String,ClusterWatcher> watchers;
	
	public ClusterManager(ZooKeeperClient zkClient) {
		this.zkClient = zkClient;
		watchers = new HashMap<String,ClusterWatcher>();
	}
	
	public void createCluster(String clusterName) throws KeeperException, InterruptedException {
		String path = "/" + clusterName;
		// The exists method triggers the creation of a Watch on the zNode.
		Stat stat = zkClient.getZk().exists(path, true);
		if(stat == null) {
			String createdPath = zkClient.getZk().create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info("Created "+createdPath);
			watchers.put(clusterName, new ClusterWatcher(zkClient.getZk(),createdPath));
		}
		else {
			logger.warn("The zNode at path "+path+" already exists.");
		}
	}
	
	public boolean addNodeToCluster(String clusterName, String nodeName) throws KeeperException, InterruptedException {
		String path = "/" + clusterName + "/" + nodeName;
		// Put no Watch on the zNode.
		Stat stat = zkClient.getZk().exists(path, false);
		if(stat == null) {
			//String createdPath = zkClient.getZk().create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			//logger.info("Created "+createdPath);
			return true;
		}
		else {
			logger.warn("The zNode at path "+path+" already exists.");
			return false;
		}
	}
	
	public boolean removeNodeFromCluster(String clusterName, String nodeName) throws KeeperException, InterruptedException {
		String path = "/" + clusterName + "/" + nodeName;
		// Check if the zNode exists.
		Stat stat = zkClient.getZk().exists(path, false);
		if(stat != null) {
			zkClient.getZk().delete(path, -1);
			logger.info("Deleted zNode at "+path);
			return true;
		}
		else {
			logger.warn("I can't delete the zNode at path "+path+" because it does not exists.");
			return false;
		}
	}

	@Override
	public void process(WatchedEvent event) {
		String[] pathElements = event.getPath().split("/");
		if(pathElements.length>1) {
		String clusterName = pathElements[1];
		// Pass event to ClusterWatcher.
		ClusterWatcher watcher = watchers.get(clusterName);
		watcher.process(event);
		} else {
			logger.error("The path of cluster zNode is not valid: "+event.getPath());
		}
	}

}
