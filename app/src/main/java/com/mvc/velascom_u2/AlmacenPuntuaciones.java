package com.mvc.velascom_u2;

import java.util.Vector;

/**
 * Author: Mario Velasco Casquero
 * Date: 30/09/2015
 * Email: m3ario@gmail.com
 */
public interface AlmacenPuntuaciones {
    public void guardarPuntuacion(int puntos,String nombre,long fecha);
    public void listaPuntuaciones(int cantidad, AlmacenListener listener);

    interface AlmacenListener {
        void onDataObtained(Vector<String> puntuaciones);
    }
}
