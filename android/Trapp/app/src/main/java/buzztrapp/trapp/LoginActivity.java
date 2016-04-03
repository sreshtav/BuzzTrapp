package buzztrapp.trapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import buzztrapp.trapp.manage_trips.ManageTripsActivity;
import cz.msebera.android.httpclient.Header;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mEmailView = (EditText) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.email_login_form || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //force hide keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            loginRequest(email, password);
        }
    }

    public void launchSignUpActivty(View view){
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    /**
     * Takes in the strings inside the email and pw and runs them agains the RESTapi
     * if properly authenticated, saves token in Shared Prefs
     * if not, then displays the proper error
     */
    private void loginRequest(String email, String password) {
        // fragment manager for the dialogue box
        final FragmentManager fm = this.getFragmentManager();

        //http client to communicated with REST api
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        client.post("http://173.236.255.240/auth/login", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Gson gson = new Gson();
                String json = new String(response);
                LoginResponseObj resObj = gson.fromJson(json, LoginResponseObj.class);
                if (resObj.getSucess()) {
                    //Stores the JWT into the shared preferences: USER_PREFS
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("USER_PREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", resObj.getMsg());
                    editor.commit();

                    //Switches to ManageTrip Activity
                    showProgress(false);
                    startActivity(new Intent(LoginActivity.this, ManageTripsActivity.class));
                    finish();

                } else {
                    // Display correct error based on what the RESTapi returned
                    switch (resObj.getMsg()) {
                        case "passwordError":
                            mPasswordView.setError("Incorrect Password");
                            mPasswordView.requestFocus();
                            break;
                        case "userNotFound":
                            mEmailView.setError("User email not found");
                            mEmailView.requestFocus();
                            break;
                    }
                }
                showProgress(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                DialogFragment dialog = new MyDialogFragment(new String(errorResponse), "Server Error :(");
                dialog.show(fm, "MyDialogFragmentTag");
                showProgress(false);
            }
        });
    }

    /**
     * Class that holds the structure for the json obj returned by REST api login
     */
    private class LoginResponseObj {
        private boolean success;
        private String msg;

        public LoginResponseObj(boolean success, String msg) {
            this.success = success;
            this.msg = msg;
        }

        //
        public boolean getSucess() {
            return this.success;
        }

        public String getMsg() {
            return this.msg;
        }
    }

    @SuppressLint("ValidFragment")
    class MyDialogFragment extends DialogFragment {
        private String msg;
        private String title;

        public MyDialogFragment(String msg, String title) {
            this.msg = msg;
            this.title = title;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            return builder.create();
        }
    }
}

