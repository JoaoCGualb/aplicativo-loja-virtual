package com.casasagres.bacalhau.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.casasagres.bacalhau.Adapter.AnunciosAdapter;
import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Model.Anuncio;
import com.casasagres.bacalhau.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private CarouselView carouselView;
    private RecyclerView recyclerAnuncios;
    private AnunciosAdapter adapter;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private List<Anuncio> listaAnunciosCarrosel = new ArrayList<>();
    private ProgressBar progressCarregamento, progressCarregamento2;

    //Firebase
    private DatabaseReference anunciosRef;
    private ValueEventListener valueEventListenerAnuncios;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        //Recupera Anucios
        recuperarAnuncios();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Remove eventListener
        anunciosRef.removeEventListener(valueEventListenerAnuncios);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //inicializando componentes
        anunciosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("anuncios");
        carouselView = view.findViewById(R.id.carouselView);
        progressCarregamento = view.findViewById(R.id.progressBarCarregamento);
        progressCarregamento2 = view.findViewById(R.id.progressCarregamento2);

        //iniciando recyclerView
        recyclerAnuncios = view.findViewById(R.id.recyclerAnuncios);

        //Configurando Adapter
        adapter = new AnunciosAdapter(listaAnuncios,getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerAnuncios.setHasFixedSize(true);
        recyclerAnuncios.setLayoutManager(layoutManager);
        recyclerAnuncios.setAdapter(adapter);
        return view;
    }

    private void recuperarAnuncios(){
        //Limpa lista de anuncios
        listaAnuncios.clear();

        //Inicia recuperação de dados pelo Firebase
        valueEventListenerAnuncios = anunciosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados: snapshot.getChildren()){
                    Anuncio anuncios = dados.getValue(Anuncio.class);

                    //Verifica se anuncio é para o carrosel ou não
                    if(anuncios.isCarrosel()){
                        listaAnunciosCarrosel.add(anuncios);
                    }else {
                        listaAnuncios.add(anuncios);
                    }
                }

                //Coloca as noticias mais recentes no topo
                Collections.reverse(listaAnuncios);

                //Configura carrosel
                configurarCarrosel();

                adapter.notifyDataSetChanged();
                progressCarregamento2.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configurarCarrosel() {
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                String urlString = listaAnunciosCarrosel.get(position).getImagem();
                Picasso.get().load(urlString).into(imageView);

            }
        };
        carouselView.setImageListener(imageListener);
        carouselView.setPageCount(listaAnunciosCarrosel.size());
        progressCarregamento.setVisibility(View.GONE);
    }

}
