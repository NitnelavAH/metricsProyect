package com.nitnelav.metricas.halstead;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class New_Metrics_Activity extends AppCompatActivity {

    private final int CHOOSE_FILE_FROM_DEVICE = 1001;
    private final String TAG = "MainActivity";

    private Button chooseFile_btn;
    private TextView path_file;
    private EditText code_view;
    private String namaFile= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_metrics);

        chooseFile_btn = findViewById(R.id.choose_file_btn);
        path_file = findViewById(R.id.path_file);
        code_view = (EditText) findViewById(R.id.code_view);


        chooseFile_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callChooseFileFromDevice();
            }
        });
    }

    private void callChooseFileFromDevice() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, CHOOSE_FILE_FROM_DEVICE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CHOOSE_FILE_FROM_DEVICE && resultCode == Activity.RESULT_OK) {
            if(resultData != null) {
                Log.d(TAG, "onActivityResult: "+ resultData.getData().getPath());
                Log.d(TAG, "onActivityResult: "+ resultData.getDataString());
                path_file.setText("File Path:" + resultData.getData() );
                try {
                    String code = readTextFromUri(resultData.getData());
                    String resuslt = getTokens(resultData.getData());
                    code_view.setText(code + "\n" + resuslt);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        String name[] = uri.getLastPathSegment().split("/");
        this.namaFile = name[name.length-1];

        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))

        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.length()>0 && !line.startsWith("#")){
                    stringBuilder.append(line + "\n");
                }

            }
        }
        return stringBuilder.toString();
    }

    private String getTokens(Uri uri) {
        int operadores = 0;
        int operandos = 0;
        int errors = 0;
        String resultado = "\nRESULTADOS\n";
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))
        ) {
            LexerAnalizer lexer = new LexerAnalizer(reader, true);
            PyTokens token;

            while ((token = lexer.yylex()) != null) {
            }

            ArrayList<Token> myTokens = lexer.getTokens();
            ArrayList<String> myOperadores = new ArrayList<>();
            ArrayList<String> myOperandos = new ArrayList<>();
            for (Token myToken : myTokens) {
                Log.d(TAG, "onActivityResult: "+ "\n " + myToken);
                switch (myToken.getType()){
                    case "KEYWORD":
                    case "OPERATOR":
                        if(!myToken.getValue().contains(")")){
                            operadores += 1;
                            myOperadores.add(myToken.getValue());
                            Log.d(TAG, "onActivityResult: "+ "\noperadores: " + myToken.getValue());
                        }
                        break;
                    case "LITERAL":
                    case "IDENTIFIER":
                        operandos +=1;
                        myOperandos.add(myToken.getValue());
                        Log.d(TAG, "onActivityResult: "+ "\noperandos: " + myToken.getValue());
                        break;
                    default:
                }
            }
            Log.d(TAG, "onActivityResult: "+ "\n-------------------");
            ArrayList<String> n1Operadores = new ArrayList<>();
            ArrayList<String> n2Operandos = new ArrayList<>();

            for (String myToken : myOperadores) {
                if(!n1Operadores.contains(myToken) || n1Operadores.size() < 1){
                    n1Operadores.add(myToken);
                    Log.d(TAG, "onActivityResult: "+ "\noperadores: " + myToken);
                }

            }
            for (String myToken : myOperandos) {
                if (!n2Operandos.contains(myToken) || n2Operandos.size() < 1){
                    n2Operandos.add(myToken);
                    Log.d(TAG, "onActivityResult: "+ "\noperandos: " + myToken);
                }

            }

            resultado += "MÉTRICAS BÁSICAS \n\t Total Operadores N1: " + operadores + "\n\t" + "Operadores n1: " +
                    n1Operadores.size() + "\n\t" + "Total Operandos N2: " +
                    operandos + "\n\t" + "Operandos n2: " + n2Operandos.size() + "\n\n";

            //MÉTRICAS DERIVADAS
            resultado += "MÉTRICAS DERIVADAS \n\t";
            // LONGITUD DEL PROGRAMA (P):
            int N = operadores + operandos;
            resultado += "LONGITUD DEL PROGRAMA (P): "+ N +" \n\t";

            //VOCABULARIO DEL PROGRAMA:
            int n = n1Operadores.size() + n2Operandos.size();
            resultado += "VOCABULARIO DEL PROGRAMA: "+ n +" \n\t";

            // VOLUMEN DEL PROGRAMA:
            Double V = N * (Math.log(n)/Math.log(2));
            resultado += "VOLUMEN DEL PROGRAMA: "+ V +" \n\t";

            //DIFICULTAD DEL PROGRAMA:
            Double D = (Double.valueOf(n1Operadores.size())/2)*(Double.valueOf(operandos)/Double.valueOf(operandos));
            resultado += "DIFICULTAD DEL PROGRAMA: "+ D +" \n\t";

            //NIVEL DEL PROGRAMA:
            Double L = 1/D;
            resultado += "NIVEL DEL PROGRAMA: "+ L +" \n\t";

            //ESFUERZO DE IMPLEMENTACIÓN:
            Double E = D*V;
            resultado += "ESFUERZO DE IMPLEMENTACIÓN: "+ E +" \n\t";

            //TIEMPO DE IMPLEMENTACIÓN:
            Double T = E/18;
            resultado += "TIEMPO DE IMPLEMENTACIÓN: "+ T +" \n\t";

            //NÚMERO DE BUGS LIBERADOS:
            Double B = Math.pow(T,2/3)/3000;
            resultado += "NÚMERO DE BUGS LIBERADOS: "+ B +" \n";

            DBQuery admin = new DBQuery(getApplicationContext(),"administracion",null,1);
            SQLiteDatabase base = admin.getWritableDatabase();

            ContentValues registro = new ContentValues();
            String name = this.namaFile;
            Date current = new Date();
            registro.put("nombre",name + "-" + current.toString());
            registro.put("n1",n1Operadores.size());
            registro.put("N_1",operadores);
            registro.put("n2",n2Operandos.size());
            registro.put("N_2", operandos);
            registro.put("N",N);
            registro.put("n_",n);
            registro.put("V",V);
            registro.put("D",D);
            registro.put("L",L);
            registro.put("E",E);
            registro.put("T",T);
            registro.put("B",B);
            base.insert("Metricas",null,registro);
            base.close();

            Toast.makeText(getApplicationContext(),name + "-" + current.toString()+ "Guardado",Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }
}