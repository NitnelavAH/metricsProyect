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
                    Log.d(TAG, "onActivityResult: "+ code);
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
        String resultado = "\n ----RESULTADOS---- \n";
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))
        ) {
            LexerAnalizer lexer = new LexerAnalizer(reader, true);
            PyTokens token;
            ArrayList<String> myTokens = new ArrayList<String>();
            while ((token = lexer.yylex()) != null) {
                Log.d(TAG, "onActivityResult: " + lexer.yylex());
                Log.d(TAG, "onActivityResult: " + lexer.yytext());
                Log.d(TAG, "onActivityResult: " + lexer.getReviewString());
                myTokens.add(lexer.yytext());
                switch (lexer.yylex()){
                    case KEYWORD:
                    case OPERATOR:

                            operadores += 1;
                            resultado += lexer.yytext() + " --> Operador \n";
                        break;
                    case LITERAL:
                    case IDENTIFIER:

                        operandos +=1;
                        resultado += lexer.yytext() + " --> Operando \n";

                        break;

                    default:
                }
            }
            String result = lexer.getReviewString();
            resultado += result;
            resultado += "\nNo. Operadores: "+ operadores+"\nNo.Operandos: "+operandos;
            lexer.printReview();


            Map<String, Integer> tokenCount = new HashMap<>();

            for (String myToken : myTokens) {

                    Integer count = tokenCount.get(myToken);
                    if (count == null)
                        count = 0; // first time this token was found

                    tokenCount.put(myToken, count+1);

            }

            for (Map.Entry entry : tokenCount.entrySet())
            {
                Log.d(TAG, "onActivityResult: "+ "key: " + entry.getKey() + "; value: " + entry.getValue());
            }

            /*while(true){
                Tokens tokens = lexer.yylex();
                Log.d(TAG, "onActivityResult: " + lexer.lexeme);
                if (tokens == null) {
                    resultado += "No. Operadores: " + operadores + "\n" + "No. Operandos: " + operandos + "\n" + "No. Errores: " + errors + "\n";
                    break;
                }
                switch (tokens) {
                    case Operador:
                        operadores += 1;
                        resultado += lexer.lexeme + ":Es un " + tokens + "\n";
                        break;
                    case Operando:
                        operandos += 1;
                        resultado += lexer.lexeme + ":Es un " + tokens + "\n";
                        break;
                    case ERROR:
                        errors += 1;
                        resultado += lexer.lexeme + ":Es un " + tokens + "\n";
                    default:
                        resultado += "Token: " + tokens + '\n';
                        break;
                }
            }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }
}