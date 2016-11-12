package com.scotty.onebaby;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.scotty.utils.Constants;
import com.scotty.utils.General;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{

    Button signUpButton;
//    name : editText
//    email : editText2
//    password: editText3
//    password_confirm: editText4
//            button3

    EditText edit_name, edit_mail, edit_pass, edit_confirm;
    String name, email, password, password_confirm;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;
    Button facebook_button, google_button;
    // [END declare_auth]
    private static final String TAG = "EmailPassword";
    private static GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGNIN = 9001;
    private CallbackManager m_callbackManager;
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.USER_DB);

//        facebook_button = (Button) findViewById(R.id.button5);
//        google_button = (Button) findViewById(R.id.button6);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signUpButton = (Button) findViewById(R.id.button3);
        edit_confirm = (EditText) findViewById(R.id.editText4);
        edit_mail = (EditText) findViewById(R.id.editText2);
        edit_name = (EditText) findViewById(R.id.editText);
        edit_pass = (EditText) findViewById(R.id.editText3);
        mAuth = FirebaseAuth.getInstance();
//        google_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GoogleSignIn();
//            }
//        });
//        // [END initialize_auth]
//
//        // [START auth_state_listener]
//        facebook_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (AccessToken.getCurrentAccessToken() != null)
//                {
//                    General.ShowToast(getApplicationContext(), "Login Failed");
//                }
//                else {
//                    LoginManager.getInstance().logInWithPublishPermissions(SignUpActivity.this, Arrays.asList("email", "public_profile", "user_friends"));
//                }
//
//            }
//        });
        m_callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(m_callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook error", error.getMessage());
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    void handleFacebookAccessToken(AccessToken token)
    {
        showProgressDialog();
        if (token == null) return;
        final AuthCredential authCredential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(authCredential).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                General.currentUser = authResult.getUser();
                currentUser = authResult.getUser();
                General.SetStringData(getApplicationContext(), Constants.FACEBOOK_LOGIN, "true");
                General.SetStringData(getApplicationContext(), Constants.GOOGLE_LOGIN, "false");
                General.SetStringData(getApplicationContext(), Constants.EMAIL_LOGIN, "false");
                Google_FacebookUserUpdate();
             }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
    void GoogleSignIn()
    {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGNIN);
    }
    public void OnLogIn(View view)
    {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
    boolean flag;
    private void updateUI()
    {
        flag = false;
        name = edit_name.getText().toString();
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(name)
//                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                .build();
//
//        currentUser.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User profile updated.");
//                            SaveUserInformation_EmailLogin();
//                            flag = true;
//                        }
//                    }
//                });
        SaveUserInformation_EmailLogin();
    }
    private void SaveUserInformation_EmailLogin()
    {
        General.SetStringData(getApplicationContext(), Constants.FACEBOOK_LOGIN, "false");
        General.SetStringData(getApplicationContext(), Constants.GOOGLE_LOGIN, "false");
        General.SetStringData(getApplicationContext(), Constants.EMAIL_LOGIN, "true");
        General.SetStringData(getApplicationContext(), Constants.USER_EMAIL, currentUser.getEmail());
        General.SetStringData(getApplicationContext(), Constants.USER_NAME, currentUser.getDisplayName());
        General.SetStringData(getApplicationContext(), Constants.USER_ID, currentUser.getUid());
        General.SetStringData(getApplicationContext(), Constants.USER_LOGINED, "true");
        General.SetStringData(getApplicationContext(), Constants.USER_PASSWORD, password);
        mDatabase.child(currentUser.getUid()).child(Constants.USER_EMAIL).setValue(currentUser.getEmail());
        mDatabase.child(currentUser.getUid()).child(Constants.USER_PHOTO).setValue(currentUser.getEmail());
        mDatabase.child(currentUser.getUid()).child(Constants.DEVICE_ID).setValue(FirebaseInstanceId.getInstance().getToken());
        mDatabase.child(currentUser.getUid()).child(Constants.ACCOUNT_ID).setValue(name);
        mDatabase.child(currentUser.getUid()).child(Constants.USER_FIRSTNAME).setValue("");
        mDatabase.child(currentUser.getUid()).child(Constants.USER_LASTNAME).setValue("");
        //    mDatabase.child(currentUser.getUid()).child(Constants.USER_TOKEN).setValue(General);
        General.currentUser = currentUser;
        General.firstName = "";
        General.lastName = "";
        General.screenName = name;
        General.logined = true;
        General.email = email;
        General.deviceToken = FirebaseInstanceId.getInstance().getToken();
        General.UID = currentUser.getUid();
//        if (currentUser.getPhotoUrl() != null)
//            General.SetStringData(getApplicationContext(), Constants.USER_PHOTO, currentUser.getPhotoUrl().toString());
//        else
//            General.SetStringData(getApplicationContext(), Constants.USER_PHOTO, currentUser.getPhotoUrl().toString());
        Intent intent = new Intent(SignUpActivity.this, VariousTitles_Page.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private void SaveUserInformation()
    {
        General.SetStringData(getApplicationContext(), Constants.USER_EMAIL, currentUser.getEmail());
        General.SetStringData(getApplicationContext(), Constants.USER_NAME, currentUser.getDisplayName());
        General.SetStringData(getApplicationContext(), Constants.USER_ID, currentUser.getUid());
        General.SetStringData(getApplicationContext(), Constants.USER_LOGINED, "true");
        General.SetStringData(getApplicationContext(), Constants.USER_PASSWORD, password);
        mDatabase.child(currentUser.getUid()).child(Constants.USER_PHOTO).setValue(currentUser.getDisplayName());
    //    mDatabase.child(currentUser.getUid()).child(Constants.USER_TOKEN).setValue(General);
        General.currentUser = currentUser;
        if (currentUser.getPhotoUrl() != null)
            General.SetStringData(getApplicationContext(), Constants.USER_PHOTO, currentUser.getPhotoUrl().toString());
        Intent intent = new Intent(SignUpActivity.this, VariousTitles_Page.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void revokeAccess()
    {
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                currentUser = authResult.getUser();
                General.currentUser = currentUser;
                General.SetStringData(getApplicationContext(), Constants.GOOGLE_LOGIN, "true");
                General.SetStringData(getApplicationContext(), Constants.FACEBOOK_LOGIN, "false");
                General.SetStringData(getApplicationContext(), Constants.EMAIL_LOGIN, "false");
                Google_FacebookUserUpdate();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        })
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGNIN)
        {
            Toast.makeText(getApplicationContext(), "Google Sign In", Toast.LENGTH_LONG).show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                Log.d("success", "google success");
                firebaseAuthWithGoogle(account);
            }
            else {

            }
        }
        else
        {
            m_callbackManager.onActivityResult(requestCode, resultCode, data);
        }
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
        String firstname = arr[0];
        String lastname = arr[1];
        mDatabase.child(currentUser.getUid()).child(Constants.USER_EMAIL).setValue(currentUser.getEmail());
        mDatabase.child(currentUser.getUid()).child(Constants.USER_PHOTO).setValue(currentUser.getEmail());
        mDatabase.child(currentUser.getUid()).child(Constants.DEVICE_ID).setValue(FirebaseInstanceId.getInstance().getToken());
        mDatabase.child(currentUser.getUid()).child(Constants.ACCOUNT_ID).setValue("");
        mDatabase.child(currentUser.getUid()).child(Constants.USER_FIRSTNAME).setValue(firstname);
        mDatabase.child(currentUser.getUid()).child(Constants.USER_LASTNAME).setValue(lastname);
        //    mDatabase.child(currentUser.getUid()).child(Constants.USER_TOKEN).setValue(General);
        General.currentUser = currentUser;
        if (currentUser.getPhotoUrl() != null)
            General.SetStringData(getApplicationContext(), Constants.USER_PHOTO, currentUser.getPhotoUrl().toString());
        Intent intent = new Intent(SignUpActivity.this, VariousTitles_Page.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private void SignUp()
    {
        if (isValidateForm())
        {
            showProgressDialog();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            currentUser = authResult.getUser();
                            updateUI();
                        }
                    })
            .addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            ;
        }
    }
    boolean isValidateForm()
    {
        password = edit_pass.getText().toString();
        password_confirm = edit_confirm.getText().toString();
        email = edit_mail.getText().toString();
        name = edit_name.getText().toString();
        if (password.equals(""))
        {
            edit_pass.setError("Input password!!!");
            return false;
        }
        else if (password.length() < 6)
        {
            edit_pass.setError("the password length should be 6 more characters.");
            return false;
        }
        if (!password_confirm.equals(password))
        {
            edit_confirm.setError("password isn't same as confirm.");
            return false;
        }
        if (name.equals(""))
        {
            edit_name.setError("Input name!!!");
            return false;
        }
        if (email.equals(""))
        {
            edit_mail.setError("Input e-mail!!!");
            return false;
        }
        if (!General.isEmailValid(email))
        {
            edit_mail.setError("wrong email format");
            return false;
        }

        return true;
    }

    public void OnBack(View view)
    {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
