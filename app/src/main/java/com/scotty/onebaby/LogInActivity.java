package com.scotty.onebaby;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.scotty.utils.Constants;
import com.scotty.utils.General;
import com.google.android.gms.auth.api.Auth;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LogInActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{

    EditText edit_email, edit_pass;
    String email, password;
    Button signin_button;
    private FirebaseAuth mAuth;
    Button googleLoginButton;
    // [END declare_auth]
    private static final String GOOGLE_TAG = "GoogleActivity";
    public static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
//    FirebaseUser user;
    private final String TAG = "TAG";
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]
    DatabaseReference mDatabase;
    FirebaseUser currentUser;
    private TextView textHelp;
    private CallbackManager mCallbackManager;
// ...
//    Button facebookLogin;
    Button facebookLogin;
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void OnCreateAccount(View view)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.USER_DB);
        Auth at;
        edit_email = (EditText) findViewById(R.id.editText2);
        edit_pass = (EditText) findViewById(R.id.editText3);
        signin_button = (Button) findViewById(R.id.button3);
        facebookLogin = (Button) findViewById(R.id.button_facebook_login);
        googleLoginButton = (Button) findViewById(R.id.google_sign_in_button);
        textHelp = (TextView) findViewById(R.id.textView44);
        textHelp.setVisibility(View.GONE);
        textHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, LogInHelpActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        if (General.logined)
