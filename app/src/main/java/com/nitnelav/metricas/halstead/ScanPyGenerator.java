package com.nitnelav.metricas.halstead;

import java.io.File;

public class ScanPyGenerator {
    public static void main(String[] args) {
        String ruta = "/Users/ValentinAbundo/Apps/Android/METRICASDEHALSTEAD/app/src/main/java/com/nitnelav/metricas/halstead/pythonLexer.flex";
        generarLexer(ruta);
    }

    public static void generarLexer(String ruta){
        File archivo = new File(ruta);
        JFlex.Main.generate(archivo);
    }


}
