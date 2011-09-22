package cloud.node.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import util.ZooKeeperUtil;

public class ZooKeeperClient implements Watcher {

	private static ZooKeeperClient instance;

	private static int SESSION_TIMEOUT;
	private ZooKeeper zk;
	private CountDownLatch connectedSignal = new CountDownLatch(1);

	public static ZooKeeperClient getInstance() throws KeeperException, IOException, InterruptedException {
		if (instance == null)
			instance = new ZooKeeperClient();
		return instance;
	}
	
	private ZooKeeperClient() throws IOException, InterruptedException {
		ZooKeeperUtil util = new ZooKeeperUtil();
		util.initConfiguration("cloud.node.zk.zk_conf");
		SESSION_TIMEOUT = util.getSessionTimeout();
		connect(util.getHosts());
	}
	
	public void connect(String hosts) throws IOException, InterruptedException {
		zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
		connectedSignal.await();
	}

	@Override
	public void process(WatchedEvent event) {
		if(event.getState() == KeeperState.SyncConnected) {
			// Decrease the latch by one.
			connectedSignal.countDown();
		}
	}

	public ZooKeeper getZk() {
		return zk;
	}

	public void close() throws InterruptedException {
		zk.close();
	}
	
}
