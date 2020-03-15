package com.dev.eatjeong.main.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dev.eatjeong.R;
import com.dev.eatjeong.main.search.searchActivity.MapSearchActivity;
import com.dev.eatjeong.main.search.searchFragment.LatelyFragment;
import com.dev.eatjeong.main.search.searchFragment.PlaceListFragment;
import com.dev.eatjeong.main.search.searchFragment.PopularFragment;

public class SearchTab extends Fragment implements View.OnClickListener{
    public static final int sub = 1002; /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
    EditText search_keyword;
    AppCompatImageView search_map;
    AppCompatImageView search_image;
    AppCompatTextView search_popular_keyword,search_lately_keyword;

    //키보드 관련
    InputMethodManager imm;

    FrameLayout child_fragment_container;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search_tab, container, false);

        initFragment(v);
        search_image = v.findViewById(R.id.search_image);
        search_image.setOnClickListener(this);

        search_map = v.findViewById(R.id.search_map);
        search_map.setOnClickListener(this);

        search_keyword = (EditText)v.findViewById(R.id.search_keyword) ;

        imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE); //키보드 관

        search_keyword.setOnClickListener(this);

        search_lately_keyword = v.findViewById(R.id.search_lately_keyword) ;
        search_lately_keyword.setOnClickListener(this);
        search_popular_keyword = v.findViewById(R.id.search_popular_keyword) ;
        search_popular_keyword.setOnClickListener(this);




        return v;
    }


    @Override
    public void onClick(View v) {
        Fragment fg;
        switch (v.getId()) {
            case R.id.search_map:
                Intent intent = new Intent(getContext(), MapSearchActivity.class);
                startActivityForResult(intent,0);//액티비티 띄우기
                getActivity().overridePendingTransition(R.anim.fadein,0);
                break;
            case R.id.search_image:
                changeText(search_keyword.getText().toString());
                break;
            case R.id.search_lately_keyword:
                fg = LatelyFragment.newInstance();
                setChildFragment(fg);
                search_lately_keyword.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.common_bottom_border));
                search_popular_keyword.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.common_white_background));
                break;
            case R.id.search_popular_keyword:
                fg = PopularFragment.newInstance();
                setChildFragment(fg);
                search_lately_keyword.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.common_white_background));
                search_popular_keyword.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.common_bottom_border));
                break;

            case R.id.search_button:
                if(search_keyword.getText().toString().equals("")){
                    return;
                }

                search_keyword.setCursorVisible(false); //키보드 관련 해당 EditText focus out 시킴
                hideKeyboard(); //키보드 관련련 (키보드 지워줌 )
                fg = PlaceListFragment.newInstance();
                setChildFragment(fg);
                break;
            case R.id.search_keyword:
                search_keyword.setCursorVisible(true); //키보드 관련 해당 EditText focus in
                break;
        }
    }

    public void initFragment(View v){
        Fragment fg;
        fg = LatelyFragment.newInstance();
        setChildFragment(fg);

    }

    public void setChildFragment(Fragment fr) {

        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();

        if (!fr.isAdded()) {
            childFt.replace(R.id.child_fragment_container, fr);
            childFt.addToBackStack(null);
            childFt.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == 1){

                Log.e("LOG", data.getStringExtra("keyword"));
                Log.e("LOG", "결과 받기 성공");
                search_keyword.setText(data.getStringExtra("keyword"));
                Fragment fg;
                fg = PlaceListFragment.newInstance();
                setChildFragment(fg);
                Editable editText = search_keyword.getText();
                Selection.setSelection(editText,editText.length());
            }

        }
    }

    public void changeText(String text)
    {
        search_keyword.setText(text);
        Fragment fg;
        fg = PlaceListFragment.newInstance();
        setChildFragment(fg);
        Editable editText = search_keyword.getText();
        Selection.setSelection(editText,editText.length());
    }

    public String getKeyword(){
        String keyword = search_keyword.getText().toString();
        return keyword;
    }

    //키보드 관련
    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(search_keyword.getWindowToken(), 0);
    }

}
