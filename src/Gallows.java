import acm.graphics.GCompound;
import acm.graphics.GRect;

import java.awt.*;


public class Gallows extends GCompound{
    static GRect base =  new GRect(200, 15);
    static GRect stem = new GRect(15, 200);
    static GRect beam = new GRect(100, 15);
    static GRect noose = new GRect(2, 20);

    public static double[] drawPoint = {noose.getX() + noose.getWidth()/2, noose.getY() + noose.getHeight()};

    public Gallows(int startPosX, int startPosY){
        GCompound gallows = new GCompound();
        add(base, startPosX, startPosY);
        add(stem, base.getX() + base.getWidth() / 2 - stem.getWidth() / 2, startPosY - stem.getHeight());
        add(beam, stem.getX() + stem.getWidth(), stem.getY());
        add(noose, beam.getX() + beam.getWidth() - stem.getWidth(), stem.getY() + beam.getHeight());

        base.setFillColor(Color.black);
        base.setFilled(true);

        stem.setFillColor(Color.black);
        stem.setFilled(true);

        beam.setFillColor(Color.black);
        beam.setFilled(true);

        noose.setFillColor(Color.black);
        noose.setFilled(true);

        add(gallows);
    }

}
