package com.example.haneum;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import java.io.File;
import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class MainActivity extends AppCompatActivity {

    String filepath;
    String filename;
    Button TopicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this); // MainActivity color 안 바뀐 이유
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* 권한 요청 */
        ActivityCompat.requestPermissions(this , new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},0);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));


        /* API Test code */
        /*
        API_Interface api_interface = API_Client.getClient().create(API_Interface.class);

        api_interface.getData("1").enqueue(new Callback<List<Pronounce_Class>>(){
            @Override
            public void onResponse(Call<List<Pronounce_Class>> call, Response<List<Pronounce_Class>> response){
                if(response.isSuccessful()){
                    List<Pronounce_Class> data = response.body();
                    Log.d("결과 : ", "성공");
                    Log.d("값 : ", data.get(0).getTitle());
                }
            }

            @Override
            public void onFailure(Call<List<Pronounce_Class>> call, Throwable t){
                Log.d("결과 : ", "실패");

                t.printStackTrace();
            }
        });
        */

        /*     /texts/0 TEST - 단순 String 받아오기
        API_Interface api_interface = API_Client.getClient().create(API_Interface.class);

        api_interface.getText().enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response){
                if(response.isSuccessful()){
                    String data = response.body();
                    Log.d("결과 : ", "성공");
                    Log.d("값 : ", data);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t){
                Log.d("결과 : ", "실패");

                t.printStackTrace();
            }
        });
        */

        filename = "test_audio.m4a";
        filepath = this.getCacheDir().getAbsolutePath();
        filepath += "/" + filename;

        File file = new File(filepath); // audio file path

        API_Interface api_interface2 = API_Client.getClient().create(API_Interface.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("audio_file", file.getName(), requestFile);

        RequestBody requestFile2 = RequestBody.create(MediaType.parse("text/plain"), "1");

        Log.d("시작","start");

        api_interface2.update(body, requestFile2).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                if(response.isSuccessful()){
                    String data = response.body().toString();
                    Log.d("결과 : ", "성공");
                    try {
                        Log.d("값", response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                Log.d("결과 : ", "실패");

                t.printStackTrace();
            }
        });

        Log.d("끝","end");

        TopicBtn = findViewById(R.id.button1);
        TopicBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent NewActivity = new Intent(getApplicationContext(), TopicActivity.class);
                startActivity(NewActivity);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        /* Test Code */
        /*
        MenuItem item = menu.findItem(R.id.toolbar_setting);
        item.setVisible(false);
        return true;*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.toolbar_setting) {
            Intent NewActivity = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(NewActivity);
        }
        return true;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        ExitPopup();
    }
    private void ExitPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit, null);
        builder.setView(dialogView);

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnExit = dialogView.findViewById(R.id.btnExit);

        final AlertDialog dialog = builder.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 앱 종료
            }
        });

        dialog.show();
    }

}