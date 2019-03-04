package com.toolinc.openairmarket.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.databinding.ActivityEmailpasswordBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

/** Creates a new user or login an existing user. */
public final class EmailPasswordActivity extends BaseActivity implements View.OnClickListener {

  private static final String TAG = "EmailPassword";
  private ActivityEmailpasswordBinding emailpasswordBinding;
  private FirebaseAuth mAuth;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_emailpassword);
    emailpasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_emailpassword);
    emailpasswordBinding.emailSignInButton.setOnClickListener(this);
    emailpasswordBinding.emailCreateAccountButton.setOnClickListener(this);
    emailpasswordBinding.signOutButton.setOnClickListener(this);
    emailpasswordBinding.verifyEmailButton.setOnClickListener(this);
    mAuth = FirebaseAuth.getInstance();
  }

  @Override
  public void onStart() {
    super.onStart();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    updateUI(currentUser);
  }

  private void createAccount(String email, String password) {
    Log.d(TAG, "createAccount:" + email);
    if (!validateForm()) {
      return;
    }
    showProgressDialog();
    mAuth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            this,
            new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  // Sign in success, update UI with the signed-in user's information
                  Log.d(TAG, "createUserWithEmail:success");
                  FirebaseUser user = mAuth.getCurrentUser();
                  updateUI(user);
                } else {
                  // If sign in fails, display a message to the user.
                  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                  Toast.makeText(
                          EmailPasswordActivity.this, "Authentication failed.", Toast.LENGTH_SHORT)
                      .show();
                  updateUI(null);
                }

                hideProgressDialog();
              }
            });
  }

  private void signIn(String email, String password) {
    Log.d(TAG, "signIn:" + email);
    if (!validateForm()) {
      return;
    }
    showProgressDialog();
    mAuth
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            this,
            new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  // Sign in success, update UI with the signed-in user's information
                  Log.d(TAG, "signInWithEmail:success");
                  FirebaseUser user = mAuth.getCurrentUser();
                  updateUI(user);
                } else {
                  // If sign in fails, display a message to the user.
                  Log.w(TAG, "signInWithEmail:failure", task.getException());
                  Toast.makeText(
                          EmailPasswordActivity.this, "Authentication failed.", Toast.LENGTH_SHORT)
                      .show();
                  updateUI(null);
                }

                if (!task.isSuccessful()) {
                  emailpasswordBinding.status.setText(R.string.auth_failed);
                }
                hideProgressDialog();
              }
            });
  }

  private void signOut() {
    mAuth.signOut();
    updateUI(null);
  }

  private void sendEmailVerification() {
    // Disable button
    findViewById(R.id.verifyEmailButton).setEnabled(false);

    // Send verification email
    final FirebaseUser user = mAuth.getCurrentUser();
    user.sendEmailVerification()
        .addOnCompleteListener(
            this,
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                // Re-enable button
                findViewById(R.id.verifyEmailButton).setEnabled(true);
                if (task.isSuccessful()) {
                  Toast.makeText(
                          EmailPasswordActivity.this,
                          "Verification email sent to " + user.getEmail(),
                          Toast.LENGTH_SHORT)
                      .show();
                } else {
                  Log.e(TAG, "sendEmailVerification", task.getException());
                  Toast.makeText(
                          EmailPasswordActivity.this,
                          "Failed to send verification email.",
                          Toast.LENGTH_SHORT)
                      .show();
                }
              }
            });
  }

  private boolean validateForm() {
    boolean valid = true;
    String email = emailpasswordBinding.fieldEmail.getText().toString();
    if (TextUtils.isEmpty(email)) {
      emailpasswordBinding.fieldEmail.setError("Required.");
      valid = false;
    } else {
      emailpasswordBinding.fieldEmail.setError(null);
    }
    String password = emailpasswordBinding.fieldPassword.getText().toString();
    if (TextUtils.isEmpty(password)) {
      emailpasswordBinding.fieldPassword.setError("Required.");
      valid = false;
    } else {
      emailpasswordBinding.fieldPassword.setError(null);
    }
    return valid;
  }

  private void updateUI(FirebaseUser user) {
    hideProgressDialog();
    if (user != null) {
      emailpasswordBinding.status.setText(
          getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
      emailpasswordBinding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));
      findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
      findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
      findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);
      findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
      startActivity(new Intent(getApplicationContext(), MainActivity.class));
    } else {
      emailpasswordBinding.status.setText(R.string.signed_out);
      emailpasswordBinding.detail.setText(null);
      findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
      findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
      findViewById(R.id.signedInButtons).setVisibility(View.GONE);
    }
  }

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == R.id.emailCreateAccountButton) {
      createAccount(
          emailpasswordBinding.fieldEmail.getText().toString(),
          emailpasswordBinding.fieldPassword.getText().toString());
    } else if (i == R.id.emailSignInButton) {
      signIn(
          emailpasswordBinding.fieldEmail.getText().toString(),
          emailpasswordBinding.fieldPassword.getText().toString());
    } else if (i == R.id.signOutButton) {
      signOut();
    } else if (i == R.id.verifyEmailButton) {
      sendEmailVerification();
    }
  }
}
