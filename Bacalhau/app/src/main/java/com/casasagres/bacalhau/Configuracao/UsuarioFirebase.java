package com.casasagres.bacalhau.Configuracao;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.casasagres.bacalhau.Model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class UsuarioFirebase {


        public static String getIndentificadorUsuario(){

            if(getUsuarioAtual()!= null) {
                String idUsuario = getUsuarioAtual().getUid();
                return idUsuario;
            }else{
                return "";
            }

        }

        public static FirebaseUser getUsuarioAtual(){
            FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
            return usuario.getCurrentUser();
        }

        public static boolean atualizarFotoUsuario(Uri url){

            try{
                FirebaseUser user = getUsuarioAtual();
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(url)
                        .build();
                user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Log.d("Perfil", "Erro ao atualizar foto de perfil");
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;

        }

        public static boolean atualizarNomeUsuario(String nome){

            try{
                FirebaseUser user = getUsuarioAtual();
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nome)
                        .build();
                user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Log.d("Perfil", "Erro ao atualizar nome de perfil");
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;

        }

        public static Usuario getDadosUsuarioLogado(){

            FirebaseUser firebaseUsuario = getUsuarioAtual();

            Usuario usuario = new Usuario();
            usuario.setId(firebaseUsuario.getUid());
            usuario.setEmail(firebaseUsuario.getEmail());
            usuario.setNome(firebaseUsuario.getDisplayName());

            return usuario;
        }
 }
