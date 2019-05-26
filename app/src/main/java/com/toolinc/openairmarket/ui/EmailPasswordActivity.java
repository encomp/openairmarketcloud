package com.toolinc.openairmarket.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.perf.metrics.AddTrace;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.databinding.ActivityEmailpasswordBinding;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

/** Creates a new user or login an existing user. */
public final class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = EmailPasswordActivity.class.getSimpleName();
  private ActivityEmailpasswordBinding emailpasswordBinding;
  @VisibleForTesting ProgressBar mProgressDialog;
  @Inject FirebaseAuth mAuth;

  public void showProgressDialog() {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressBar(this);
      mProgressDialog.setIndeterminate(true);
    }
    mProgressDialog.setVisibility(View.VISIBLE);
  }

  public void hideProgressDialog() {
    if (mProgressDialog != null) {
      mProgressDialog.setVisibility(View.INVISIBLE);
    }
  }

  public void hideKeyboard(View view) {
    final InputMethodManager imm =
        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    hideProgressDialog();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_emailpassword);
    emailpasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_emailpassword);
    emailpasswordBinding.emailSignInButton.setOnClickListener(this);
    emailpasswordBinding.emailCreateAccountButton.setOnClickListener(this);
    emailpasswordBinding.signOutButton.setOnClickListener(this);
    emailpasswordBinding.verifyEmailButton.setOnClickListener(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    updateUI(currentUser);
  }

  @AddTrace(name = "EmailPasswordActivity.createAccount", enabled = true)
  private void createAccount(String email, String password) {
    Timber.tag(TAG).d(TAG, "createAccount:" + email);
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
                  Timber.tag(TAG).d("createUserWithEmail:success");
                  FirebaseUser user = mAuth.getCurrentUser();
                  updateUI(user);
                } else {
                  // If sign in fails, display a message to the user.
                  Timber.tag(TAG).w(task.getException(), "createUserWithEmail:failure.");
                  Toast.makeText(
                          EmailPasswordActivity.this, "Authentication failed.", Toast.LENGTH_SHORT)
                      .show();
                  updateUI(null);
                }
                hideProgressDialog();
              }
            });
  }

  @AddTrace(name = "EmailPasswordActivity.signIn", enabled = true)
  private void signIn(String email, String password) {
    Timber.tag(TAG).d("signIn:" + email);
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
                  Timber.tag(TAG).d("signInWithEmail:success");
                  FirebaseUser user = mAuth.getCurrentUser();
                  updateUI(user);
                } else {
                  // If sign in fails, display a message to the user.
                  Timber.tag(TAG).w(task.getException(), "signInWithEmail:failure.");
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

  @AddTrace(name = "EmailPasswordActivity.sendEmailVerification", enabled = true)
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
                  Timber.tag(TAG).e(task.getException(), "sendEmailVerification");
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
