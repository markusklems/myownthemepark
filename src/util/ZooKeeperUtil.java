package util;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class ZooKeeperUtil {
	
	static Logger logger = Logger.getLogger(ZooKeeperUtil.class);
	
	public static final String SESSION_TIMEOUT_PROPERTY = "session.timeout";
	public static final String SESSION_TIMEOUT_PROPERTY_DEFAULT = "5000";
	Integer sessionTimeout;
	
	public static final String HOSTS_PROPERTY = "hosts";
	public static final String HOSTS_PROPERTY_DEFAULT = "127.0.0.1";
	String hosts;	
	
	private ResourceBundle zkConf;
	
	public void initConfiguration(final String zkConfFile) {
		logger.info("Load zookeeper configuration file "+zkConfFile);
		zkConf =
			    ResourceBundle.getBundle(zkConfFile);
		logger.debug(zkConf.toString());
		sessionTimeout = Integer.parseInt(((String)zkConf.getObject(SESSION_TIMEOUT_PROPERTY)).replace(" ", ""));
		if(sessionTimeout==null) {
			logger.warn("The following property has not been set: "+SESSION_TIMEOUT_PROPERTY);
			logger.warn("Therefore, we take the default value: "+SESSION_TIMEOUT_PROPERTY_DEFAULT);
			sessionTimeout=Integer.parseInt(SESSION_TIMEOUT_PROPERTY_DEFAULT);
		}
		hosts = (String) zkConf.getObject(HOSTS_PROPERTY);
		if(hosts==null) {
			logger.warn("The following property has not been set: "+HOSTS_PROPERTY);
			logger.warn("Therefore, we take the default value: "+HOSTS_PROPERTY_DEFAULT);
			hosts=HOSTS_PROPERTY_DEFAULT;
		}		
	}
	
	public int getSessionTimeout() {
		return sessionTimeout;
	}
	
	public String getHosts() {
		return hosts;
	}

}
