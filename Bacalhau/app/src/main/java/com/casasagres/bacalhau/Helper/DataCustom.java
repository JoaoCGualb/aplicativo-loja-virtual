package com.casasagres.bacalhau.Helper;

import java.text.SimpleDateFormat;

public class DataCustom {


    public static String recuperarData() {

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);

        return dataString;

    }

    public static String recuperarHora() {
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String dataString = simpleDateFormat.format(data);

        return dataString;
    }


}
