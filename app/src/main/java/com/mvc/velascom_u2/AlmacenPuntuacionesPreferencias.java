package com.mvc.velascom_u2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

/**
 * Author: Mario Velasco Casquero
 * Date: 30/09/2015
 * Email: m3ario@gmail.com
 */
public class AlmacenPuntuacionesPreferencias implements AlmacenPuntuaciones {

    private static final String PREF_ALMACEN = "prefAlmacen";

    private Vector<String> puntuaciones;
    private Context context;

    public AlmacenPuntuacionesPreferencias(Context context) {
        this.context = context;
        puntuaciones = getAlmacenGuardado();
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        puntuaciones.add(0, puntos + " " + nombre);
        PreferencesManager.saveObject(context, PREF_ALMACEN, puntuaciones);
    }



    @Override
    public void listaPuntuaciones(int cantidad, AlmacenListener listener) {
        List<String> list;
        puntuaciones = getAlmacenGuardado();
        if (cantidad > puntuaciones.size()) {
            list = puntuaciones.subList(0, puntuaciones.size());
        } else {
            list = puntuaciones.subList(0, cantidad);
        }
        listener.onDataObtained(new Vector<>(list));
    }

    @SuppressWarnings("unchecked")
    public Vector<String> getAlmacenGuardado() {
        Vector<String> puntuaciones = PreferencesManager.loadObject(context, PREF_ALMACEN, Vector.class, null);
        if (puntuaciones == null) {
            puntuaciones = new Vector<>();
        }

        return puntuaciones;
    }

}
