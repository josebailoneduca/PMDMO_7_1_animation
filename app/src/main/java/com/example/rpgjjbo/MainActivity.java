package com.example.rpgjjbo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Actividad principal de la aplicacion. Va cargando diferentes layout conforme el usuario va avanzando
 * por las opciones de la aplicacion. Se encarga de ir cargando las pantallas y gestionar la logica
 * de las mismas almacenando los datos en un objeto de tipo Personaje. La logica de control de la seleccion
 * de avatar esta en una clase aparte SelectorAvatar
 *
 * @author Jose J. Bailon Ortiz
 */
public class MainActivity extends AppCompatActivity implements TextWatcher {

    /**
     * Almacena el personaje que se va configurando conforme se avanzan en las pantallas
     */
    private Personaje personaje;
    ActivityResultLauncher<EnumClassType> lanzadorSelectorClase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //carga de la pantalla inicial
        setContentView(R.layout.inicio);
        //gestion del boton de inicio
        Button btnInicio = findViewById(R.id.btnInicio);
        btnInicio.setOnClickListener(view -> elijeClase());

        //Lanzador de actividad de seleccion de clase
        lanzadorSelectorClase = registerForActivityResult(
                new ElegirClaseResultContract(),
                (result) -> {
                    manejarResultadoAcitivityClase(result);
                });
    }



    // ELEGIR CLASE

    /**
     * Entrada en la pantalla de seleccion de clase de personaje y genero
     */
    private void elijeClase() {
        //inicializacion del personaje
        this.personaje = new Personaje();
        //carga del layout
        setContentView(R.layout.elegir_clase);

        //referencias a elementos de interface
        Button btnClaseContinuar = findViewById(R.id.btnRasgosContinuar);
        RadioGroup radioGroupGenero = findViewById(R.id.radioGroupGenero);
        Button btnElegirClase = findViewById(R.id.btnElegirClase);
       // Spinner spinnerClases = findViewById(R.id.spinnerClases);
        ImageView iconoClase = findViewById(R.id.imagenIconoClase);
        //inicializacion de GUI
        //definicion del spinner
        ClasesArrayAdapter adaptador = new ClasesArrayAdapter(this, R.layout.list_classes_item, EnumClassType.values());
        adaptador.setDropDownViewResource(R.layout.list_classes_item_dropdown);
        //spinnerClases.setAdapter(adaptador);
        //icono de clase
        //iconoClase.setOnClickListener(view -> spinnerClases.performClick());
        //radiobuttons de genero
        radioGroupGenero.clearCheck();
        btnClaseContinuar.setEnabled(false);

        //evento de cambio de clase
        btnElegirClase.setOnClickListener(view ->{
            lanzadorSelectorClase.launch(personaje.getClase());
        });


        //evento de cambio de genero
        radioGroupGenero.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroupGenero.getCheckedRadioButtonId() != -1)
                btnClaseContinuar.setEnabled(true);
        });

        //evento del boton continuar
        btnClaseContinuar.setOnClickListener((view) -> {
            //comprobar clase seleccionada
            if (this.personaje.getClase() == null) {
                muestraToast(getString(R.string.debes_elegir_una_clase));
                return;
            }

            //almacenamiento de genero seleccionado
            int generoSeleccionado = radioGroupGenero.getCheckedRadioButtonId();
            if (generoSeleccionado == -1) {
                muestraToast(getString(R.string.debes_elegir_un_genero));
                return;
            } else if (generoSeleccionado == R.id.radioButtonMasculino) {
                this.personaje.setGenero(EnumGender.masculino);
            } else if (generoSeleccionado == R.id.radioButtonFemenino) {
                this.personaje.setGenero(EnumGender.femenino);
            }
            //siguiente pantalla
            statsAleatoria();
        });
    }


    /**
     * Maneja el resultado devuelto por la actividad de seleccion de clase
     * @param clase La clase seleccionada
     */
    private void manejarResultadoAcitivityClase(EnumClassType clase) {
        if (clase!=null) {
            personaje.setClase(clase);
            ((ImageView)findViewById(R.id.imagenIconoClase)).setImageResource(personaje.getClase().imagen);
            ((TextView)findViewById(R.id.lbNombreClase)).setText(personaje.getClase().getNombre());
        }
    }




    //STATS ALEATORIOS

    /**
     * Entrada en la pantalla de caracteristicas aleatorias. Gestiona la generacion de valores
     * aleatorios para las caracteristicas de personaje
     */
    @SuppressLint("SetTextI18n")
    private void statsAleatoria() {
        setContentView(R.layout.asignar_stats_random);

        //elementos de interface
        Button btnLanzarDados = findViewById(R.id.btnLanzarDados);
        Button btnContinuar = findViewById(R.id.btnNombreContinuar);
        TextView lbValTiradas = findViewById(R.id.lbValTiradas);
        //inicializacin de interface
        actualizarEtiquetasStats();
        lbValTiradas.setText("" + personaje.getIntentosStatsAzar());

        //evento de boton de tirar dados
        btnLanzarDados.setOnClickListener(view -> {
            //generacion de caracteristicas aleatorias
            int tiradasRestantes = this.personaje.randomStats();
            //actualizacion de interface
            actualizarEtiquetasStats();
            lbValTiradas.setText("" + personaje.getIntentosStatsAzar());
            if (tiradasRestantes == 0)
                btnLanzarDados.setEnabled(false);
            btnContinuar.setEnabled(true);
        });

        //boton continuar a siguiente pantalla
        btnContinuar.setOnClickListener(view -> statsManual());
    }

    /**
     * Actualiza las etiquetas de caracteristicas segun los datos que tiene el personaje
     */
    @SuppressLint("SetTextI18n")
    private void actualizarEtiquetasStats() {
        TextView lbValSalud = findViewById(R.id.lbValSalud);
        TextView lbValAtaqueFisico = findViewById(R.id.lbValAtaqueFisico);
        TextView lbValAtaqueMagico = findViewById(R.id.lbValAtaqueMagico);
        TextView lbValDefensaFisica = findViewById(R.id.lbValDefensaFisica);
        TextView lbValDefensaMagica = findViewById(R.id.lbValDefensaMagica);
        TextView lbValPunteria = findViewById(R.id.lbValPunteria);
        lbValSalud.setText("" + personaje.getStat_salud());
        lbValAtaqueFisico.setText("" + personaje.getStat_ataque_fisico());
        lbValAtaqueMagico.setText("" + personaje.getStat_ataque_magico());
        lbValDefensaFisica.setText("" + personaje.getStat_defensa_fisica());
        lbValDefensaMagica.setText("" + personaje.getStat_defensa_magica());
        lbValPunteria.setText("" + personaje.getStat_punteria());
    }

    //STATS MANUAL

    /**
     * Entrada a la pantalla de caracteristicas manuales. Ofrece la posibilidad de asignar de manera
     * manual puntos a las caracteristicas de personaje. Hace llamadas a la comprobacion de puntos restantes
     * y a la desactivacion de botones si no quedan puntos que asignar
     */
    private void statsManual() {
        setContentView(R.layout.asignar_stats_manual);
        //elementos de interface
        Button btnContinuar = findViewById(R.id.btnStatsManualContinuar);
        ImageButton btnStatSalud = findViewById(R.id.btnStatSalud);
        ImageButton btnStatAtaqueFisico = findViewById(R.id.btnStatAtaqueFisico);
        ImageButton btnStatAtaqueMagico = findViewById(R.id.btnStatAtaqueMagico);
        ImageButton btnStatDefensaFisica = findViewById(R.id.btnStatDefensaFisica);
        ImageButton btnStatDefensaMagica = findViewById(R.id.btnStatDefensaMagica);
        ImageButton btnStatPunteria = findViewById(R.id.btnStatPunteria);
        ImageButton[] botonesManuales = new ImageButton[]{btnStatSalud, btnStatAtaqueFisico, btnStatAtaqueMagico, btnStatDefensaFisica, btnStatDefensaMagica, btnStatPunteria};

        //inicializacion de interface
        btnContinuar.setEnabled(false);
        actualizarEtiquetasStats();
        actualizarBarrasStats();

        //eventos de botones de asignacion de puntos
        //Tras aumentar el punto se sabe si se ha llegado al limite y se llama a la funcion
        //aumentaStatMaNUAL
        btnStatSalud.setOnClickListener(view -> gestionaBotonAumentarStatManual(personaje.aumentaSalud(), view, botonesManuales));
        btnStatAtaqueFisico.setOnClickListener(view -> gestionaBotonAumentarStatManual(personaje.aumentaAtaqueFisico(), view, botonesManuales));
        btnStatAtaqueMagico.setOnClickListener(view -> gestionaBotonAumentarStatManual(personaje.aumentaAtaqueMagico(), view, botonesManuales));
        btnStatDefensaFisica.setOnClickListener(view -> gestionaBotonAumentarStatManual(personaje.aumentaDefensaFisica(), view, botonesManuales));
        btnStatDefensaMagica.setOnClickListener(view -> gestionaBotonAumentarStatManual(personaje.aumentaDefensaMagica(), view, botonesManuales));
        btnStatPunteria.setOnClickListener(view -> gestionaBotonAumentarStatManual(personaje.aumentaPunteria(), view, botonesManuales));

        //evento de boton continuar a siguiente pantalla
        btnContinuar.setOnClickListener(view -> avatarNombreDescripcion());
    }


    /**
     * Gestiona el estado de un boton de agregar punto segun el resultado de intentar agregarlo.
     * Si "exito" es true significa que el boton debe seguir activo. Si es False significa que debe desactivarse
     *
     * @param habilitado      Si el boton debe estar habilitado o no
     * @param view            Vista del boton
     * @param botonesManuales Lista de todos los botones
     */
    private void gestionaBotonAumentarStatManual(boolean habilitado, View view, ImageButton[] botonesManuales) {
        //comprobar si hay que deshabilitar el boton
        if (!habilitado) {
            view.setEnabled(false);
            ((ImageButton) view).setColorFilter(R.color.black);
        }
        //actualizar la interface
        actualizaStatsManual();
        //actualizar el estado de los botones
        comprobarPuntosRestantes(botonesManuales);
    }

    /**
     * Comprueba si quedan puntos que asignar y si no quedan puntos deshabilita
     * todos los botones
     *
     * @param botonesStatsManual Lista de botones
     */
    private void comprobarPuntosRestantes(ImageButton[] botonesStatsManual) {
        if (this.personaje.getPuntosStatManuales() == 0) {
            for (ImageButton btn : botonesStatsManual) {
                btn.setEnabled(false);
                btn.setColorFilter(R.color.black);
            }
            //habilitar el boton de continuar ala pantalla siguiente
            findViewById(R.id.btnStatsManualContinuar).setEnabled(true);
        }
    }

    /**
     * Actualiza las etiquetas y barras de caracteristicas de la interfaz
     */
    private void actualizaStatsManual() {
        actualizarEtiquetasStats();
        actualizarBarrasStats();
    }

    /**
     * Actualiza las barras de caracteristicas de la interfaz
     */
    @SuppressLint("SetTextI18n")
    private void actualizarBarrasStats() {
        //recoger elementos de la interfaz
        ProgressBar progressBarSalud = findViewById(R.id.progressBarSalud);
        ProgressBar progressBarAtaqueFisico = findViewById(R.id.progressBarAtaqueFisico);
        ProgressBar progressBarAtaqueMagico = findViewById(R.id.progressBarAtaqueMagico);
        ProgressBar progressBarDefensaFisica = findViewById(R.id.progressBarDefensaFisica);
        ProgressBar progressBarDefensaMagica = findViewById(R.id.progressBarDefensaMagica);
        ProgressBar progressBarPunteria = findViewById(R.id.progressBarPunteria);

        //actualizar barras
        progressBarSalud.setProgress(this.personaje.getStat_salud());
        progressBarAtaqueFisico.setProgress(this.personaje.getStat_ataque_fisico());
        progressBarAtaqueMagico.setProgress(this.personaje.getStat_ataque_magico());
        progressBarDefensaFisica.setProgress(this.personaje.getStat_defensa_fisica());
        progressBarDefensaMagica.setProgress(this.personaje.getStat_defensa_magica());
        progressBarPunteria.setProgress(this.personaje.getStat_punteria());

        //actualizar etiqueta de puntos restantes si existe
        TextView lbValPuntosRestantes = findViewById(R.id.lbValPuntosRestantes);
        if (lbValPuntosRestantes != null)
            lbValPuntosRestantes.setText("" + personaje.getPuntosStatManuales());
    }


    //AVATAR NOMBRE DESCRIPCION

    /**
     * Inicio de la pantalla de seleccion de avatar y entrada de nombre y biografia
     */
    @SuppressLint("ClickableViewAccessibility")
    private void avatarNombreDescripcion() {
        setContentView(R.layout.nombre_descripcion);
        //recoger elementos de interface
        ImageButton btnAnterior = findViewById(R.id.btnAvatarAnterior);
        ImageButton btnPosterior = findViewById(R.id.btnAvatarPosterior);
        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        Button btnNombreContinuar = findViewById(R.id.btnNombreContinuar);
        EditText inputNombre = findViewById(R.id.inputNombre);
        EditText inputDescripcion = findViewById(R.id.inputDescripcion);
        ImageView fondoNombre = findViewById(R.id.fondoNombre);
        //clase externa que se encarga de la seleccion de avatar
        SelectorAvatar selectorAvatar = new SelectorAvatar(btnAnterior, btnPosterior, imgAvatar, personaje, this);
        //inicializacion de elementos de interface
        btnNombreContinuar.setEnabled(false);
        inputDescripcion.setRawInputType(InputType.TYPE_CLASS_TEXT); // para soportar multilinea y boton de terminar edicion

        //listener de texto cambiado. Se encarga de ir actualizando la etiqueta de caracteres restantes y
        //la visibilidad del boton de continuar
        inputDescripcion.addTextChangedListener(this);

        //soporte de ocultacion del teclado al tocar el fondo de la app
        fondoNombre.setOnTouchListener((view, motionEvent) -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });

        //evento del boton continuar
        btnNombreContinuar.setOnClickListener(view -> {
            personaje.setNombre(inputNombre.getText().toString());
            personaje.setBiografia(inputDescripcion.getText().toString());
            elijeRasgos();
        });
    }

    //RASGOS

    /**
     * Inicio de la pantalla de sleccion de rasgos
     */
    private void elijeRasgos() {
        setContentView(R.layout.rasgos);
        //CHECKBOX DE RASGOS
        CheckBox[] btnRasgos = new CheckBox[8];
        btnRasgos[0] = findViewById(R.id.inputBerserker);
        btnRasgos[1] = findViewById(R.id.inputCauteloso);
        btnRasgos[2] = findViewById(R.id.inputConductorOtto);
        btnRasgos[3] = findViewById(R.id.inputConcentrado);
        btnRasgos[4] = findViewById(R.id.inputHonesto);
        btnRasgos[5] = findViewById(R.id.inputRapido);
        btnRasgos[6] = findViewById(R.id.inputMusculoso);
        btnRasgos[7] = findViewById(R.id.inputEmpollon);
        //agregar eventos a CHECKBOX
        for (CheckBox c : btnRasgos) {
            c.setOnCheckedChangeListener((compoundButton, b) -> manejadorCheckRasgos(btnRasgos));
        }

        //BOTONES DE INFORMACION
        ImageButton[] btnInfoRasgos = new ImageButton[8];
        btnInfoRasgos[0] = findViewById(R.id.btnInfoBerserker);
        btnInfoRasgos[1] = findViewById(R.id.btnInfoCauteloso);
        btnInfoRasgos[2] = findViewById(R.id.btnInfoConductorOtto);
        btnInfoRasgos[3] = findViewById(R.id.btnInfoConcentrado);
        btnInfoRasgos[4] = findViewById(R.id.btnInfoHonesto);
        btnInfoRasgos[5] = findViewById(R.id.btnInfoRapido);
        btnInfoRasgos[6] = findViewById(R.id.btnInfoMusucloso);
        btnInfoRasgos[7] = findViewById(R.id.btnInfoEmpollon);
        //agregar eventos de botones de informacion
        for (ImageButton i : btnInfoRasgos) {
            i.setOnClickListener(view -> manejadorInfoRasgos(view, btnInfoRasgos));
        }

        //boton de continuar a pantalla siguiente
        Button btnRasgosContinuar = findViewById(R.id.btnRasgosContinuar);
        btnRasgosContinuar.setOnClickListener(view -> verFichaDePersonaje());

    }

    /**
     * Controla la habilitacion y deshabilitacion de checkbox de rasgos dejando que como maximo
     * se puedan elegir 3 rasgos
     *
     * @param btnRasgos Lista de checkbox de rasgos
     */
    private void manejadorCheckRasgos(CheckBox[] btnRasgos) {
        //borra los rasgos actuales del personaje
        personaje.resetTraits();
        //recorre los rasgos y los marcados en su checkbox los agrega al personaje
        for (int i = 0; i < btnRasgos.length; i++) {
            if (btnRasgos[i].isChecked()) {
                btnRasgos[i].setTextColor(getColor(R.color.verde_claro));
                switch (i) {
                    case 0:
                        personaje.addTrait(EnumTrait.berserker);
                        break;
                    case 1:
                        personaje.addTrait(EnumTrait.cauteloso);
                        break;
                    case 2:
                        personaje.addTrait(EnumTrait.conductor_otto);
                        break;
                    case 3:
                        personaje.addTrait(EnumTrait.concentrado);
                        break;
                    case 4:
                        personaje.addTrait(EnumTrait.honesto);
                        break;
                    case 5:
                        personaje.addTrait(EnumTrait.rapido);
                        break;
                    case 6:
                        personaje.addTrait(EnumTrait.musculoso);
                        break;
                    case 7:
                        personaje.addTrait(EnumTrait.empollon);
                        break;
                }
            } else {
                btnRasgos[i].setTextColor(getColor(R.color.white));
            }
        }

        //comprobar si hemos llegado a 3 marcados para desactivar el resto
        if (personaje.getTraits().size() == 3) {
            for (CheckBox btnRasgo : btnRasgos) {
                if (!btnRasgo.isChecked()) {
                    btnRasgo.setEnabled(false);
                    btnRasgo.setTextColor(getColor(R.color.rojo_oscuro));
                }
            }

            findViewById(R.id.btnRasgosContinuar).setEnabled(true);
            //en caso de que no sean 3 loas activaos entonces los habilitamos todos
        } else {
            for (CheckBox btnRasgo : btnRasgos) {
                btnRasgo.setEnabled(true);
            }
            findViewById(R.id.btnRasgosContinuar).setEnabled(false);
        }
    }


    /**
     * Maneja los botones de informacion de los rasgos. Detecta el boton pulsado y muestra un toast
     * con la informacion sobre ese rasgo
     *
     * @param view    Boton pulsado
     * @param botones Lista total de botones
     */
    private void manejadorInfoRasgos(View view, ImageButton[] botones) {
        //recorre los botones y si el pulsado es el activo muestra un toast con la informacion
        for (int i = 0; i < botones.length; i++) {
            if (view.equals(botones[i]))
                switch (i) {
                    case 0:
                        muestraToast(getString(R.string.berserker_desc));
                        break;
                    case 1:
                        muestraToast(getString(R.string.cauteloso_desc));
                        break;
                    case 2:
                        muestraToast(getString(R.string.conductor_otto_desc));
                        break;
                    case 3:
                        muestraToast(getString(R.string.concentrado_desc));
                        break;
                    case 4:
                        muestraToast(getString(R.string.honesto_desc));
                        break;
                    case 5:
                        muestraToast(getString(R.string.rapido_desc));
                        break;
                    case 6:
                        muestraToast(getString(R.string.musculoso_desc));
                        break;
                    case 7:
                        muestraToast(getString(R.string.empollon_desc));
                        break;
                }
        }
    }


    //VER FICHA DE PERSONAJE

    /**
     * Punto e entrada a la vista final de la ficha de personaje. Recoge los datos del objeto  Personaje
     * y rellena la interface con sus datos
     */
    private void verFichaDePersonaje() {
        setContentView(R.layout.ficha);
        //recogida de elementos de la interface
        TextView lbNombre = findViewById(R.id.lbNombre);
        TextView lbClase = findViewById(R.id.lbClase);
        TextView lbBiografia = findViewById(R.id.lbBiografia);
        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        ImageView imgGenero = findViewById(R.id.imgGenero);
        TextView lbRasgo1 = findViewById(R.id.lbRasgo1);
        TextView lbRasgo1Desc = findViewById(R.id.lbRasgo1Desc);
        TextView lbRasgo2 = findViewById(R.id.lbRasgo2);
        TextView lbRasgo2Desc = findViewById(R.id.lbRasgo2Desc);
        TextView lbRasgo3 = findViewById(R.id.lbRasgo3);
        TextView lbRasgo3Desc = findViewById(R.id.lbRasgo3Desc);

        //Rellenar datos de la interface
        lbNombre.setText(personaje.getNombre());
        lbClase.setText(getString(personaje.getClase().nombre));
        lbBiografia.setText(personaje.getBiografia());
        imgAvatar.setImageResource(personaje.getAvatar());
        imgGenero.setImageResource(personaje.getGenero().getIcono());
        actualizarEtiquetasStats();
        actualizarBarrasStats();
        ArrayList<EnumTrait> rasgos = personaje.getTraits();
        lbRasgo1.setText(getString(rasgos.get(0).nombre));
        lbRasgo1Desc.setText(getString(rasgos.get(0).descripcion));
        lbRasgo2.setText(getString(rasgos.get(1).nombre));
        lbRasgo2Desc.setText(getString(rasgos.get(1).descripcion));
        lbRasgo3.setText(getString(rasgos.get(2).nombre));
        lbRasgo3Desc.setText(getString(rasgos.get(2).descripcion));


        //boton de crear nuevo personaje
        Button btnFichaNuevoPersonaje = findViewById(R.id.btnFichaNuevoPersonaje);
        btnFichaNuevoPersonaje.setOnClickListener(view -> elijeClase());

    }

    /**
     * Muestra un toast con el mensaje suministrado
     *
     * @param msg Mensaje a mostrar
     */
    private void muestraToast(String msg) {
        Toast t = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        t.show();
    }

    //IMPLEMENTACION DE LA INTERFACE TEXTWATCHER
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable editable) {
        TextView lbCharRestantes = findViewById(R.id.lbCharRestantes);
        lbCharRestantes.setText("(" + (140 - editable.length()) + ")");
        Button btnNombreContinuar = findViewById(R.id.btnNombreContinuar);
        EditText inputNombre = findViewById(R.id.inputNombre);
        EditText inputDescripcion = findViewById(R.id.inputDescripcion);
        btnNombreContinuar.setEnabled(inputNombre.getText().length() > 0 && inputDescripcion.getText().length() > 0);


    }


}