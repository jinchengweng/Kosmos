package com.fct.kosmos.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.fct.kosmos.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    private LottieAnimationView imgUsuario, loadingProgress;
    static int PreqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedUmgUri;
    //Almacenar las imagenes de perfil
    private EditText userEmail, userPassword,userPassword2,userName;

    private Button btnRegistrarse;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //comprobacion de acceso a nuevo usuario
        userEmail = findViewById(R.id.regEmail);
        userName = findViewById(R.id.regName);
        userPassword = findViewById(R.id.regPass1);
        userPassword2 = findViewById(R.id.regPass2);
        loadingProgress = findViewById(R.id.regProBar);

        loadingProgress.setVisibility(View.INVISIBLE);//para que nose vea la principiio

        btnRegistrarse = findViewById(R.id.btnRegistrar);

        //Acceso al registro en Firebase
        mAuth = FirebaseAuth.getInstance();


        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnRegistrarse.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String name = userName.getText().toString();
                final String paswd1 = userPassword.getText().toString();
                final String paswdCompro = userPassword2.getText().toString();


                //excepcion de vacio de campos y no coincidencia de la contraseña
                if(email.isEmpty() || name.isEmpty() || paswd1.isEmpty() || !paswdCompro.equals(paswd1)){

                    showMessage("Porfa escribe las dos contraseñas iguales");
                    btnRegistrarse.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                }else{
                    //si todo va bien se procede a pasar losdatos
                    CreateUserAccount(name,email,paswd1);
                }



            }
        });


        //acceso a la imagen
        imgUsuario = findViewById(R.id.regIncidenciaImagen);
        imgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndrequestForPermission();
                }else{
                    openGallery();
                }



            }
        });
    }
    //fxvfd
    private void CreateUserAccount(final String name, String email, String paswd1) {

        mAuth.createUserWithEmailAndPassword(email,paswd1).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showMessage("Cuenta creada");
                            updateUri();

                        } else {
                            showMessage("No se ha podido crear" + task.getException().getMessage());
                            btnRegistrarse.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                } );


    }


    private void updateToUserInfo(final String name, Uri pickedUmgUri, final FirebaseUser currentUser) {

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
        final StorageReference imageFilePath = mStorage.child(pickedUmgUri.getLastPathSegment());
        imageFilePath.putFile(pickedUmgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {//la uri va a contener la direccion de la imagen

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            showMessage("Registro completo");

                                        }
                                    }
                                });
                    }
                });
            }
        });

    }

    private void updateUri() {
        Intent LogInActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(LogInActivity);
        finish();
    }

    private void showMessage(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void checkAndrequestForPermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getBaseContext(), getString(R.string.permisoIMG), Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PreqCode);
            }
        }else{
            openGallery();

        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            pickedUmgUri = data.getData();
            imgUsuario.setImageURI(pickedUmgUri);
        }
    }

}
