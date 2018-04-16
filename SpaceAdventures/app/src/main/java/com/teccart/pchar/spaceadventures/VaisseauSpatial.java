package com.teccart.pchar.spaceadventures;

/**
 * Created by pchar on 2018-03-22.
 */

public class VaisseauSpatial{
        private int height;
        private int width;
        private float posX;
        private float posY;
        private String image;

        public VaisseauSpatial(int height, int width, int posX, int posY, String image)
        {
            this.height=height;
            this.width=width;
            this.posX=posX;
            this.posY=posY;
            this.image=image;
        }
        public int getHeight()
        {
            return height;
        }
        public int getWidth()
        {
            return width;
        }
        public float getPosX()
        {
            return posX;
        }
        public void setPosX(float posX)
        {
            this.posX=posX;
        }
        public float getPosY()
        {
            return posY;
        }
        public void setPosY(float posY)
        {
        this.posY=posY;
        }
        public String getImage()
        {
            return image;
        }
}
