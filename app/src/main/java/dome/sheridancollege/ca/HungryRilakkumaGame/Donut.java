package dome.sheridancollege.ca.HungryRilakkumaGame;

import android.widget.ImageView;

public class Donut {
    private ImageView donut;
    private boolean isDonutAlive;

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


}
