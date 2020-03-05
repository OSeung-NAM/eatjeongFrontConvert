package com.dev.eatjeong.main.bookmark.BookmarkFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.dev.eatjeong.R;
import com.dev.eatjeong.main.bookmark.BookmarkListAdapter.BookmarkTistoryListAdapter;
import com.dev.eatjeong.main.bookmark.BookmarkListVO.BookmarkTistoryListVO;
import com.dev.eatjeong.main.bookmark.BookmarkRetrofitAPI;
import com.dev.eatjeong.main.bookmark.BookmarkRetrofitVO.BookmarkTistoryResponseVO;
import com.dev.eatjeong.mainWrap.MainWrapActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TistoryFragment extends Fragment {


    String user_id;
    String sns_division;

    private ArrayList<BookmarkTistoryListVO> arrayList = new ArrayList<BookmarkTistoryListVO>();

    private Retrofit mRetrofit;

    private BookmarkRetrofitAPI mBookmarkRetrofitAPI;

    private Call<BookmarkTistoryResponseVO> mCallBookmarkTistoryResponseVO;

    ListView listView;

    BookmarkTistoryListAdapter adapter;

    public static TistoryFragment newInstance(){
        return new TistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bookmark_tistory_fragment, container, false);


        user_id = ((MainWrapActivity)getActivity()).getUserInfo().get("user_id");
        sns_division = ((MainWrapActivity)getActivity()).getUserInfo().get("sns_division");

        //레트로핏 연결하기위한 초기화 작업.
        setRetrofitInit();

        //레트로핏 초기화 후 호출작업 진행.
        callSearchResponse();

        listView = (ListView) v.findViewById(R.id.bookmark_tistory_list);

        return v;
    }

    private void setRetrofitInit() {
        /*addConverterFactory(GsonConverterFactory.create())은
        Json을 우리가 원하는 형태로 만들어주는 Gson라이브러리와 Retrofit2에 연결하는 코드입니다 */

        mRetrofit = new Retrofit.Builder()

                .baseUrl(getString(R.string.baseUrl))

                .addConverterFactory(GsonConverterFactory.create())

                .build();


        mBookmarkRetrofitAPI = mRetrofit.create(BookmarkRetrofitAPI.class);

    }

    private void callSearchResponse() {

        mCallBookmarkTistoryResponseVO = mBookmarkRetrofitAPI.getBookmarkTistory("tistory",user_id,sns_division);

        mCallBookmarkTistoryResponseVO.enqueue(mRetrofitCallback);

    }

    private Callback<BookmarkTistoryResponseVO> mRetrofitCallback = new Callback<BookmarkTistoryResponseVO>() {



        @Override

        public void onResponse(Call<BookmarkTistoryResponseVO> call, Response<BookmarkTistoryResponseVO> response) {
            Log.e("dd",response.body().getCode());
            Log.e("dd",response.body().getMessage());

            for(int i = 0; i < response.body().mDatalist.size(); i ++){
                Log.e("dd",response.body().mDatalist.get(i).toString());
                arrayList.add(new BookmarkTistoryListVO(
                        response.body().mDatalist.get(i).getPlace_name()
                ));

            }

            adapter = new BookmarkTistoryListAdapter(getContext(),arrayList);
            listView.setAdapter(adapter);

        }



        @Override

        public void onFailure(Call<BookmarkTistoryResponseVO> call, Throwable t) {


            Log.e("ss","asdasdasd");
            t.printStackTrace();

        }

    };

}