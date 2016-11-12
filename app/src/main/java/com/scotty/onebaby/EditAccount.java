package com.scotty.onebaby;

import android.graphics.Color;
import android.media.Image;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scotty.utils.Constants;
import com.scotty.utils.General;

public class EditAccount extends BaseActivity {
    ImageButton googleButton, facebookButton;
    String password, confirm;
    String email, first_name, last_name, account_id;
    EditText edit_email, edit_first, edit_last, edit_accountID, edit_password, edit_retype;
    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference databaserRef;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        mAuth = FirebaseAuth.getInstance();
   //     user = FirebaseAuth.getInstance().getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    user = firebaseAuth.getCurrentUser();
                }
            }
        };


        databaserRef = FirebaseDatabase.getInstance().getReference("users");
        googleButton = (ImageButton) findViewById(R.id.imageButton6);
        facebookButton = (ImageButton) findViewById(R.id.imageButton7);
        edit_accountID = (EditText) findViewById(R.id.editText15);
        edit_email = (EditText) findViewById(R.id.editText16);
        edit_first = (EditText) findViewById(R.id.editText12);
        edit_last = (EditText)  findViewById(R.id.editText14);
        edit_password = (EditText) findViewById(R.id.editText18);
        edit_retype = (EditText) findViewById(R.id.editText19);
        String facebook_state = General.GetStringData(this, Constants.FACEBOOK_LOGIN);
        String google_state = General.GetStringData(this, Constants.GOOGLE_LOGIN);
        String email_stae = General.GetStringData(this, Constants.EMAIL_LOGIN);
        edit_accountID.setText(General.screenName);
        edit_email.setText(General.email);
        edit_first.setText(General.firstName);
        edit_last.setText(General.lastName);
        if (google_state.equals("true")) {
            googleButton.setEnabled(true);
         //   googleButton.setBackgroundColor(Color.parseColor("#ffff1744"));
        }
        else {
            googleButton.setEnabled(false);
          //  googleButton.setBackgroundColor(Color.parseColor("#00000000"));
        }
        if (facebook_state.equals("true"))
            facebookButton.setEnabled(true);
        else
            facebookButton.setEnabled(false);
    }

    private void ChangeAccount()
    {
        if (!account_id.equals("") && !account_id.equals(General.screenName)) {
            showProgressDialog();
            databaserRef.child(user.getUid()).child(Constants.ACCOUNT_ID).setValue(account_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "AccountID change success", Toast.LENGTH_SHORT).show();
                        General.screenName = account_id;
                    } else {
                        Toast.makeText(getApplicationContext(), "AccountID change error", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog();
                }
            });
        }
        if (!first_name.equals("") && !first_name.equals(General.firstName)) {
            showProgressDialog();
            databaserRef.child(user.getUid()).child(Constants.USER_FIRSTNAME).setValue(first_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "FirstName change success", Toast.LENGTH_SHORT).show();
                        General.firstName = first_name;
                    } else {
                        Toast.makeText(getApplicationContext(), "FirstName change error", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog();
                }
            });
        }
        if (!last_name.equals("") && !last_name.equals(General.lastName)) {
            showProgressDialog();
            databaserRef.child(user.getUid()).child(Constants.USER_LASTNAME).setValue(last_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        General.lastName = last_name;
                        Toast.makeText(getApplicationContext(), "LastName change success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "LastName change error", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog();
                }
            });
        }
    }
    private boolean validateForm()
    {
        email = edit_email.getText().toString();
        first_name = edit_first.getText().toString();
        last_name = edit_last.getText().toString();
        account_id = edit_accountID.getText().toString();
        password = edit_password.getText().toString();
        confirm = edit_retype.getText().toString();
        if (!General.isEmailValid(email) && !email.equals("") && !email.equals(General.email))
        {
            edit_email.setError("Input validate email");
            return false;
        }
//        if (first_name.equals(""))
//        {
//            edit_first.setError("Please first name"); return false;
//        }
//        if (last_name.equals("")) {
//            edit_last.setError("Please last name");
//            return false;
//        }
//        if (account_id.equals(""))
//        {
//            edit_accountID.setError("Please input accountID");
//            return false;
//        }
        if (!password.equals("") && password.length() < 6)
        {
            edit_password.setError("password lenngth must be more 6.");
            return false;
        }
        if (!confirm.equals(password))
        {
            edit_retype.setError("password isn't confirm!");
            return false;
        }

        return true;
    }
    public void PasswordChange()
    {
        showProgressDialog();
        user.updatePassword(password).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Password Change Error!  Please login again and retry", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Password Change Success!", Toast.LENGTH_LONG).show();
                }
                hideProgressDialog();
            }
        });
    }

    public void EmailChange()
    {

      //  mAuth.signInWithCredential()
     //   String token = General.GetStringData(this, Constants.USER_TOKEN);
     //   Log.d("TOKEN", token);

//        mAuth.signInWithCustomToken(token).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                Log.d("TOKEN ERROR", task.getException().getMessage());
//            }
//        });
//        String old_email, old_password;
//        old_email = General.GetStringData(this, Constants.USER_EMAIL);
//        old_password = General.GetStringData(this, Constants.USER_PASSWORD);
//        Log.d("email password", old_email + ", " + old_password);
//        mAuth.signInWithEmailAndPassword(old_email, old_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful())
//                    Log.d("RESULT", task.getResult().toString());
//                else
//                    Log.d("RESULT", task.getException().getMessage().toString());
//            }
//        });
//        if (mAuth.signInWithEmailAndPassword(old_email, old_password).isSuccessful())
//        {
            showProgressDialog();
            user.updateEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Email Change Error! Please login again and retry", Toast.LENGTH_LONG).show();

                        Log.d("emailError", task.getException().toString());
                    } else {
                        databaserRef.child(user.getUid()).child(Constants.USER_EMAIL).setValue(email);
                        Toast.makeText(getApplicationContext(), "Email Change Success!", Toast.LENGTH_LONG).show();
                    }
                    hideProgressDialog();
                }
            });
//            user.updateEmail(email).addOnFailureListener(this, new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("error", e.getLocalizedMessage());
//                }
//            });
//        }
//        else
//        {
//            Toast.makeText(this, "please sign in again and retry. ", Toast.LENGTH_LONG).show();
//        }

    }
    public void OnConfirm(View view)
    {
        if (!validateForm()) return;
        if (!email.equals(""))
        {
            EmailChange();
        }
        if (!password.equals(""))
        {
            PasswordChange();
        }
        ChangeAccount();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop()
    {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
    public void OnBack(View view)
    {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
