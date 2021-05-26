package com.casasagres.bacalhau.Configuracao;


import com.casasagres.bacalhau.Helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {
        private static FirebaseAuth auth;
        private static DatabaseReference database;
        private static StorageReference storage;


        //retorna a instancia do FirebaseAuth
        public static FirebaseAuth getFirebaseAutenticacao(){

            if(auth == null) {
                auth = FirebaseAuth.getInstance();
            }
            return auth;
        }

        //retorna a instancia do FirebaseDatabase
        public static DatabaseReference getFirebaseDatabase(){

            if(database == null) {
                database = FirebaseDatabase.getInstance().getReference();
            }
            return database;
        }

        public static  DatabaseReference recuperarIdUsuario() {
            String emailUsuario = auth.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);
            DatabaseReference usuarioRef = database.child("usuarios").child(idUsuario);

            return usuarioRef;
        }

        //retorna a instancia do FirebaseStorage
        public static StorageReference getFirebaseStorage(){
            if(storage == null){
                storage = FirebaseStorage.getInstance().getReference();
            }
            return storage;
        }
    }
