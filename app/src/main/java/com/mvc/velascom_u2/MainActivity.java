package com.mvc.velascom_u2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();
    private MediaPlayer mp;
    private SharedPreferences pref;
    private boolean musicaActivada;
//    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //        tabHost = (FragmentTabHost) findViewById(R.id.mi_tabhost);
//        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Lengüeta 1"),
//                Tab1.class, null);
//        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Lengüeta 2"),
//                Tab2.class, null);
//        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Lengüeta 3"),
//                Tab1.class, null);
    }

    private void initMusic() {
        musicaActivada = pref.getBoolean("musica", false);
        if (musicaActivada) {
            if (mp == null) {
                mp = MediaPlayer.create(this, R.raw.audio);
            }
        } else {
            mp = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null) {
            mp.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMusic();
        if (mp != null) {
            mp.start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado) {
        super.onSaveInstanceState(estadoGuardado);
        if (mp != null) {
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado) {
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null) {
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }
    }

    public void lanzarJuego(View view) {
        Intent i = new Intent(this, Juego.class);
        startActivity(i);
    }

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }

    public void mostrarPreferencias(View view) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música: " + pref.getBoolean("musica", true)
                + ", gráficos: " + pref.getString("graficos", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void lanzarPuntuaciones(View view) {
        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; /** true -> el menú ya está visible */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        } else if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
