package com.teccart.pchar.spaceadventures;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RadioButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private RadioButton rbTouch;
    private RadioButton rbAccel;
    private boolean Accel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView wv = (WebView) findViewById(R.id.WebView1);
        wv.loadUrl("file:///android_asset/space.gif");
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);

        rbTouch = findViewById(R.id.radioTouch);
        rbAccel = findViewById(R.id.radioAccel);



    }

    public void startGame(View view) {

        Accel=false;
        classHelper PHPGetter = new classHelper(this);
        PHPGetter.execute();

        if(rbAccel.isChecked())
        {
            Accel=true;
        }

        Intent Facteur = new Intent(this,Main2Activity.class);
        Facteur.putExtra("Accel",Accel);
        startActivity(Facteur);
    }

    public void prepSQLiteDB(String received)
    {

        int cnt=0;
        String demo="";
        ArrayList<String> ListString = new ArrayList<>();

        mySQLiteDBAdapter db=new mySQLiteDBAdapter(this);


        db.open();

        db.deleteAstres();





        //Verifier si Astres sont existants.
        ArrayList<Astre> ListAstres = new ArrayList<Astre>();
        ListAstres=db.selectAllAstres();

        if(ListAstres.size()==0)
        {
            //Aller chercher les infos des Astres et les amener dans la SQlite DB.
            for (int i = 0; i < received.length(); i++) {
                char e = received.charAt(i);
                if (e != '!') {
                    demo += e;
                } else {
                    ListString.add(demo);
                    demo="";
                    cnt++;
                }
            }
            int i = 0;
            while (i < ListString.size()) {
                boolean bool = (Integer.parseInt(ListString.get(i + 3)) == 1);

                db.insertAstre(ListString.get(i + 1), Integer.parseInt(ListString.get(i + 2)),
                        bool, ListString.get(i + 4));
                i += 5;
            }
        }




    }


    // ClassHelper innerClass (AsyncTask) pour aller chercher les planetes dans BD MySQL et les ramener.

    public class classHelper extends AsyncTask<String,String,String>{
        private Context c;
        public AlertDialog ad;



        public classHelper(Context c)
        {
            this.c=c;
        }

        @Override
        protected void onPreExecute() {
            //this.ad=new AlertDialog.Builder(this.c).create();
            //this.ad.setTitle("Checking for Planets . . .");
        }

        @Override
        protected String doInBackground(String... param) {
            String cible="http://192.168.0.183/spaceadventures/selectallastres.php";
            //String cible="http://10.1.13.148/spaceadventures/selectallastres.php";
            try{
                URL url=new URL(cible);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setDoInput(true);


                InputStream ins=con.getInputStream();
                BufferedReader bufr=new BufferedReader(new InputStreamReader(ins,"iso-8859-1"));
                String line;
                StringBuffer sbuff = new StringBuffer();

                while((line=bufr.readLine())!=null)
                {
                    sbuff.append(line+"\n");
                }
                return sbuff.toString();
            }
            catch(Exception ex)
            {
                return ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // code for SQLite (Creation of SQLite DB, Inserts of Data from result)
            prepSQLiteDB(result);

        }
    }
}
