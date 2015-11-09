package com.mvc.velascom_u2;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

/**
 * Author: Mario Velasco Casquero
 * Date: 09/11/2015
 * Email: m3ario@gmail.com
 */
public class AlmacenPuntuacionesSW_PHP implements AlmacenPuntuaciones {

    public void listaPuntuaciones(int cantidad, AlmacenListener listener) {
        new ListaPuntuacionesTask(cantidad, listener).execute();
    }

    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        new GuardarPuntuacionTask(puntos, nombre, fecha).execute();
    }

    private class GuardarPuntuacionTask extends AsyncTask<Void, Void, Void> {

        int puntos;
        String nombre;
        long fecha;


        public GuardarPuntuacionTask(int puntos, String nombre, long fecha) {
            this.puntos = puntos;
            this.nombre = nombre;
            this.fecha = fecha;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://158.42.146.127/puntuaciones/nueva.php"
                        + "?puntos=" + puntos
                        + "&nombre=" + URLEncoder.encode(nombre, "UTF-8")
                        + "&fecha=" + fecha);
                HttpURLConnection conexion = (HttpURLConnection) url

                        .openConnection();
                if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new

                            InputStreamReader(conexion.getInputStream()));
                    String linea = reader.readLine();
                    if (!linea.equals("OK")) {
                        Log.e("Asteroides", "Error en servicio Web nueva");
                    }
                } else {
                    Log.e("Asteroides", conexion.getResponseMessage());
                }
                conexion.disconnect();
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
            }
            return null;
        }
    }

    private class ListaPuntuacionesTask extends AsyncTask<Void, Void, Vector<String>> {

        private int cantidad;
        private AlmacenListener listener;

        public ListaPuntuacionesTask(int cantidad, AlmacenListener listener) {
            this.cantidad = cantidad;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Vector<String> doInBackground(Void... voids) {
            Vector<String> result = new Vector<>();
            HttpURLConnection conexion = null;
            try {
                URL url = new URL("http://158.42.146.127/puntuaciones/lista.php"
                        + "?max=20");
                conexion = (HttpURLConnection) url.openConnection();
                if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conexion.getInputStream()));
                    String linea = reader.readLine();
                    while (!linea.equals("")) {
                        result.add(linea);
                        linea = reader.readLine();
                    }
                    reader.close();
                    conexion.disconnect();
                } else {
                    Log.e("Asteroides",
                            conexion.getResponseMessage());
                    conexion.disconnect();
                }
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
            } finally {
                if (conexion != null) {
                    conexion.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Vector<String> result) {
            listener.onDataObtained(result);
        }
    }
}
