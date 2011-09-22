package cloud.node.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

public class NodeWatcher implements Watcher {
	
	private ZooKeeper zk;

	public NodeWatcher(ZooKeeper zk) {
		this.zk = zk;
	}

	// TODO
	@Override
	public void process(WatchedEvent event) {
		if(event.getType() == EventType.NodeCreated) {
			System.out.println("A new zNode has been created at "+event.getPath());
		} else if(event.getType() == EventType.NodeDeleted) {
			System.out.println("The zNode "+event.getPath()+" has been deleted.");
			// Try to re-connect?
		}
	}
}
