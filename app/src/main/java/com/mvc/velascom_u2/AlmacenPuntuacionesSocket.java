package com.mvc.velascom_u2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

/**
 * Author: Mario Velasco Casquero
 * Date: 09/11/2015
 * Email: m3ario@gmail.com
 */
public class AlmacenPuntuacionesSocket implements AlmacenPuntuaciones {

    private static final String SERVER_ADDRESS = "158.42.146.127";
    private static final int SERVER_PORT = 1234;



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
                Socket sk = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(sk.getInputStream()));
                PrintWriter salida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
                salida.println(puntos + " " + nombre);
                String respuesta = entrada.readLine();
                if (!respuesta.equals("OK")) {
                    Log.e("Asteroides", "Error: respuesta de servidor incorrecta");
                }
                sk.close();
            } catch (Exception e) {
                Log.e("Asteroides", e.toString(), e);
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
            try {
                Socket sk = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(sk.getInputStream()));
                PrintWriter salida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
                salida.println("PUNTUACIONES");
                int n = 0;
                String respuesta;
                do {
                    respuesta = entrada.readLine();
                    if (respuesta != null) {
                        result.add(respuesta);
                        n++;
                    }
                } while (n < cantidad && respuesta != null);
                sk.close();
            } catch (Exception e) {
                Log.e("Asteroides", e.toString(), e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Vector<String> result) {
            listener.onDataObtained(result);
        }
    }


}
