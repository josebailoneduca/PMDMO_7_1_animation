package com.example.rpgjjbo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Clase adaptador para injectar los datos en un spinner selector de clase de personaje
 *
 * Necesita un texto con id nombre en el layout de seleccionado y
 * para la vista del dropdown necesita:
 * -dos textos uno id nombre y otro con id descripcion
 * -una imagen id imagenIcono
 * - dos rattingbars con id ratingAtaque y ratingSalud
 *
 */
public class ClasesArrayAdapter extends ArrayAdapter<EnumClassType> {
    private int itemLayout;

    private int dropdownItemLayout;
    private EnumClassType[] clases;

    private Context context;
    public ClasesArrayAdapter(@NonNull Context context, int resource, @NonNull EnumClassType[] clases) {
        super(context, resource, clases);
        itemLayout =resource;
        this.clases=clases;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflador= LayoutInflater.from(context);
        View vFila = inflador.inflate(itemLayout,parent,false);
        ((TextView)vFila.findViewById(R.id.nombre)).setText(clases[position].getNombre());
        return vFila;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflador= LayoutInflater.from(context);
        View vFila = inflador.inflate(dropdownItemLayout,parent,false);
        ((TextView)vFila.findViewById(R.id.nombre)).setText(clases[position].getNombre());
        ((ImageView) vFila.findViewById(R.id.imagenIcono)).setImageResource(clases[position].getImagen());
        RatingBar rbVida=(RatingBar) vFila.findViewById(R.id.ratingSalud);
        rbVida.setMax(5);
        rbVida.setProgress(clases[position].getVida());
        RatingBar rbFuerza=(RatingBar) vFila.findViewById(R.id.ratingAtaque);
        rbFuerza.setMax(5);
        rbFuerza.setProgress(clases[position].getFuerza());
        ((TextView)vFila.findViewById(R.id.descripcion)).setText(clases[position].getDescripcion());
        return vFila;
    }

    @Override
    public void setDropDownViewResource(int resource) {
        this.dropdownItemLayout =resource;
    }
}
