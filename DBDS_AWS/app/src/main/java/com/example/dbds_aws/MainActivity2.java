package com.example.dbds_aws;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dbds_aws.databinding.ActivityMain2Binding;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    int sel;
    String db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sel = 0;
        db="";
    }

    public void selectDBMS(View view){
        RadioButton sql = (RadioButton) findViewById(R.id.sql);
        RadioButton redshift = (RadioButton) findViewById(R.id.redshift);
        if (sql.isChecked()){
            sel=1;
        }
        else if (redshift.isChecked()){
            sel=2;
        }
        Log.wtf("sel", String.valueOf(sel));
    }

    public void selectDB(View view){
        RadioButton inst = (RadioButton) findViewById(R.id.inst);
        RadioButton db2 = (RadioButton) findViewById(R.id.db2);
        if (inst.isChecked()){
            db="instacart";
        }
        else if (db2.isChecked()){
            db="abc";
            /*URL url = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                url = new URL("http://192.168.1.156:8888");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Cache-Control", "no-cache");

                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");

                JSONObject object = new JSONObject();
                object.put("query", "use abc;");
                //object.put("part", value);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(object.toString());
                writer.flush();
                writer.close();
                os.close();

                connection.connect();

                StringBuffer response = new StringBuffer();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.wtf("Answer",response.toString());

            }  catch (Exception e) {
                Log.wtf("error_verify","error_verify");
                e.printStackTrace();
            }*/
        }
        Log.wtf("db", db);
    }

    public void runQuery(View view){
        if (sel==0){
            Toast.makeText(this,"Select DBMS type",Toast.LENGTH_LONG).show();
        }
        else if (db.equals("")){
            Toast.makeText(this,"Select database",Toast.LENGTH_LONG).show();
        }
        else{
            EditText query = (EditText) findViewById(R.id.query);
            URL url = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                url = new URL("http://192.168.1.156:8888");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //httpUrlConnection.setUseCaches(false);
                //httpUrlConnection.setDoOutput(true);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Cache-Control", "no-cache");
                //connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");

                JSONObject object = new JSONObject();
                object.put("query", query.getText().toString());
                object.put("database",db);
                //object.put("part", value);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(object.toString());
                writer.flush();
                writer.close();
                os.close();

                connection.connect();

                StringBuffer response = new StringBuffer();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //Log.wtf("Answer",response.toString());

                JSONObject data = new JSONObject(response.toString());

                if(data.getString("isError").equals("True")){
                    TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
                    tableLayout.removeAllViews();

                    TextView output = (TextView) findViewById(R.id.output);
                    output.setText(data.getString("output"));
                }
                else{
                    TextView output = (TextView) findViewById(R.id.output);
                    output.setText("");

                    //Log.wtf("type",data.get("columns").getClass().getName());
                    //Log.wtf("columns",data.get("columns").toString());
                    String str[] = data.getString("columns").split(",");
                    //Log.wtf("Number of Columns", String.valueOf(str.length));

                    ShapeDrawable border = new ShapeDrawable(new RectShape());
                    border.getPaint().setStyle(Paint.Style.STROKE);
                    border.getPaint().setColor(Color.WHITE);

                    TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
                    tableLayout.removeAllViews();
                    //TableLayout tableLayout = new TableLayout(this);

                    TableRow head = new TableRow(this);
                    for(int i=0; i<str.length; i++){
                        TextView t = new TextView(this);
                        t.setText(str[i]);
                        head.addView(t);
                    }
                    //tableLayout.addView(head);

                    Log.wtf("Head added","done");

                    JSONArray op = data.getJSONArray("output");
                    for(int i=0; i<op.length(); i++){
                        JSONObject row = op.getJSONObject(i);
                        TableRow tableRow = new TableRow(this);
                        tableRow.setPadding(0,0,0,2);
                        //Log.wtf("row",row.toString());
                        for(int j=0; j< row.length(); j++){
                            //Log.wtf("check",str[j]);
                            TextView t = new TextView(this);
                            t.setBackgroundResource(R.drawable.border_text);
                            t.setGravity(Gravity.CENTER);
                            t.setPadding(2,2,2,2);
                            t.setText(row.getString(String.valueOf(j)));
                            tableRow.addView(t);
                        }
                        //tableRow.setBackground(border);
                        tableLayout.addView(tableRow);
                    }
                    //tableLayout1 = tableLayout;
                }

                TextView time = (TextView) findViewById(R.id.time);
                time.setText(new StringBuilder().append("Time Elapsed: ").append(data.getString("time")).toString() + " s");

            }  catch (Exception e) {
                Log.wtf("error_verify","error_verify");
                e.printStackTrace();
            }
        }
    }

}