package com.tajim.hisabkorun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class FragmentProducts extends Fragment {
    LottieAnimationView add_products_frg;
    RecyclerView recycler_products_frg;
    ImageView no_data_img_products_frg;
    TextView no_data_txt_products_frg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_products, container, false);
        add_products_frg = view1.findViewById(R.id.add_products_frg);
        recycler_products_frg = view1.findViewById(R.id.recycler_products_frg);
        no_data_img_products_frg = view1.findViewById(R.id.no_data_img_products_frg);
        no_data_txt_products_frg = view1.findViewById(R.id.empty_txt_products_frg);


        return view1;
    }
}