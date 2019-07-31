package CUSTOM.PACKAGE.NAME.backend;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.regions.Region;
import android.content.Context;
import java.util.*;

/**
 * helper class for cognito functionality
 * jack wolf
 */
public class CognitoManager {

    public String cognito_token = "";
    public CognitoUserPool user_pool;
    public CognitoUserSession user_session;
    String REGION = "us-west-2";
    String IDENTITY_POOLID = "CUSTOM_IDENTITYPOOL_ID";
    String POOL_ID = "CUSTOM_POOL_ID";
    String POOL_PROVIDER = "CUSTOM_POOL_PROVIDER";
    String CLIENTAPP_ID     = "CUSTOM_CLIENTAPP_ID";
    String CLIENTAPP_SECRET = "CUSTOM_CLIENTAPP_SECRET";

    /**
     * cache credentials so user keeps permissions while
     * using app
     * @return caching credentials provider
     */
    public CognitoCachingCredentialsProvider getCredentialProvider(Context cont, String token) {
        CognitoCachingCredentialsProvider creds = new CognitoCachingCredentialsProvider(
                cont, IDENTITY_POOLID, REGION);
        Map<String, String> logs = new HashMap<String, String>();
        logs.put(POOL_PROVIDER, token);
        creds.setLogins(logs);
        return creds;
    }

    /**
     * configure user pool
     */
    public void ConfigureUserPool(Context cont) {
        AmazonCognitoIdentityProviderClient cli = new AmazonCognitoIdentityProviderClient(
                new AnonymousAWSCredentials(), new ClientConfiguration());
        cli.setRegion(Region.getRegion(REGION));
        user_pool = new CognitoUserPool(cont, POOL_ID, CLIENTAPP_ID, CLIENTAPP_SECRET, cli);
    }

    /**
     * get email of user
     */
    public String getEmail() {
        return user_pool.getCurrentUser().getUserId();
    }

}
