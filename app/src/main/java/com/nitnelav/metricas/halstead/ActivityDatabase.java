package com.nitnelav.metricas.halstead;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ActivityDatabase extends AppCompatActivity {

    private TableLayout tabla;
    private Button regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        this.tabla = (TableLayout) findViewById(R.id.tabla_layout);
        this.regresar = (Button) findViewById(R.id.btn_regresar);

        DBQuery admin = new DBQuery(getApplicationContext(),"administracion",null,1);
        SQLiteDatabase Base = admin.getWritableDatabase();
        Cursor fila = Base.rawQuery("SELECT nombre,n1,N_1,n2,N_2,N,n_,V,D,L,E,T,B FROM Metricas",null);


        initTable();
        TableRow row = new TableRow(this);
        TextView tv;
        if (fila.moveToFirst())
        {
            do {
                row = new TableRow(this);
                String nombre = fila.getString(0);
                String n1 = fila.getString(1);
                String N1 = fila.getString(2);
                String n2 = fila.getString(3);
                String N2 = fila.getString(4);
                String n = fila.getString(5);
                String N = fila.getString(6);
                String V = fila.getString(7);
                String D = fila.getString(8);
                String L = fila.getString(9);
                String E = fila.getString(10);
                String T = fila.getString(11);
                String B = fila.getString(12);

                tv = newRow(nombre);
                row.addView(tv);

                tv = newRow(n1);
                row.addView(tv);

                tv = newRow(N1);
                row.addView(tv);

                tv = newRow(n2);
                row.addView(tv);

                tv = newRow(N2);
                row.addView(tv);

                tv = newRow(n);
                row.addView(tv);

                tv = newRow(N);
                row.addView(tv);

                tv = newRow(V);
                row.addView(tv);

                tv = newRow(D);
                row.addView(tv);

                tv = newRow(L);
                row.addView(tv);

                tv = newRow(E);
                row.addView(tv);

                tv = newRow(T);
                row.addView(tv);

                tv = newRow(B);
                row.addView(tv);

                tabla.addView(row);
            } while(fila.moveToNext());
        }

        this.regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private TextView newRow(String texto)
    {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(15);
        tv.setTextColor(Color.BLACK);
        tv.setLayoutParams(new TableRow.LayoutParams(200, 120));

        return tv;
    }

    private TextView newHeader(String texto)
    {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        tv.setTextColor(Color.rgb(10,99,13));
        tv.setLayoutParams(new TableRow.LayoutParams(200, 50));

        return tv;
    }

    private void initTable()
    {
        TableRow row = new TableRow(this);
        TextView tv = newHeader("Nombre P");
        row.addView(tv);

        tv = newHeader("n1");
        row.addView(tv);

        tv = newHeader("N1");
        row.addView(tv);

        tv = newHeader("n2");
        row.addView(tv);

        tv = newHeader("N2");
        row.addView(tv);

        tv = newHeader("N");
        row.addView(tv);

        tv = newHeader("n");
        row.addView(tv);

        tv = newHeader("V");
        row.addView(tv);

        tv = newHeader("D");
        row.addView(tv);

        tv = newHeader("L");
        row.addView(tv);

        tv = newHeader("E");
        row.addView(tv);

        tv = newHeader("T");
        row.addView(tv);

        tv = newHeader("B");
        row.addView(tv);

        tabla.addView(row);
    }
}