package com.example.rpgjjbo;

/**
 * Enumerador de rasgos de personaje
 *
 * @author Jose J. Bailon Ortiz
 */
public enum EnumTrait {
    berserker(R.string.berserker,R.string.berserker_desc),
    cauteloso(R.string.cauteloso,R.string.cauteloso_desc),
    conductor_otto(R.string.conductor_otto,R.string.conductor_otto_desc),
    concentrado(R.string.concentrado,R.string.concentrado_desc),
    honesto(R.string.honesto,R.string.honesto_desc),
    rapido(R.string.rapido,R.string.rapido_desc),
    musculoso(R.string.musculoso,R.string.musculoso_desc),
    empollon(R.string.empollon,R.string.empollon_desc);

    /**
     * nombre del rasgo
     */
    final int nombre;
    /**
     * Descripcion del rasgo
     */
    final int descripcion;
    EnumTrait(int nombre, int descripcion) {
        this.nombre=nombre;
        this.descripcion=descripcion;
    }

    public int getNombre(){
        return this.nombre;
    }
    public int getDescripcion(){
        return this.descripcion;
    }
}
