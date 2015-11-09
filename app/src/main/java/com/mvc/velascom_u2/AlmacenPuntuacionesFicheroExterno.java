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
public class AlmacenPuntuacionesFicheroExterno implements AlmacenPuntuaciones {


    private static final String FILE_NAME = "puntuaciones.txt";

    private Vector<String> puntuaciones;
    private Context context;

    public AlmacenPuntuacionesFicheroExterno(Context context) {
        this.context = context;
        puntuaciones = getAlmacenGuardado();
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try {
            FileOutputStream f;
            File file = new File(context.getExternalFilesDir(null).getPath() + "folder", FILE_NAME);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                boolean result = file.createNewFile();
                Log.d("", "result: " + result);
            }
            f = new FileOutputStream(file.getPath(), true);
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
            File file = new File(context.getExternalFilesDir(null).getPath() + "folder", FILE_NAME);
            FileInputStream f = new FileInputStream(file.getPath());
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
