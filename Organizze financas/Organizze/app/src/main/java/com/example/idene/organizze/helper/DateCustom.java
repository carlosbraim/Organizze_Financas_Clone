package com.example.idene.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){

        long data = System.currentTimeMillis();//Retornar a data por um long
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");//hh:mm:ss para horas
        String dataString = simpleDateFormat.format(data);
        return dataString;

    }

    //separar a data tirando as barras // para passar para o firebase como id
    public static String mesanoDataEcolhida(String data){

        String retornoData[] = data.split("/");
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesAno = mes + ano;
        return mesAno;
    }

}
