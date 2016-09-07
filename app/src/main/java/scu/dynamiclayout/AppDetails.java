package scu.dynamiclayout;

import android.graphics.drawable.Drawable;

/**
 * Created by Anthony Harrell on 4/3/2016.
 */

public class AppDetails{
    private CharSequence label;
    private CharSequence name;
    private Drawable icon;

    public void setLabel(CharSequence label){
        this.label = label;
    }

    public void setName(CharSequence name){
        this.name = name;
    }

    public void setIcon(Drawable drawable){
        this.icon = drawable;
    }

    public CharSequence getLabel(){
        return this.label;
    }

    public CharSequence getName(){
        return this.name;
    }

    public  Drawable getIcon(){
        return this.icon;
    }
}
