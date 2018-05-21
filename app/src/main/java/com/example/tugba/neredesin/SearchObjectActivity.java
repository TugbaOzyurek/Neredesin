package com.example.tugba.neredesin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchObjectActivity  extends AppCompatActivity {

    Button buttonAra;
    EditText etNesneAdi;

    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://192.168.1.38:8080/")
            .build();

    final ImageAPI imgService = restAdapter.create(ImageAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_object);

        etNesneAdi = findViewById(R.id.editTextAra);
        buttonAra = findViewById(R.id.buttonAra);

        buttonAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String aranan = etNesneAdi.getText().toString();
                etNesneAdi.setText("");
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(aranan));
                reader.setLenient(true);

                imgService.find(aranan, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        if(s != null){
                            Message(s);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("SearchObjectActivity",error.getMessage());
                        Message("failure  "+error.getMessage());
                    }
                });

//                service.login(user, new Callback<ResponseModel>() {
//                    @Override
//                    public void success(ResponseModel responseModel, Response response) {
//                        if(responseModel.getStatus() == 200){   //işlem başarılı
//
//                            Intent intend = new Intent("com.example.selingk.loginretrofit.Activities.UserActivity");    //Kullanıcı UserActivity sayfasına yönlendirilir
//                            intend.putExtra("username",usernameTV.getText().toString());    //UserActivity'ye username ve password yolluyorum
//                            intend.putExtra("password",passwordTV.getText().toString());
//                            startActivity(intend);
//                            Message(responseModel.getMessage());
//                            ;}
//                        else
//                            Message(responseModel.getMessage());    //işlem başarısızsa hatanın neden kaynaklı olduğunu yazıyor.
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        Message(error.getMessage()); //işlem başarısızsa hatanın neden kaynaklı olduğunu yazıyor.
//                    }
//                });

            }
        });


    }

    private void Message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

