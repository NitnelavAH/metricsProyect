package com.nitnelav.metricas.halstead;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
                    operandos + "\n\t" + "Operandos n1: " + n2Operandos.size() + "\n";

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }
}