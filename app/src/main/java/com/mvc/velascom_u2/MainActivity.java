package com.mvc.velascom_u2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int SHAREDPREF_TYPE = 0;
    private static final int INTERNAL_MEMORY_TYPE = 1;
    private static final int EXTERNAL_MEMORY_TYPE = 2;
    private static final int RAW_TYPE = 3;
    private static final int ASSETS_TYPE = 4;
    public static AlmacenPuntuaciones almacen;
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

    private AlmacenPuntuaciones getAlmacen() {
        AlmacenPuntuaciones almacen;
        int tipoAlmacen = getTipoAlmacen();
        switch (tipoAlmacen) {
            default:
            case SHAREDPREF_TYPE:
                almacen = new AlmacenPuntuacionesPreferencias(this);
                break;
            case INTERNAL_MEMORY_TYPE:
                almacen = new AlmacenPuntuacionesFicheroInterno(this);
                break;
            case EXTERNAL_MEMORY_TYPE:
                almacen = new AlmacenPuntuacionesFicheroExterno(this);
                break;
            case RAW_TYPE:
                almacen = new AlmacenPuntuacionesRecursoRaw(this);
                break;
            case ASSETS_TYPE:
                almacen = new AlmacenPuntuacionesRecursoAssets(this);
                break;
        }
        return almacen;
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
        almacen = getAlmacen();
        initMusic();
        if (mp != null) {
            mp.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            int puntuacion = data.getExtras().getInt("puntuacion");
            lanzarDialog(puntuacion);
        }
    }

    private void lanzarDialog(final int puntuacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pon tu nombre");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = input.getText().toString();
                almacen.guardarPuntuacion(puntuacion, nombre, System.currentTimeMillis());
                lanzarPuntuaciones(null);
            }
        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });

        builder.setCancelable(false);
        builder.show();
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
        startActivityForResult(i, 1234);
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


    private boolean canWriteOnMemory() {
        boolean canWrite = true;
        String stadoSD = Environment.getExternalStorageState();
        if (!stadoSD.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "No puedo escribir en la memoria externa", Toast.LENGTH_LONG).show();
            canWrite = false;
        } else if (!stadoSD.equals(Environment.MEDIA_MOUNTED) &&
                !stadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            Toast.makeText(this, "No puedo leer en la memoria externa", Toast.LENGTH_LONG).show();
            canWrite = false;
        }
        return canWrite;
    }

    private int getTipoAlmacen() {
        String value = pref.getString("tipoAlmacen", "0");
        int intValue = Integer.parseInt(value);
        if (intValue == EXTERNAL_MEMORY_TYPE && !canWriteOnMemory()) {
            intValue = INTERNAL_MEMORY_TYPE;
        }
        return intValue;
    }
}
