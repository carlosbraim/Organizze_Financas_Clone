package com.example.idene.organizze;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){

        this.context = context;
    }

    //Array
    public  int[] slide_images ={
            R.drawable.um,
            R.drawable.dois,
            R.drawable.tres,
            R.drawable.quatro

    };

    public String[] slider_headings ={
            "Organize suas contas de onde estiver",
            "Saiba para onde esta indo seu dineheiro",
            "Nunca mais esqueça de pagar uma conta",
            "Tuso organizado, no celular ou computador"
    };

    public String[] slide_descs ={
            "Simples facil de usar e gratis",
            "Categorizando seus lançamentos e vendo o destino de cada centavo",
            "Receba alertas quando quiser",
            "Use no seu smartphone e acessando o Organizze pelo site"
    };

    @Override
    public int getCount() {
        return slider_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImagView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImagView.setImageResource(slide_images[position]);
        slideHeading.setText(slider_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);




        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
