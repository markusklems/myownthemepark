package util;

import java.util.ResourceBundle;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public class AWSUtil {
	
	public static final String PATH_TO_PROPERT_FILE="manager.aws.aws_conf";
	public static final String AWS_ID_PROPERTY = "AWS_ID";
	public static final String AWS_SECRET_KEY_PROPERTY = "AWS_SECRET_KEY";
	
	public static AWSCredentials getAWSCredentials() {
		ResourceBundle awsConf = ResourceBundle.getBundle(PATH_TO_PROPERT_FILE);
		final String awsID = (String) awsConf.getObject(AWS_ID_PROPERTY);
		final String awsSecretKey = (String) awsConf.getObject(AWS_SECRET_KEY_PROPERTY);
		
		return new BasicAWSCredentials(awsID,awsSecretKey);
	}

}
