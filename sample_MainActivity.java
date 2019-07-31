package CUSTOM.PACKAGE.NAME;

import CUSTOM.PACKAGE.NAME.backend.CognitoManager;
import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import android.os.Bundle;

/**
 * sample MainActivity class, very bare
 */
public class MainActivity extends AppCompatActivity {

    public CognitoManager mCognitoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // called when app is started
      mCognitoManager = new CognitoManager();
    }

    /**
     * called after user is successfully logged in using Cognito
     * necessary to user cognito to get credentials for S3
     */
    public void PerformPostLoginSequence(CognitoUserSession usersession) {
      mCognitoManager.user_session = usersession;
      mCognitoManager.cognito_token = usersession.getIdToken().getJWTToken();
    }

}
