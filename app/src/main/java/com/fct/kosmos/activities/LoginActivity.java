package com.fct.kosmos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.fct.kosmos.R;
import com.fct.kosmos.view.ui.CatalogActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText nombreUsuario, contraseña;
    private Button btnAcceso, btnRegistro;
    private LottieAnimationView barraProgreso;

    //Conexion a firebase
    private FirebaseAuth mAuth;

    //lOG IN GOOGLE
    private GoogleApiClient googleApi;
    public static final int SING_IN_CODE = 777;

    private Intent activityRegistro,restart, homeActivity;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        //asignar los valores de las variables
        nombreUsuario = findViewById(R.id.etId);
        contraseña = findViewById(R.id.etContrasenia);
        btnAcceso = findViewById(R.id.btnAcceso);
        btnRegistro = findViewById(R.id.btnAcceso2);
        barraProgreso = findViewById(R.id.logInBar);

        activityRegistro = new Intent(this, RegisterActivity.class);
        homeActivity = new Intent(this, CatalogActivity.class);
        restart = new Intent(this, LoginActivity.class);

        //gOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        barraProgreso.setVisibility(View.INVISIBLE);
        btnAcceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barraProgreso.setVisibility(View.VISIBLE);
                btnAcceso.setVisibility(View.INVISIBLE);
                btnRegistro.setVisibility(View.INVISIBLE);

                final String idUsuario = nombreUsuario.getText().toString().trim();
                final String contraseniaUsuario = contraseña.getText().toString().trim();

                if (idUsuario.isEmpty() || contraseniaUsuario.isEmpty()) {
                    showMessage("No has intorudico tus datos correctamente, vuelve a intentarlo");
                    barraProgreso.setVisibility(View.INVISIBLE);
                    btnAcceso.setVisibility(View.VISIBLE);
                    btnRegistro.setVisibility(View.VISIBLE);
                } else {
                    acceder(idUsuario, contraseniaUsuario);
                }


            }
        });
    }


    private void acceder(String idUsuario, String contraseniaUsuario) {

        mAuth.signInWithEmailAndPassword(idUsuario, contraseniaUsuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    barraProgreso.setVisibility(View.VISIBLE);
                    btnAcceso.setVisibility(View.INVISIBLE);
                    btnRegistro.setVisibility(View.INVISIBLE);
                    actualizarUsuario();
                }else{
                    showMessage(task.getException().getMessage());
                    iniciarDeNuevo();
                }


            }
        });
    }
    private void updateUI(GoogleSignInAccount account) {

    }

    private void actualizarUsuario() {
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String s) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this, "No hay una cuenta asociada a este email", Toast.LENGTH_LONG);
        toast.show();
    }

    public void accesoRegistro(View v){
        startActivity(activityRegistro);
    }


    private void iniciarDeNuevo() {
        startActivity(restart);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}