package com.dev.eatjeong.main.settings.settingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.eatjeong.R;
import com.dev.eatjeong.main.settings.SettingsRetrofitAPI;
import com.dev.eatjeong.main.settings.settingsRetrofitVO.SettingsUserInfoMapResponseVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PasswordChangeActivity extends AppCompatActivity {
    private String user_id, sns_division;
    private Retrofit mRetrofit;

    private SettingsRetrofitAPI mSettingsRetrofitAPI;

    private Call<SettingsUserInfoMapResponseVO> mCallSettingsUserInfoMapResponseVO;

    EditText password;
    Button confirm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.password_change);

        Intent intent = getIntent();

        password = (EditText)findViewById(R.id.password);
        confirm = (Button)findViewById(R.id.confirm);

        user_id = intent.getStringExtra("user_id");
        sns_division = intent.getStringExtra("sns_division");
        password.setText(intent.getStringExtra("phone_number"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRetrofitInit();

                callPlaceInfoResponse();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRetrofitInit() {
        /*addConverterFactory(GsonConverterFactory.create())은
        Json을 우리가 원하는 형태로 만들어주는 Gson라이브러리와 Retrofit2에 연결하는 코드입니다 */

        mRetrofit = new Retrofit.Builder()

                .baseUrl(getString(R.string.baseUrl))

                .addConverterFactory(GsonConverterFactory.create())

                .build();


        mSettingsRetrofitAPI = mRetrofit.create(SettingsRetrofitAPI.class);

    }

    private void callPlaceInfoResponse() {
        mCallSettingsUserInfoMapResponseVO = mSettingsRetrofitAPI.updatePassword(user_id,sns_division,password.getText().toString());
        mCallSettingsUserInfoMapResponseVO.enqueue(mRetrofitCallback);

    }

    private Callback<SettingsUserInfoMapResponseVO> mRetrofitCallback = new Callback<SettingsUserInfoMapResponseVO>() {
        @Override
        public void onResponse(Call<SettingsUserInfoMapResponseVO> call, Response<SettingsUserInfoMapResponseVO> response) {
            Log.e("dd", response.body().getCode());
            Log.e("dd", response.body().getMessage());

            if(response.body().getCode().equals("200")){
                onBackPressed();
            }
        }


        @Override

        public void onFailure(Call<SettingsUserInfoMapResponseVO> call, Throwable t) {

            Log.e("ss", "asdasdasd");
            t.printStackTrace();

        }
    };

    @Override
    public void onBackPressed() {
        // 검색 동작
        Intent intent = getIntent(); // 객체 생성자의 인자에 아무 것도 넣지 않는다.

        setResult(1,intent);
        // finish();
        super.onBackPressed();


    }
}