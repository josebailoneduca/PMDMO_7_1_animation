package com.example.rpgjjbo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Result contract para el traspaso de información entre MainActivity y SelectorClaseActivity.
 *
 * Este contrato tiene como envío y retorno una EnumClassType para enviar la clase actual del personaje
 * y recibir de vuelta la clase seleccionada.
 */
public class ElegirClaseResultContract extends ActivityResultContract<EnumClassType,EnumClassType> {


    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, EnumClassType clase) {
        Intent intent = new Intent(context, SelectorClaseActivity.class);
        intent.putExtra("CLASE_ACTUAL",clase);
        return intent;
    }

    @Override
    public EnumClassType parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode != Activity.RESULT_OK || intent == null) {
            return null;
        }
        EnumClassType res= (EnumClassType) intent.getExtras().get("CLASE_SELECCIONADA");
        return res;
    }
}
