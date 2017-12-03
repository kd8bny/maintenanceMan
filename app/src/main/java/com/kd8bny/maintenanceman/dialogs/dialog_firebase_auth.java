package com.kd8bny.maintenanceman.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.kd8bny.maintenanceman.R;
import com.rengwuxian.materialedittext.MaterialEditText;


public class dialog_firebase_auth extends DialogFragment {
    private static final String TAG = "dlg_fb_auth";

    private int RESULT_CODE;

    private Context mContext;
    private FirebaseAuth mAuth;

    private MaterialEditText vEmail, vPassword;
    private Button buttonLogIn, buttonCreate;
    //google button
    /*https://developers.google.com/identity/sign-in/android/sign-in
    SignInButton signInButton = findViewById(R.id.sign_in_button);
    signInButton.setSize(SignInButton.SIZE_STANDARD);*/


    public dialog_firebase_auth(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESULT_CODE = getTargetRequestCode();
        mContext = getContext();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_firebase_auth, null);

        buttonLogIn = view.findViewById(R.id.button_login);
        buttonCreate = view.findViewById(R.id.button_create);

        vEmail = view.findViewById(R.id.email);
        vPassword = view.findViewById(R.id.password);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false);

        return alertDialog.create();
    }

    @Override
    public void onStart(){
        super.onStart();
        /*Override in order to keep alert dialog open for error check*/

        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if(alertDialog != null){
            buttonLogIn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String email = vEmail.getText().toString();
                    String password = vPassword.getText().toString();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                    } else {

                                    }
                            }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "signInWithEmail:failure", e);
                                    if(e instanceof FirebaseAuthInvalidUserException){
                                        vEmail.setError(mContext.getString(R.string.error_firebase_invalid_user));
                                    }
                                    if( e instanceof FirebaseAuthInvalidCredentialsException){
                                        vPassword.setError(mContext.getString(R.string.error_firebase_invalid_password));
                                    }
                                    if(e instanceof FirebaseNetworkException){
                                        vEmail.setError(mContext.getString(R.string.error_firebase_connection));
                                    }
                                }
                            });
            }});

            buttonCreate.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String email = vEmail.getText().toString();
                    String password = vPassword.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    /*Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();*/

                                }

                                // ...
                            }
                    });
                }});
        }
    }
}
