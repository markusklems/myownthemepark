package manager.zk;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

public class ClusterWatcher implements Watcher {
	
	static Logger logger = Logger.getLogger(ClusterWatcher.class);
	
	private ZooKeeper zk;
	private String clusterPath;

	public ClusterWatcher(ZooKeeper zk, String clusterPath) {
		this.zk = zk;
		this.clusterPath = clusterPath;
	}

	// TODO
	@Override
	public void process(WatchedEvent event) {
		if(event.getType() == EventType.NodeCreated) {
			logger.info("A new zNode has been created at "+event.getPath());
		} else if(event.getType() == EventType.NodeDeleted) {
			logger.info("The zNode "+event.getPath()+" has been deleted.");
		} else if(event.getType() == EventType.NodeChildrenChanged) {
			logger.info("The cluster "+event.getPath()+" has changed.");
			try {
				List<String> children = zk.getChildren(clusterPath, false);
				if(children.isEmpty())
					logger.info("There are no nodes in the cluster.");
				else {
					logger.info("The following nodes are in the cluster:");
					for(String child : children) {
						logger.info(child);
					}
				}
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
