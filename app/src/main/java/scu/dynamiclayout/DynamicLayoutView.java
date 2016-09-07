package scu.dynamiclayout;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Anthony Harrell on 4/1/2016.
 */
public class DynamicLayoutView extends View {
    private static String[] tempIconsArray = {"Google Now Launcher", "Messaging", "Phone",
            "Camera", "Email", "Settings"};
    private Point size;
    private int height;
    private int width;
    private float centerY;
    private float centerX;
    private float quarterY;
    private float quarterX;
    private float twentyY;
    private float thirtySevenY;

    public DynamicLayoutView (Context context, AttributeSet attrs){
        super(context, attrs);
    }


    public void setScreenParameters(Display display){
        // Getting Screen Size
        size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;

        // Getting center of screen virtical and width
        centerY = height/2;
        centerX = width/2;

        // Getting quarter width of screen
        quarterY = width/4;
        quarterX = width/4;

        // Getting 20% of vertical screen
        twentyY = (float)(height*.20);

        // Getting 37% of vertical screen
        thirtySevenY = (float) (height*0.37);
    }

    /*Default Home Layout Setup
    * inputs: RelativeLayout
    * outputs: none
    * */
    public void setDynamicLayout(RelativeLayout rl, FLDatabaseHelper flHelper){
        // Adding ImageView of home button
        Drawable res = ContextCompat.getDrawable(rl.getContext(), R.drawable.home_button_fl);  // used to be this so not sure if this will work

        ImageView home = new ImageView(rl.getContext());  // used to be this, so not sure if this will work
        home.setId(0);
        if(res != null) {
            home.setImageDrawable(res);
        }
        // Set home button location and add to main layout
        home.setY(centerY - (home.getDrawable().getIntrinsicHeight()/2));
        home.setX(centerX - (home.getDrawable().getIntrinsicWidth()/2));
        rl.addView(home);

        // Rest of dynamic icons
        Drawable appView = null;
        for(int i=0; i<6; i++) {
            ImageView iv = new ImageView(rl.getContext());  // used to be this, so not sure if this will work
            iv.setId(i + 1);

            // Getting application icon
            Cursor c = flHelper.getTableData(flHelper.getTableName1());
            int test = flHelper.getCustomRowNumber(flHelper.getTableName1(),
                    "label", tempIconsArray[i]);
            appView = tempDefaultHomeIcons(c,test, flHelper);

            // Setting
            if (appView != null) {
                iv.setImageDrawable(appView);

                switch(i) {
                    case 0:
                        iv.setY(quarterY - (iv.getDrawable().getIntrinsicHeight() / 2));
                        iv.setX(centerX - (iv.getDrawable().getIntrinsicWidth() / 2));
                        rl.addView(iv);
                        break;
                    case 1:
                        iv.setY(centerY - (iv.getDrawable().getIntrinsicHeight() / 2));
                        iv.setX((width - quarterX) - (iv.getDrawable().getIntrinsicWidth() / 2));
                        rl.addView(iv);
                        break;
                    case 2:
                        iv.setY((height - quarterY) - (iv.getDrawable().getIntrinsicHeight() / 2));
                        iv.setX(centerX - (iv.getDrawable().getIntrinsicWidth() / 2));
                        rl.addView(iv);
                        break;
                    case 3:
                        iv.setY(centerY - (iv.getDrawable().getIntrinsicHeight() / 2));
                        iv.setX(quarterX - (iv.getDrawable().getIntrinsicWidth() / 2));
                        rl.addView(iv);
                        break;
                    case 4:
                        iv.setY(thirtySevenY - (iv.getDrawable().getIntrinsicHeight() / 2));
                        iv.setX(centerX - (iv.getDrawable().getIntrinsicWidth() / 2));
                        rl.addView(iv);
                        break;
                    case 5:
                        iv.setY((height - thirtySevenY)
                                - (iv.getDrawable().getIntrinsicHeight() / 2));
                        iv.setX(centerX - (iv.getDrawable().getIntrinsicWidth() / 2));
                        rl.addView(iv);
                        break;
                    default:
                        continue;
                }
            }
        }
    }

    public Drawable tempDefaultHomeIcons(Cursor cursor, int pos, FLDatabaseHelper flHelper){

        Drawable appIcon;
        cursor.moveToPosition(pos);
        byte[] iconBytes = cursor.getBlob(cursor.getColumnIndex("icon"));
        appIcon = flHelper.bitmapToDrawable(flHelper.byteArrayToBitmap(iconBytes));

        return appIcon;
    }
}
