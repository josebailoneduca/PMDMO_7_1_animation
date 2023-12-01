package com.example.rpgjjbo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class SelectorClaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EnumClassType claseSeleccionada=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elegir_clase_activity);
        claseSeleccionada = (EnumClassType)getIntent().getExtras().get("CLASE_ACTUAL");
        eligeClase();

    }


    private void eligeClase() {

        //referencias a elementos de interface
        Button btnClaseContinuar = findViewById(R.id.btnRasgosContinuar);
        Spinner spinnerClases = findViewById(R.id.spinnerClases);
        ImageView iconoClase = findViewById(R.id.imagenIconoClase);
        if (claseSeleccionada!=null)
            iconoClase.setImageResource(claseSeleccionada.getImagen());
        //inicializacion de GUI
        //definicion del spinner
        ClasesArrayAdapter adaptador = new ClasesArrayAdapter(this, R.layout.list_classes_item, EnumClassType.values());
        adaptador.setDropDownViewResource(R.layout.list_classes_item_dropdown);
        spinnerClases.setAdapter(adaptador);
        //icono de clase
        iconoClase.setOnClickListener(view -> spinnerClases.performClick());

        //evento de cambio de clase
        spinnerClases.setOnItemSelectedListener(this);
        if(claseSeleccionada!=null)
            spinnerClases.setSelection(claseSeleccionada.getId());

        //evento del boton continuar termina la actividad
        btnClaseContinuar.setOnClickListener((view) -> {

                        Intent miIntent=new Intent();
                        miIntent.putExtra("CLASE_SELECCIONADA",claseSeleccionada);
                        setResult(RESULT_OK,miIntent);
                        finish();

        });
    }



    /**
     * Gestion de spinner de clase. Segun el indice i recoge el valor de EnumClassType
     * y lo agrega como clase al personaje. Ademas modifica el icono segun ese EnumClasstype
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ImageView iconoClase = findViewById(R.id.imagenIconoClase);
        claseSeleccionada=EnumClassType.values()[i];
        iconoClase.setImageResource(EnumClassType.values()[i].getImagen());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}