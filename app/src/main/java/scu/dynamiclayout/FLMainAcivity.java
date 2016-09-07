package scu.dynamiclayout;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Anthony Harrell on 3/30/2016.
 */
public class FLMainAcivity extends Activity {
    private PackageManager manager;
    private List<AppDetails> apps;                  // Not needed any longer will remove soon
    private Hashtable<String,AppDetails> hashApps;  // Not needed any longer will remove soon
    private FLDatabaseHelper flHelper;
    private Display display;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fl_home_layout);

        // Wallpaper init
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        // Layout init
        final DynamicLayoutView dl = (DynamicLayoutView) findViewById(R.id.flHomeLayout);
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.masterLayout);

        // Setting wallpaper as background
        dl.setBackground(wallpaperDrawable);
        //rl.setBackground(wallpaperDrawable);

        // DB helper init
        flHelper = new FLDatabaseHelper(this);

        // Call other init functions
        getApps();
        display = (getWindowManager().getDefaultDisplay());
        dl.setScreenParameters(display);
        dl.setDynamicLayout(rl,flHelper);

        final GestureDetector gestureDetector = new GestureDetector(this,
                new SwipeListener(FLMainAcivity.this));
        // need to look into this
        // why was this being done???
        // Two relative layouts makes no sense....
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.masterLayout);
        dl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        //flHelper.deleteAllRows(flHelper.getTableName1());
    }

    private void getApps() {
        manager = getPackageManager();
        //apps = new ArrayList<AppDetails>();
        //hashApps = new Hashtable<String,AppDetails>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableActivities) {
            AppDetails app = new AppDetails();
            app.setLabel(ri.loadLabel(manager));
            app.setName(ri.activityInfo.packageName);
            app.setIcon(ri.activityInfo.loadIcon(manager));
            //apps.add(app);
            //hashApps.put(app.getLabel().toString(),app);

            // Filling AppsTable with phone apps
            byte[] iconBytes = flHelper.bitmapToBytesArray(flHelper.drawableToBitmap(app.getIcon()));
            flHelper.insertAppsTable(flHelper.getTableName1(), app.getLabel().toString()
                    , app.getName().toString(), iconBytes);
        }
    }

    private class SwipeListener extends OnSwipeListener {

        public SwipeListener(Context context) {
            super(context);
        }
        public boolean onSwipe(Direction direction) {
            switch(direction) {
                case UP: Toast.makeText(FLMainAcivity.this, "UP", Toast.LENGTH_SHORT).show();
                    System.out.println("The final direction is UP.");
                    break;
                case DOWN: Toast.makeText(FLMainAcivity.this, "DOWN", Toast.LENGTH_SHORT).show();
                    System.out.println("The final direction is DOWN");
                    getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, new SettingsFragment())
                            .commit();
                    break;
                case LEFT: Toast.makeText(FLMainAcivity.this, "LEFT", Toast.LENGTH_SHORT).show();
                    System.out.println("The final direction is LEFT");
                    break;
                case RIGHT: Toast.makeText(FLMainAcivity.this, "RIGHT", Toast.LENGTH_SHORT).show();
                    System.out.println("The final direction is RIGHT");
                    break;
                default: Toast.makeText(FLMainAcivity
                        .this, "Invalid", Toast.LENGTH_SHORT).show();
                    System.out.println("The final direction is Invalid");
                    break;
            }
            return false;
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.layout);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View view = super.onCreateView(inflater, container, savedInstanceState);

            view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            return view;
        }

    }
}

