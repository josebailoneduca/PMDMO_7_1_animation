package com.example.rpgjjbo;

/**
 * Enumerador de generos de personaje
 *
 * @author Jose J. Bailon Ortiz
 */
public enum EnumGender {
    femenino(R.string.femenino,R.drawable.femaleicon,1),
    masculino(R.string.masculino,R.drawable.maleicon,0);

    /**
     * Nombre del genero
     */
    private final int nombre;
    /**
     * Icono del genero
     */
    private final int icono;

    /**
     * Id del genero
     */
    private final int id;
    EnumGender(int nombre,int icono, int id) {
        this.nombre=nombre;
        this.icono=icono;
        this.id =  id;
    }
    public int getNombre(){
        return nombre;
    }

    public int getIcono() {
        return icono;
    }

    public int getId() {
        return id;
    }
}
