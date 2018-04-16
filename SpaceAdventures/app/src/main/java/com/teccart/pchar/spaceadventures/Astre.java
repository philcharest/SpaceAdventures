package com.teccart.pchar.spaceadventures;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by pchar on 2018-03-15.
 */

public class Astre{
    private int id;
    private String nom;
    private int taille;
    private boolean status;
    private String url;






    public Astre(int id, String nom, int taille, boolean status, String url)
    {
        this.id=id;
        this.nom=nom;
        this.taille=taille;
        this.status=status;
        this.url=url;


    }

    public int getId()
    {
        return id;
    }
    public String getNom()
    {
        return nom;
    }
    public int getTaille()
    {
        return taille;
    }
    public boolean getStatus()
    {
        return status;
    }
    public void setStatus(boolean state)
    {

    }
    public String getUrl()
    {
        return url;
    }




}
