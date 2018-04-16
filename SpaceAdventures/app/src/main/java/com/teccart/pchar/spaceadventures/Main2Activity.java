package com.teccart.pchar.spaceadventures;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main2Activity extends Activity implements SensorEventListener{



    private static boolean Accel;

// On Acceleration Captor

    private SensorManager sm ;
    private List<Sensor> sensorList;
    private Sensor accelerometer;

    private static int posX;
    private static int posY;
    private static VaisseauSpatial ship;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creation du Vaisseau Spatial

        ship = new VaisseauSpatial(40, 30, posX, posY, "rocket");
        AlienSolarSystem ASS = new AlienSolarSystem(this);
        setContentView(ASS);

        Bundle b = getIntent().getExtras();
        Accel=b.getBoolean("Accel");

        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ALL);

        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float vectorLength;

        vectorLength = (float)Math.sqrt(Math.pow((double)(sensorEvent.values[0]),2)+Math.pow((double)(sensorEvent.values[1]),2)
                + Math.pow((double)(sensorEvent.values[2]),2));

        ship.setPosX(ship.getPosX() + sensorEvent.values[0] * (-1*vectorLength));
        ship.setPosY(ship.getPosY() + sensorEvent.values[1] * vectorLength);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private static class AlienSolarSystem extends View{
        ArrayList<Astre> ListAstres = new ArrayList<>();
        // Position initial du Vaisseau Spatial (initialiser ici pour aller recuperer dans TouchEvent)


        // Afin de suivre les positions des Astres (puisque le guide omettait de mettre dans la BD de l Astre -_- )

        private Drawable AstresDessinee[] = new Drawable[5];
        private static boolean oneTime=false;



        public AlienSolarSystem(Context context) {
            super(context);
            setFocusable(true);
            //Set position depart du Vaisseau Spatial

            mySQLiteDBAdapter db = new mySQLiteDBAdapter(context);
            db.open();
            ListAstres = db.selectAllAstres();


        }

        public Drawable DrawFirstTime(Astre demo, Canvas canvas)
        {
            Drawable temp;

                String planet = demo.getNom();
                int id = getResources().getIdentifier(planet, "drawable", getContext().getPackageName());

                // Randomize positions on Canvas

                Random rand = new Random();
                final int posX = rand.nextInt(canvas.getWidth());
                final int posY = rand.nextInt(canvas.getHeight());


                // Add planets to Canvas
                //temp = getResources().getDrawable(id);
                temp = getResources().getDrawable(id);



                temp.setBounds(posX, posY, posX + demo.getTaille(), posY + demo.getTaille());

                return temp;
        }

        @Override
        protected void onDraw(Canvas canvas) {


            Paint myPaint = new Paint();
            myPaint.setColor(Color.rgb(0, 0, 0));
            myPaint.setStrokeWidth(10);

            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), myPaint);


            for (int i = 0; i < ListAstres.size(); i++) {

                if(!oneTime)
                {
                    Drawable d=DrawFirstTime(ListAstres.get(i),canvas);
                    AstresDessinee[i] = d;
                    d.draw(canvas);
                }
                else
                {
                    Drawable d=AstresDessinee[i];
                    d.draw(canvas);
                }

            }

            oneTime=true;

            //Creation du Vaisseau Spatial

            ship = new VaisseauSpatial(40, 30, posX, posY, "rocket");

            int idship = getResources().getIdentifier(ship.getImage(), "drawable", getContext().getPackageName());

            Drawable dship = getResources().getDrawable(idship);
            dship.setBounds(Math.round(ship.getPosX()), Math.round(ship.getPosY()), Math.round(ship.getPosX() + ship.getWidth()), Math.round(ship.getPosY() + ship.getHeight()));
            dship.draw(canvas);
        }




        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (Accel == false) {


                int action = event.getAction();
                int touchX = (int) event.getX();
                int touchY = (int) event.getY();

                boolean limitL, limitR, LimitU, LimitD = false;

                switch (action) {

                    case MotionEvent.ACTION_MOVE:
                        posX = touchX;
                        posY = touchY;

                        for (int i = 0; i < 5; i++) {

                            limitL = posX > (AstresDessinee[i].getBounds().left - ListAstres.get(i).getTaille());
                            limitR = posX < (AstresDessinee[i].getBounds().right + ListAstres.get(i).getTaille());
                            LimitU = posY > (AstresDessinee[i].getBounds().bottom - ListAstres.get(i).getTaille());
                            LimitD = posY < (AstresDessinee[i].getBounds().top + ListAstres.get(i).getTaille());

                            if (limitL && limitR && LimitD && LimitU) {
                                if (ListAstres.get(i).getStatus()) {

                                    AstresDessinee[i].setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

                                    Toast t = Toast.makeText(this.getContext(), "Planete habitable. SUCCESS", Toast.LENGTH_LONG);
                                    t.show();
                                }


                            }

                        }

                        break;


                }
                invalidate();
                return true;

            }
            return true;
        }


    }
}
