package com.example.tugba.neredesin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    ImageView ivAra;
    ImageView ivKamera;
    ImageView imageView;

    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://192.168.1.38:8080/")
            .build();

    final ImageAPI imgService = restAdapter.create(ImageAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivAra = (ImageView) findViewById(R.id.imageViewAra);
        ivKamera = (ImageView) findViewById(R.id.imageViewKamera);
        imageView = findViewById(R.id.imageView);

        ivAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend = new Intent(getApplicationContext(),SearchObjectActivity.class);    //Kullanıcı UserActivity sayfasına yönlendirilir
                startActivity(intend);
            }
        });

        ivKamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            imgService.imgSend(encodedImage, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    if(s != null){
                        Log.d("base64 string:",s.trim().substring(2,s.length()));
                        byte[] decodedString = Base64.decode(s.trim().substring(2,s.length()), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(decodedByte);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("MainActivity",error.getMessage());
                    Message("failure  "+error.getMessage());
                }
            });

        }
    }

    private void Message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