//        {
//            textHelp.setText("Forgot your password? Get help signing in.");
//        }
//        else
//        {
//            textHelp.setText("");
//        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignIn();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, firebaseAuth.toString());
                }
            }
        };
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken() != null)
                {
                    General.ShowToast(getApplicationContext(), "already logined");
                    LoginManager.getInstance().logOut();
                }
                else
                {
                    LoginManager.getInstance().logInWithReadPermissions(LogInActivity.this, Arrays.asList("email", "public_profile", "user_friends"));

                }
            }
        });
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edit_email.getText().toString();
                password = edit_pass.getText().toString();
                signIn();
            }
        });
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError:" + error);
                General.ShowToast(getApplicationContext(), "Facebook Error");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
       //     Toast.makeText(this, "Google Sign In", Toast.LENGTH_SHORT).show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                Log.d("Success", "google");
                firebaseAuthWithGoogle(account);
            }
            else
            {
                Toast.makeText(getApplicationContext(), result.getStatus().toString(), Toast.LENGTH_LONG).show();
                updateUI(null);
            }
        }
        else
             mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void revokeAccess(){
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                }
        );
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        Log.d(GOOGLE_TAG, "firebaseAuthWithgoogle:|" + account.getId());
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(GOOGLE_TAG, "signInWithCredential:OnConplete:" + task.isSuccessful());
                if (!task.isSuccessful())
                {
                    Log.w(GOOGLE_TAG, "signInWithCredential", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    currentUser = task.getResult().getUser();
                    General.SetStringData(getApplicationContext(), Constants.FACEBOOK_LOGIN, "false");
                    General.SetStringData(getApplicationContext(), Constants.GOOGLE_LOGIN, "true");
                    General.SetStringData(getApplicationContext(), Constants.EMAIL_LOGIN, "false");
                    General.currentUser = currentUser;
                    Google_FacebookUserUpdate();


                }
                hideProgressDialog();
            }
        })
        .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void GoogleSignIn()
    {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }
    private void signIn()
    {
        Log.d(TAG, "SignIn: " + email);
        if (!validateForm()) return;
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {

//                    Log.w(TAG, "signInWithEmail:failed", task.getException());
//                    Toast.makeText(LogInActivity.this, R.string.auth_failed,
//                            Toast.LENGTH_SHORT).show();
                }
                else
                {
//                       Toast.makeText(LogInActivity.this, R.string.successed,
//                            Toast.LENGTH_SHORT).show();
                    currentUser = task.getResult().getUser();
                    General.SetStringData(getApplicationContext(), Constants.FACEBOOK_LOGIN, "false");
                    General.SetStringData(getApplicationContext(), Constants.GOOGLE_LOGIN, "false");
                    General.SetStringData(getApplicationContext(), Constants.EMAIL_LOGIN, "true");
                    General.currentUser = currentUser;

                    EmailUserUpdate();
//                    SaveUserToFirebase(currentUser);
//                    updateUI(currentUser);
//                    Intent intent = new Intent(LogInActivity.this, VariousTitles_Page.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();

                }
                hideProgressDialog();

            }

        })
        .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void SaveUserToFirebase(FirebaseUser user)
    {
//        mDatabase.child(user.getUid()).child(Constants.USER_EMAIL).setValue(user.getEmail());
//        mDatabase.child(user.getUid()).child(Constants.USER_PHOTO).setValue(user.getEmail());
//        mDatabase.child(user.getUid()).child(Constants.DEVICE_ID).setValue(FirebaseInstanceId.getInstance().getToken());
//        mDatabase.child(user.getUid()).child(Constants.ACCOUNT_ID).setValue();
//        mDatabase.child(user.getUid()).child(Constants.USER_FIRSTNAME).setValue("");
//        mDatabase.child(user.getUid()).child(Constants.USER_LASTNAME).setValue("");

    }

    private void EmailUserUpdate()
    {
        General.SetStringData(getApplicationContext(), Constants.USER_EMAIL, currentUser.getEmail());
        General.SetStringData(getApplicationContext(), Constants.USER_NAME, currentUser.getDisplayName());
        General.SetStringData(getApplicationContext(), Constants.USER_ID, currentUser.getUid());
        General.SetStringData(getApplicationContext(), Constants.USER_LOGINED, "true");
        General.SetStringData(getApplicationContext(), Constants.USER_PASSWORD, password);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.USER_DB).
                child(General.currentUser.getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> map  = (HashMap<String, String>) dataSnapshot.getValue();
                General.screenName = map.get("account_id");
                General.firstName = map.get("user_firstname");
                General.lastName = map.get("user_lastname");
                General.email = map.get("user_email");
               // General.deviceToken = map.get(Constants.DEVICE_ID);
                General.deviceToken = FirebaseInstanceId.getInstance().getToken();
                General.UID = General.currentUser.getUid();
                Intent intent = new Intent(LogInActivity.this, VariousTitles_Page.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void Google_FacebookUserUpdate()
    {

        General.SetStringData(getApplicationContext(), Constants.USER_EMAIL, currentUser.getEmail());
        General.SetStringData(getApplicationContext(), Constants.USER_NAME, currentUser.getDisplayName());
        General.SetStringData(getApplicationContext(), Constants.USER_ID, currentUser.getUid());
        General.SetStringData(getApplicationContext(), Constants.USER_LOGINED, "true");
        General.SetStringData(getApplicationContext(), Constants.USER_PASSWORD, password);

        String string = currentUser.getDisplayName();
        String arr[] = string.split(" ");
        final String firstname = arr[0];
        final String lastname = arr[1];
        General.currentUser = currentUser;
        if (currentUser.getPhotoUrl() != null)
            General.SetStringData(getApplicationContext(), Constants.USER_PHOTO, currentUser.getPhotoUrl().toString());
        final String token = FirebaseInstanceId.getInstance().getToken();
        final DatabaseReference temp = mDatabase.child(currentUser.getUid());
        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                if (hashMap == null)
                {
                    temp.child(Constants.USER_EMAIL).setValue(currentUser.getEmail());
                    temp.child(Constants.USER_PHOTO).setValue(currentUser.getEmail());
                    temp.child(Constants.DEVICE_ID).setValue(token);
                    temp.child(Constants.ACCOUNT_ID).setValue("");
                    temp.child(Constants.USER_FIRSTNAME).setValue(firstname);
                    temp.child(Constants.USER_LASTNAME).setValue(lastname);
                    General.firstName = firstname;
                    General.lastName = lastname;
                    General.screenName = "";
                    General.logined = true;
                    General.email = currentUser.getEmail();
                    General.UID = currentUser.getUid();
                    General.deviceToken = token;
                    //    mDatabase.child(currentUser.getUid()).child(Constants.USER_TOKEN).setValue(General);
                }
                else
                {
                    General.firstName = (String) hashMap.get(Constants.USER_FIRSTNAME);
                    General.lastName = (String) hashMap.get(Constants.USER_LASTNAME);
                    General.screenName = (String) hashMap.get(Constants.ACCOUNT_ID);
                    General.logined = true;
                    General.email = currentUser.getEmail();
                    General.UID = currentUser.getUid();
                    General.deviceToken = token;
                }
                Intent intent = new Intent(LogInActivity.this, VariousTitles_Page.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
//    private void acceessFacebookLoginData(AccessT)

    ///// Handling the Facebook AcessToken
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        if (token == null) return;
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            currentUser  = task.getResult().getUser();
                            General.SetStringData(getApplicationContext(), Constants.FACEBOOK_LOGIN, "true");
                            General.SetStringData(getApplicationContext(), Constants.GOOGLE_LOGIN, "false");
                            General.SetStringData(getApplicationContext(), Constants.EMAIL_LOGIN, "false");
                       //     General.ShowToast(getApplicationContext(), "Success");
                            General.currentUser = currentUser;
                            Google_FacebookUserUpdate();
                        }
                   }
                })
        .addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    ///////Updating UI and Action for currenct user
    private void updateUI(FirebaseUser user)
    {
        if (user == null) return;
     //   Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_LONG).show();


      //  Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
      //  Toast.makeText(this, user.getUid(), Toast.LENGTH_LONG).show();
      //  Toast.makeText(this, user.getPhotoUrl().toString(), Toast.LENGTH_LONG).show();
        SaveUserInformation(user);
    }

    //// Saving current user information
    private void SaveUserInformation(FirebaseUser user)
    {
        General.SetStringData(getApplicationContext(), Constants.USER_EMAIL, user.getEmail());
        General.SetStringData(getApplicationContext(), Constants.USER_NAME, user.getDisplayName());
        General.SetStringData(getApplicationContext(), Constants.USER_ID, user.getUid());
        General.SetStringData(getApplicationContext(), Constants.USER_LOGINED, "true");
        General.SetStringData(getApplicationContext(), Constants.USER_PASSWORD, password);

        user.getToken(true).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                General.SetStringData(LogInActivity.this, Constants.USER_TOKEN, task.getResult().getToken());
                Log.d("TOKEN", task.getResult().getToken());
            }
        });
        if (user.getPhotoUrl() != null)
         General.SetStringData(getApplicationContext(), Constants.USER_PHOTO, user.getPhotoUrl().toString());
        else
            General.SetStringData(getApplicationContext(), Constants.USER_PHOTO, "a");
    }

    ////Check validating textField
    private boolean validateForm()
    {
        if (email.equals(""))
        {
            edit_email.setError("please input email");
            return false;
        }
        if (password.equals(""))
        {
            edit_pass.setError("Please input password");
            return false;
        }
        if (password.length() < 6)
        {
            edit_pass.setError("the password length must be longer than 6");
            return false;
        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(GOOGLE_TAG, "onConnectionFailed" + connectionResult);
     //   Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
