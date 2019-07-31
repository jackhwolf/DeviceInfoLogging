package CUSTOM.PACKAGE.NAME.backend;

import walllab.guesswhat.MainActivity;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import com.amazonaws.HttpMethod;
import java.io.File;
import java.net.URL;
import java.util.Date;


/**
 * jack wolf
 */
public class S3Util {

    AmazonS3 client;
    TransferUtility transferUtil;
    String filekey;
    String REGION = "us-west-2";
    String BUCKETNAME = "CUSTOM-BUCKETNAME";


    /**
     * Constructor for S3 manager. Setup s3 client to access buckets
     * and TransferUtility to facilitate file upload/download
     * @param mainAct MainActivity instance for access to fields
     */
    public S3Util(@NonNull MainActivity mainAct) {
        this.client = new AmazonS3Client(mainAct.mCognitoManager.getCredentialProvider(mainAct, mainAct.mCognitoManager.cognito_token));
        client.setRegion(Region.getRegion(REGION));
        this.transferUtil = new TransferUtility(this.client, mainAct);
        String u = mainAct.mCognitoManager.user_pool.getCurrentUser().getUserId();
        this.filekey = "AGDeviceInfo/" + u + "/device_info.txt";
    }

    private Date GetExpirationTime(int time) {
        java.util.Date expire = new java.util.Date();
        long time_remaining = expire.getTime() + time;
        expire.setTime(time_remaining);
        return expire;
    }

    /**
     * generate URL for HTTP background upload
     * @param key
     * @return
     */
    public URL GenerateURL(String key) {
        if (key.charAt(0) == File.separatorChar) {
            key = key.substring(1);
        }
        GeneratePresignedUrlRequest url_req = new GeneratePresignedUrlRequest(BUCKETNAME, key);
        url_req.setMethod(HttpMethod.PUT);
        url_req.setExpiration(GetExpirationTime(1000 * 60));
        url_req.setKey(this.filekey);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = this.client.generatePresignedUrl(url_req);
        return url;
    }


}
