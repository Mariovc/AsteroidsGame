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
 * Date: 09/11/2015
 * Email: m3ario@gmail.com
 */
public class AlmacenPuntuacionesFicheroInterno implements AlmacenPuntuaciones {


    private static final String FILE_NAME = "puntuaciones.txt";

    private Vector<String> puntuaciones;
    private Context context;

    public AlmacenPuntuacionesFicheroInterno(Context context) {
        this.context = context;
        puntuaciones = getAlmacenGuardado();
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try {
            FileOutputStream f = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            String texto = puntos + " " + nombre + "\n";
            f.write(texto.getBytes());
            f.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
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

    public Vector<String> getAlmacenGuardado() {
        Vector<String> puntuaciones;
        puntuaciones = leerPuntuacionesDeMemoria(10);
        if (puntuaciones == null) {
            puntuaciones = new Vector<>();
        }

        return puntuaciones;
    }

    public Vector<String> leerPuntuacionesDeMemoria(int cantidad) {
        Vector<String> result = new Vector<>();
        try {
            FileInputStream f = context.openFileInput(FILE_NAME);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
            int n = 0;
            String linea;
            do {
                linea = entrada.readLine();
                if (linea != null) {
                    result.add(linea);
                    n++;
                }
            } while (n < cantidad && linea != null);
            f.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        return result;
    }
}
