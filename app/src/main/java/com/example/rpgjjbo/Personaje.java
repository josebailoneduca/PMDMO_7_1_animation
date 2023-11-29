package com.example.rpgjjbo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Clase para almacenar todos los datos de personaje
 *
 * @author Jose J. Bailon Ortiz
 */
public class Personaje {
    private EnumClassType clase;
    private EnumGender genero;
    private String nombre;
    private int avatar;
    private String biografia;
    private int stat_ataque_fisico=0;
    private int stat_ataque_magico=0;
    private int stat_defensa_fisica=0;
    private int stat_defensa_magica=0;
    private int stat_punteria=0;
    private int stat_salud=0;
    private ArrayList<EnumTrait> traits = new ArrayList<EnumTrait>();

    /**
     * Intentos de generacion de stats al azar
     */
    private int intentosStatsAzar = 2;

    /**
     * Puntos aleatorios a distribuir
     */
    private int puntosStatAleatorios=30;

    /**
     * Puntos manuales a distributir
     */
    private int puntosStatManuales=10;

    //GETTERS Y SETTERS

    /**
     * Devuelve los intentos de asignacion de stats al azar que quedan
     * @return El numero de intentos
     */
    public int getIntentosStatsAzar() {
        return intentosStatsAzar;
    }

    public EnumClassType getClase() {
        return clase;
    }

    public void setClase(EnumClassType clase) {
        this.clase = clase;
    }

    public EnumGender getGenero() {
        return genero;
    }

    public void setGenero(EnumGender genero) {
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public int getStat_ataque_fisico() {
        return stat_ataque_fisico;
    }

    public int getStat_ataque_magico() {
        return stat_ataque_magico;
    }

    public int getStat_defensa_fisica() {
        return stat_defensa_fisica;
    }

    public int getStat_defensa_magica() {
        return stat_defensa_magica;
    }

    public int getStat_punteria() {
        return stat_punteria;
    }

    public int getStat_salud() {
        return stat_salud;
    }
    public int getPuntosStatManuales() {
        return puntosStatManuales;
    }
    public ArrayList<EnumTrait> getTraits() {
        return traits;
    }
    /**
     * Aumenta en 1 el ataque fisico
     *
     * @return True si no se ha llegado al limite y quedan puntos. False si se ha llegado al limite o no quedan puntos
     */
    public boolean aumentaAtaqueFisico() {
        if (this.stat_ataque_fisico<25&&this.puntosStatManuales>0) {
            this.stat_ataque_fisico++;
            this.puntosStatManuales--;
            return this.stat_ataque_fisico<25;
         }
        return false;
    }
    /**
     * Aumenta en 1 el ataque magico
     *
     * @return True si no se ha llegado al limite y quedan puntos. False si se ha llegado al limite o no quedan puntos
     */
    public boolean aumentaAtaqueMagico() {
        if (this.stat_ataque_magico<25&&this.puntosStatManuales>0) {
            this.stat_ataque_magico++;
            this.puntosStatManuales--;
            return this.stat_ataque_magico<25;
         }
        return false;
    }
    /**
     * Aumenta en 1 la defensa fisica
     *
     * @return True si no se ha llegado al limite y quedan puntos. False si se ha llegado al limite o no quedan puntos
     */
    public boolean aumentaDefensaFisica() {
        if (this.stat_defensa_fisica<25&&this.puntosStatManuales>0) {
            this.stat_defensa_fisica++;
            this.puntosStatManuales--;
            return this.stat_defensa_fisica<25;
         }
        return false;
    }

    /**
     * Aumenta en 1 el defensa magica
     *
     * @return True si no se ha llegado al limite y quedan puntos. False si se ha llegado al limite o no quedan puntos
     */
    public boolean aumentaDefensaMagica() {
        if (this.stat_defensa_magica<25&&this.puntosStatManuales>0) {
            this.stat_defensa_magica++;
            this.puntosStatManuales--;
            return this.stat_defensa_magica<25;
        }
        return false;
    }
    /**
     * Aumenta en 1 el punteria
     *
     * @return True si no se ha llegado al limite y quedan puntos. False si se ha llegado al limite o no quedan puntos
     */
    public boolean aumentaPunteria() {
        if (this.stat_punteria<25&&this.puntosStatManuales>0) {
            this.stat_punteria++;
            this.puntosStatManuales--;
            return this.stat_punteria<25;
        }
        return false;
    }

    /**
     * Aumenta en 1 la salud
     *
     * @return True si no se ha llegado al limite y quedan puntos. False si se ha llegado al limite o no quedan puntos
     */
    public boolean aumentaSalud() {
        if (this.stat_salud<25&&this.puntosStatManuales>0) {
            this.stat_salud++;
            this.puntosStatManuales--;
            return this.stat_salud<25;
        }
        return false;
    }


    /**
     * Agrega un trait/rasgo
     * @param trait El rasgo a agregar
     * @return True si tiene menos de 3, false si tiene 3 o mas
     */
    public boolean addTrait(EnumTrait trait) {
        if (this.traits.contains(trait) || this.traits.size() > 2)
            return false;
        this.traits.add(trait);
        return true;
    }

    /**
     * Borra los traits/rasgos del personaje
     */
    public void resetTraits(){
        this.traits=new ArrayList<EnumTrait>();
    }

    /**
     * Genera aleatoriamente las stats del personaje si quedan intentos disponibles
     *
     * @return Cantidad de intentos restantes
     */
    public int randomStats() {
        if (this.intentosStatsAzar == 0)
            return 0;
        resetStats();
        this.intentosStatsAzar--;
        this.puntosStatAleatorios=30;
        Random r = new Random();
        while (this.puntosStatAleatorios > 0) {
            int stat = r.nextInt(6);

            switch (stat) {
                case 0:
                    if (this.stat_salud < 20) {
                        this.stat_salud++;
                        this.puntosStatAleatorios--;
                    }
                    break;
                case 1:
                    if (this.stat_ataque_fisico < 20) {
                        this.stat_ataque_fisico++;
                        this.puntosStatAleatorios--;
                    }
                    break;
                case 2:
                    if (this.stat_ataque_magico < 20) {
                        this.stat_ataque_magico++;
                        this.puntosStatAleatorios--;
                    }
                    break;
                case 3:
                    if (this.stat_defensa_fisica < 20) {
                        this.stat_defensa_fisica++;
                        this.puntosStatAleatorios--;
                    }
                    break;
                case 4:
                    if (this.stat_defensa_magica < 20) {
                        this.stat_defensa_magica++;
                        this.puntosStatAleatorios--;
                    }
                    break;
                case 5:
                    if (this.stat_punteria < 20) {
                        this.stat_punteria++;
                        this.puntosStatAleatorios--;
                    }
                    break;
            }
        }
        return this.intentosStatsAzar;
    }

    /**
     * Pone las stats a 10
     */
    private void resetStats() {
        stat_ataque_fisico = 10;
        stat_ataque_magico = 10;
        stat_defensa_fisica = 10;
        stat_defensa_magica = 10;
        stat_punteria = 10;
        stat_salud = 10;
    }
}
