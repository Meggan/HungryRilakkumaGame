package dome.sheridancollege.ca.HungryRilakkumaGame;

import android.widget.ImageView;

public class Donut extends MainActivity{
    private ImageView donut;
    private boolean isDonutAlive;
    final int MIN_X =0;
    int MAX_X ;
    final int MIN_Y =0;
    int MAX_Y;

    public Donut(){

    }

    //constructor for donut
    public Donut(ImageView donut, boolean isDonutAlive){
        this.donut = donut;
        this.isDonutAlive = isDonutAlive;
    }


    public ImageView getDonut() {
        return donut;
    }

    public boolean isDonutAlive() {
        return isDonutAlive;
    }

    public void setDonutAlive(boolean donutAlive) {
        isDonutAlive = donutAlive;
    }

    public void setDonut(ImageView donut) {
        this.donut = donut;
    }


    public void createDonut(Donut donut, int MAX_X, int MAX_Y){
        //if there is no donut out, create new one
        double dx;
        double dy;

        do{
            dx = (Math.random()*(((MAX_X) - MIN_X) + 1)) + donut.getDonut().getLayoutParams().width;
        }while(dx>MAX_X-donut.getDonut().getLayoutParams().width || dx<(donut.getDonut().getLayoutParams().width));

        do{
            dy = (Math.random()*((MAX_Y - MIN_Y) + 1)) + donut.getDonut().getLayoutParams().height;
        }while(dy>MAX_Y-donut.getDonut().getLayoutParams().height || dy<(donut.getDonut().getLayoutParams().height));

        System.out.println("X: " + dx);
        System.out.println("Y: " + dy);
        donut.getDonut().setTranslationX((float)dx);
        donut.getDonut().setTranslationY((float)dy);
        isDonutAlive=true;
    }
}
