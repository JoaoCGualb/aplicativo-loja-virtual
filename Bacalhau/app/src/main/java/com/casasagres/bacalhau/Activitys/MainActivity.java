package com.casasagres.bacalhau.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.casasagres.bacalhau.Configuracao.ConfiguracaoFirebase;
import com.casasagres.bacalhau.Fragment.HomeFragment;
import com.casasagres.bacalhau.Fragment.LojaFragment;
import com.casasagres.bacalhau.Fragment.MinhasComprasFragment;
import com.casasagres.bacalhau.Fragment.PerfilFragment;
import com.casasagres.bacalhau.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    private Bundle bundle;
    private FirebaseAuth auth;
    private BottomNavigationViewEx bottomNavigationViewEx;

    public static int HOME = 0;
    public static int LOJA = 1;
    public static int MINHAS_COMPRAS = 2;
    public static int PERFIL = 3;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Casa Sagres");
        setSupportActionBar(toolbar);

        inicializarComponentes();

//Configurar bootom navigation view
        configuraBottomNavigationView();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPager, new HomeFragment()).commit();
    }

    private void inicializarComponentes() {

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        bundle = getIntent().getExtras();
    }

    private void configuraBottomNavigationView(){
        bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

        //faz configurações iniciais do Bottom Navigation
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.setTextVisibility(true);

        //habilitar navegação
        habilitarNavegacao(bottomNavigationViewEx);


            //configurar item selecionado inicialmente
            Menu menu = bottomNavigationViewEx.getMenu();
            MenuItem menuItem = menu.getItem(0);
            bottomNavigationViewEx.setCurrentItem(0);
            menuItem.setChecked(true);
    }

    private void habilitarNavegacao(final BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        fragmentTransaction.replace(R.id.viewPager, new HomeFragment()).commit();
                        return true;

                    case R.id.ic_loja:
                        fragmentTransaction.replace(R.id.viewPager, new LojaFragment()).commit();
                        return true;

                    case R.id.ic_minhasCompras:
                        if(auth.getCurrentUser()!= null) {
                            fragmentTransaction.replace(R.id.viewPager, new MinhasComprasFragment()).commit();
                        }else{
                            usuarioNaoLogado();
                        }
                        return true;

                    case R.id.ic_perfil:
                        if(auth.getCurrentUser()!=null){
                            fragmentTransaction.replace(R.id.viewPager, new PerfilFragment()).commit();
                        }else{
                            usuarioNaoLogado();
                        }
                        return true;

                }
                return false;
            }
        });

    }

    private void usuarioNaoLogado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Usuario não Logado");
        builder.setMessage("Esta área é destinada a usuarios previamente logados, para acessa-la pedimos que realize o login primeiramente." +
                "\nAgradeçemos a compreenção.");

        builder.setPositiveButton("Logar-se", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirTelaLogin();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Menu menu = bottomNavigationViewEx.getMenu();
                MenuItem menuItem = menu.getItem(0);
                bottomNavigationViewEx.setCurrentItem(0);
                menuItem.setChecked(true);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_a, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(auth.getCurrentUser() != null){
            menu.setGroupVisible(R.id.menuLogado,true);
        }else{
            menu.setGroupVisible(R.id.menuDeslogado,true);

        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                break;
            case R.id.menuLogar:
                abrirTelaLogin();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirTelaLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void deslogarUsuario(){
        try {
            auth.signOut();
            invalidateOptionsMenu();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

