package com.example.rpgjjbo;

import android.content.Context;
import android.content.res.TypedArray;
import android.widget.ImageButton;
import android.widget.ImageView;
//import com.example.rpgjjbo.R;

/**
 * Se encarga de gestionar la interfaz de seleccion de avatar
 *
 * @author Jose J. Bailon Ortiz
 */
public class SelectorAvatar {
    /**
     * Contexto de la activity
     */
    Context contexto;
    /**
     * Boton avatar anterior
     */
    ImageButton btnAnterior;
    /**
     * Boton avatar posterior
     */
    ImageButton btnPosterior;
    /**
     * Vista de imagen del avatar
     */
    ImageView imgAvatar;

    /**
     * Personaje a editar
     */
    Personaje personaje;
    /**
     * avatar actual (de 0 a 3) basado en su posicion en el array de avatares
     */
    int actual=0;
    /**
     * Desfase dentro del array de avatares basado en raza * 8 + genero * 4 (cada raza tiene 8 avatares y cada genero 4)
     */
    int desfase=0;
    /**
     * Array de recursos de avatares
     */
    TypedArray resourceAvatares;

    /**
     * Constructor
     * @param btnAnterior Ref al boton anterior
     * @param btnPosterior Ref al boton posterior
     * @param imgAvatar Ref a la ImageView del avatar
     * @param personaje Ref al personaje a editar
     * @param contexto Contexto de la activity
     */
    public SelectorAvatar(ImageButton btnAnterior, ImageButton btnPosterior, ImageView imgAvatar, Personaje personaje,Context contexto) {
        //inicializar
        this.btnAnterior = btnAnterior;
        this.btnPosterior = btnPosterior;
        this.imgAvatar = imgAvatar;
        this.personaje = personaje;
        this.contexto=contexto;
        //CALCULO DEL DESFASE SEGUN EL GENERO Y LA CLASE DEL PERSONAJE.
        //raza * 8 + genero * 4 (cada raza tiene 8 avatares y cada genero 4)
        // Eso nos da la posicion inicial dentro del array de los 4 avatares asignados a cada raza y genero
        desfase=this.personaje.getClase().id*8+this.personaje.getGenero().getId()*4;
        //carga de recursos de avatares
        this.resourceAvatares = contexto.getResources().obtainTypedArray(R.array.avatares);

        //eventos
        setEventos();

        //inicializacion de avatar en personaje
        actualizarPersonaje();
        //inicializacion de interface
        actualizaVistaAvatar();
    }

    /**
     * Define los eventos de los botones
     */
    private void setEventos() {
        if (this.btnAnterior!=null)
            this.btnAnterior.setOnClickListener(view -> anterior());
        if (this.btnPosterior!=null)
            this.btnPosterior.setOnClickListener(view ->posterior());
    }


    /**
     * Actualiza los datos de avatar del personaje
     */
    private void actualizarPersonaje() {
        this.personaje.setAvatar(this.resourceAvatares.getResourceId(this.desfase+actual,0));
    }


    /**
     * Selecciona el siguiente avatar
     */
    private void posterior() {
        this.actual++;
        if(this.actual>3)
            this.actual=0;
        actualizarPersonaje();
        actualizaVistaAvatar();
    }


    /**
     * Selecciona el avatar anterior
     */
    private void anterior() {
        this.actual--;
        if(this.actual<0)
            this.actual=3;
        actualizarPersonaje();
        actualizaVistaAvatar();
    }

    /**
     * Actualiza la imagen del avatar en la interface
     */
    private void actualizaVistaAvatar(){
        this.imgAvatar.setImageResource(personaje.getAvatar());
    }
}
