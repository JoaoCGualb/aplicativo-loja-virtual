package com.casasagres.bacalhau.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.casasagres.bacalhau.Activitys.CatalogoActivity;
import com.casasagres.bacalhau.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LojaFragment extends Fragment {

    private Button buttonLoja,buttonTaberna;

    public LojaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loja, container, false);


        buttonLoja = view.findViewById(R.id.buttonLoja);
        buttonTaberna = view.findViewById(R.id.buttonTaberna);


        buttonLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CatalogoActivity.class);
                i.putExtra("tipoCatalogo", "loja");
                startActivity(i);
            }
        });

        buttonTaberna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CatalogoActivity.class);
                i.putExtra("tipoCatalogo", "taberna");
                startActivity(i);
            }
        });

        return view;
    }
}

