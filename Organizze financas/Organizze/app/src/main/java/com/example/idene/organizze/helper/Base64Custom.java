package com.example.idene.organizze.helper;

import android.util.Base64;

//https://www.base64decode.org/
public class Base64Custom {

    public static String codificarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)","");//retirar caracteres invalidos
    }

    public static  String decodificarBase64(String textoCodificado){
        return new String( Base64.decode(textoCodificado,Base64.DEFAULT) );
    }

}
