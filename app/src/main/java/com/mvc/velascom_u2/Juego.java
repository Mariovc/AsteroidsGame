package com.mvc.velascom_u2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Author: Mario Velasco Casquero
 * Date: 30/09/2015
 * Email: m3ario@gmail.com
 */
public class Juego extends Activity {

    private VistaJuego vistaJuego;
    private int puntuacion = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);
        vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vistaJuego.getThread().pausar();
        vistaJuego.desactivarSensores();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vistaJuego.getThread().reanudar();
        vistaJuego.activarSensores(this);
        vistaJuego.setPadre(this);
    }

    @Override
    protected void onDestroy() {
        vistaJuego.getThread().detener();
        super.onDestroy();
    }
}
