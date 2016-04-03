package buzztrapp.trapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import buzztrapp.trapp.manage_trips.ManageTripsActivity;
import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    // UI references
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    //private View mProgressView;
    private View mSignupFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        mEmailView = (EditText) findViewById(R.id.signup_email);
        mPasswordView = (EditText) findViewById(R.id.signup_password);
        mNameView = (EditText) findViewById(R.id.signup_name);

        Button mSignupButton = (Button) findViewById(R.id.sign_up_button);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });
        mSignupFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);

    }

    private void attemptSignup() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String name = mNameView.getText().toString();

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

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (!isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            focusView = mNameView;
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
            //showProgress(true);
            signUpRequest(email, password, name);
        }
    }

    public void launchSignInActivity(View view){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }

    private void signUpRequest(String email, String password, String name) {
        // fragment manager for the dialogue box
        final FragmentManager fm = this.getFragmentManager();

        //http client to communicated with REST api
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        client.post("http://173.236.255.240/auth/signup", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Gson gson = new Gson();
                String json = new String(response);
                SignupResponseObj resObj = gson.fromJson(json, SignupResponseObj.class);
                if (resObj.getSuccess()) {
                    //Stores the JWT into the shared preferences: USER_PREFS
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("USER_PREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", resObj.getMsg());
                    editor.commit();

                    //Switches to ManageTrip Activity
                    //showProgress(false);
                    startActivity(new Intent(SignUpActivity.this, ManageTripsActivity.class));
                    finish();

                } else {
                    // Display correct error based on what the RESTapi returned
                    // TODO
                    mPasswordView.setError("Incorrect Password");
                    mPasswordView.requestFocus();

                }
                //showProgress(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                DialogFragment dialog = new MyDialogFragment(new String(errorResponse), "Server Error :(");
                dialog.show(fm, "MyDialogFragmentTag");
                //showProgress(false);
            }
        });

    }

    private class SignupResponseObj {
        private boolean success;
        private String msg;

        public SignupResponseObj(boolean success, String msg) {
            this.success = success;
            this.msg = msg;
        }

        //
        public boolean getSuccess() {
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
