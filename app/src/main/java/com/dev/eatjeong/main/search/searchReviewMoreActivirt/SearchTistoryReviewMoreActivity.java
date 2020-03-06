package com.dev.eatjeong.main.search.searchReviewMoreActivirt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.eatjeong.R;
import com.dev.eatjeong.main.search.SearchRetrofitAPI;
import com.dev.eatjeong.main.search.searchListAdapter.NaverReviewListMoreAdapter;
import com.dev.eatjeong.main.search.searchListAdapter.TistoryReviewListMoreAdapter;
import com.dev.eatjeong.main.search.searchListVO.NaverReviewVO;
import com.dev.eatjeong.main.search.searchListVO.TistoryReviewVO;
import com.dev.eatjeong.main.search.searchRetrofitVO.SearchAreaListResponseVO;
import com.dev.eatjeong.main.search.searchRetrofitVO.SearchNaverListResponseVO;
import com.dev.eatjeong.main.search.searchRetrofitVO.SearchTistoryListResponseVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchTistoryReviewMoreActivity extends AppCompatActivity {


    String user_id,sns_division,place_id,place_address,place_name;

    private ArrayList<TistoryReviewVO> arrayList = new ArrayList<TistoryReviewVO>();

    private Retrofit mRetrofit;

    private SearchRetrofitAPI mSearchRetrofitAPI;

    private Call<SearchTistoryListResponseVO> mCallSearchTistoryListResponseVO;

    ListView listView;

    TistoryReviewListMoreAdapter adapter;

    ProgressBar search_tistory_progress_bar;

    TextView review_more;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_tistory_review_more);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        sns_division = intent.getStringExtra("sns_division");
        place_id = intent.getStringExtra("place_id");
        place_address = intent.getStringExtra("place_address");
        place_name = intent.getStringExtra("place_name");

        //레트로핏 연결하기위한 초기화 작업.
        setRetrofitInit();

        //레트로핏 초기화 후 호출작업 진행.
        callSearchResponse();

        search_tistory_progress_bar = (ProgressBar)findViewById(R.id.search_tistory_progress_bar);
        listView = (ListView)findViewById(R.id.search_tistory_list);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    private void setRetrofitInit() {
        /*addConverterFactory(GsonConverterFactory.create())은
        Json을 우리가 원하는 형태로 만들어주는 Gson라이브러리와 Retrofit2에 연결하는 코드입니다 */

        mRetrofit = new Retrofit.Builder()

                .baseUrl(getString(R.string.baseUrl))

                .addConverterFactory(GsonConverterFactory.create())

                .build();


        mSearchRetrofitAPI = mRetrofit.create(SearchRetrofitAPI.class);

    }

    private void callSearchResponse() {

        String place_address_arr[] = place_address.split(" ");

        if(user_id == null){
            mCallSearchTistoryListResponseVO = mSearchRetrofitAPI.getDaumReviewMore(place_id,user_id,sns_division,place_address_arr[0] + " " +place_address_arr[1] +" " + place_name + " " + "맛집");
        }else{
            mCallSearchTistoryListResponseVO = mSearchRetrofitAPI.getDaumReviewMore(place_id,"temp","T",place_address_arr[0] + " " +place_address_arr[1] +" " + place_name + " " + "맛집");
        }
        mCallSearchTistoryListResponseVO.enqueue(mRetrofitCallback);

    }

    private Callback<SearchTistoryListResponseVO> mRetrofitCallback = new Callback<SearchTistoryListResponseVO>() {



        @Override

        public void onResponse(Call<SearchTistoryListResponseVO> call, Response<SearchTistoryListResponseVO> response) {
            Log.e("dd",response.body().getCode());
            Log.e("dd",response.body().getMessage());

            arrayList.clear();
            for(int i = 0; i < response.body().mDatalist.size(); i ++){
                arrayList.add(new TistoryReviewVO(
                        response.body().mDatalist.get(i).getIndex(),
                        response.body().mDatalist.get(i).getReview_id(),
                        response.body().mDatalist.get(i).getTitle(),
                        response.body().mDatalist.get(i).getWrite_date(),
                        response.body().mDatalist.get(i).getAuthor(),
                        response.body().mDatalist.get(i).getDescription(),
                        response.body().mDatalist.get(i).getUrl(),
                        response.body().mDatalist.get(i).getThumbnail_url(),
                        response.body().mDatalist.get(i).getBookmark_flag()
                ));
            }

            adapter = new TistoryReviewListMoreAdapter(getApplicationContext(),arrayList);
            listView.setAdapter(adapter);

            search_tistory_progress_bar.setVisibility(View.GONE);

        }



        @Override

        public void onFailure(Call<SearchTistoryListResponseVO> call, Throwable t) {


            Log.e("ss","asdasdasd");
            t.printStackTrace();

        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == 1){
                //레트로핏 연결하기위한 초기화 작업.
                setRetrofitInit();

                //재호출
                callSearchResponse();

            }

        }
    }

}
