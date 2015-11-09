package com.mvc.velascom_u2;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Vector;

public class Puntuaciones extends ListActivity {

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuaciones);
        progressDialog = showDialog();
        MainActivity.almacen.listaPuntuaciones(10, new AlmacenPuntuaciones.AlmacenListener() {
            @Override
            public void onDataObtained(Vector<String> puntuaciones) {
                setListAdapter(new MiAdaptador(Puntuaciones.this, puntuaciones));
                hideDialog(progressDialog);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView listView,
                                   View view, int position,
                                   long id) {
        super.onListItemClick(listView, view, position, id);
        Object o = getListAdapter().getItem(position);
        Toast.makeText(this, "Selección: " + Integer.toString(position)
                + " - " + o.toString(), Toast.LENGTH_LONG).show();
    }



    public ProgressDialog showDialog() {
        Log.d("", "show dialog");
        return ProgressDialog.show(this, "Cargando ...", "Espere por favor", true, false);
    }

    public void hideDialog(ProgressDialog progressDialog) {
        Log.d("", "dismiss");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
