package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.perf.metrics.AddTrace;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.databinding.FragmentLoginBinding;
import com.toolinc.openairmarket.ui.EmailPasswordActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import timber.log.Timber;

public class LoginFragment extends DaggerFragment implements View.OnClickListener {

  public static final int REQUEST_CODE = 1;
  private static final String TAG = EmailPasswordActivity.class.getSimpleName();
  private FragmentLoginBinding loginBinding;

  @VisibleForTesting ProgressBar mProgressDialog;
  @Inject FirebaseAuth mAuth;

  public void showProgressDialog() {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressBar(getContext());
      mProgressDialog.setIndeterminate(true);
    }
    mProgressDialog.setVisibility(View.VISIBLE);
  }

  public void hideProgressDialog() {
    if (mProgressDialog != null) {
      mProgressDialog.setVisibility(View.INVISIBLE);
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    hideProgressDialog();
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    loginBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, viewGroup, false);
    loginBinding.emailSignInButton.setOnClickListener(this);
    loginBinding.emailCreateAccountButton.setOnClickListener(this);
    loginBinding.signOutButton.setOnClickListener(this);
    loginBinding.verifyEmailButton.setOnClickListener(this);
    LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());
    if (args.getLogout()) {
      signOut();
    }
    return loginBinding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null && currentUser.isEmailVerified()) {
      NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_start_recipts);
    } else {
      updateUI(currentUser);
    }
  }

  @AddTrace(name = "EmailPasswordActivity.createAccount", enabled = true)
  private void createAccount(View view, String email, String password) {
    Timber.tag(TAG).d(TAG, "createAccount: [%s].", email);
    if (!validateForm()) {
      return;
    }
    showProgressDialog();
    mAuth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            getActivity(),
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
                  Snackbar.make(view, R.string.account_failed, Snackbar.LENGTH_LONG).show();
                  updateUI(null);
                }
                hideProgressDialog();
              }
            });
  }

  @AddTrace(name = "EmailPasswordActivity.signIn", enabled = true)
  private void signIn(View view, String email, String password) {
    Timber.tag(TAG).d("signIn: [%s].", email);
    if (!validateForm()) {
      return;
    }
    showProgressDialog();
    mAuth
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            getActivity(),
            new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  // Sign in success, update UI with the signed-in user's information
                  Timber.tag(TAG).d("signInWithEmail:success");
                  FirebaseUser user = mAuth.getCurrentUser();
                  if (user.isEmailVerified()) {
                    NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_start_recipts);
                  } else {
                    String msg =
                        String.format(getString(R.string.hint_verification_email), user.getEmail());
                    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
                  }
                } else {
                  // If sign in fails, display a message to the user.
                  Timber.tag(TAG).w(task.getException(), "signInWithEmail:failure.");
                  Snackbar.make(view, R.string.auth_failed, Snackbar.LENGTH_LONG).show();
                  updateUI(null);
                }
                if (!task.isSuccessful()) {
                  loginBinding.status.setText(R.string.auth_failed);
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
  private void sendEmailVerification(View view) {
    // Disable button
    loginBinding.verifyEmailButton.setEnabled(false);

    // Send verification email
    final FirebaseUser user = mAuth.getCurrentUser();
    user.sendEmailVerification()
        .addOnCompleteListener(
            getActivity(),
            new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                // Re-enable button
                loginBinding.verifyEmailButton.setEnabled(true);
                if (task.isSuccessful()) {
                  Snackbar.make(
                          view,
                          String.format(getString(R.string.verification_sent), user.getEmail()),
                          Snackbar.LENGTH_LONG)
                      .show();
                  signOut();
                } else {
                  Timber.tag(TAG).e(task.getException(), "sendEmailVerification");
                  Snackbar.make(view, R.string.verification_failed, Snackbar.LENGTH_LONG).show();
                }
              }
            });
  }

  private boolean validateForm() {
    boolean valid = true;
    String email = loginBinding.fieldEmail.getText().toString();
    if (TextUtils.isEmpty(email)) {
      loginBinding.fieldEmail.setError(getString(R.string.hint_invalid_email));
      valid = false;
    } else {
      loginBinding.fieldEmail.setError(null);
    }
    String password = loginBinding.fieldPassword.getText().toString();
    if (TextUtils.isEmpty(password)) {
      loginBinding.fieldPassword.setError(getString(R.string.hint_invalid_password));
      valid = false;
    } else {
      loginBinding.fieldPassword.setError(null);
    }
    return valid;
  }

  private void updateUI(FirebaseUser user) {
    hideProgressDialog();
    if (user != null) {
      loginBinding.status.setText(
          getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
      loginBinding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));
      loginBinding.emailPasswordButtons.setVisibility(View.GONE);
      loginBinding.emailPasswordFields.setVisibility(View.GONE);
      loginBinding.signedInButtons.setVisibility(View.VISIBLE);
      loginBinding.verifyEmailButton.setEnabled(!user.isEmailVerified());
    } else {
      loginBinding.status.setText(R.string.signed_out);
      loginBinding.detail.setText(null);
      loginBinding.emailPasswordButtons.setVisibility(View.VISIBLE);
      loginBinding.emailPasswordFields.setVisibility(View.VISIBLE);
      loginBinding.signedInButtons.setVisibility(View.GONE);
    }
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.emailCreateAccountButton) {
      createAccount(
          view,
          loginBinding.fieldEmail.getText().toString(),
          loginBinding.fieldPassword.getText().toString());
    } else if (view.getId() == R.id.emailSignInButton) {
      signIn(
          view,
          loginBinding.fieldEmail.getText().toString(),
          loginBinding.fieldPassword.getText().toString());
    } else if (view.getId() == R.id.signOutButton) {
      signOut();
    } else if (view.getId() == R.id.verifyEmailButton) {
      sendEmailVerification(view);
    }
  }
}
