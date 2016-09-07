package scu.dynamiclayout;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * Created by praneethkollareddy on 4/10/16.
 */
public class OnSwipeListener extends GestureDetector.SimpleOnGestureListener {
    Context context;

    public OnSwipeListener (Context context) {
        this.context = context;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
        // Let e1 be the initial event
        // e2 can be located at 4 different positions, consider the following diagram
        // (Assume that lines are separated by 90 degrees.)
        //
        //
        //         \ A  /
        //          \  /
        //       D   e1   B
        //          /  \
        //         / C  \
        //
        // So if (x2,y2) falls in region:
        //  A => it's an UP swipe
        //  B => it's a RIGHT swipe
        //  C => it's a DOWN swipe
        //  D => it's a LEFT swipe
        //

        float x1 = e1.getX();
        float y1 = e1.getY();

        float x2 = e2.getX();
        float y2 = e2.getY();

        Direction direction = getDirection(x1, y1, x2, y2);
        Direction position = getInitPosition(x1, y1);
        if (direction == position) {
            System.out.println("This should only be followed by a valid direction");
            return onSwipe(direction);
        } else {
            System.out.println("This should only be followed by Invalid");
            return onSwipe(Direction.INVALID);
        }
    }

    public boolean onSwipe(Direction direction){
        return false;
    }

    /**
     * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
     * returns the direction that an arrow pointing from p1 to p2 would have.
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the direction
     */
    public Direction getDirection(float x1, float y1, float x2, float y2){
        double angle = getAngle(x1, y1, x2, y2);
        return Direction.get(angle);
    }

    /**
     *
     * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
     * The angle is measured with 0/360 being the X-axis to the right, angles
     * increase counter clockwise.
     *
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the angle between two points
     */
    public double getAngle(float x1, float y1, float x2, float y2) {

        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        return (rad*180/Math.PI + 180)%360;
    }

    public Direction getInitPosition(float x1, float y1) {
        System.out.println("The initial position is (" + x1 + ", " + y1 + ")");
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        System.out.println("The height is " + height + " and the width is " + width);

        if (y1 > x1*height/width && y1 < (-x1*height/width + height) && x1 <= width/2) {
            System.out.println("The initial position is LEFT");
            return Direction.LEFT;
        } else if (y1 < x1*height/width && y1 > (-x1*height/width + height) && x1 >= width/2) {
            System.out.println("The initial position is RIGHT");
            return Direction.RIGHT;
        } else if (x1 < y1*height/width && x1 > (-y1*height/width + width) && y1 <= height/2) {
            System.out.println("The initial position is UP");
            return Direction.UP;
        } else if (x1 < y1*width/height && x1 > (-y1*width/height + width) && y1 >= height/2) {
            System.out.println("The initial position is DOWN");
            return Direction.DOWN;
        } else {
            System.out.println("The initial position is INVALID");
            return Direction.INVALID;
        }
    }


    public enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        INVALID;

        /**
         * Returns a direction given an angle.
         * Directions are defined as follows:
         *
         * Up: [45, 135]
         * Right: [0,45] and [315, 360]
         * Down: [225, 315]
         * Left: [135, 225]
         *
         * @param angle an angle from 0 to 360 - e
         * @return the direction of an angle
         */
        public static Direction get(double angle){
            if (inRange(angle, 45, 135)) {
                System.out.println("The swipe direction is UP");
                return Direction.UP;
            } else if (inRange(angle, 0,45) || inRange(angle, 315, 360)) {
                System.out.println("The swipe direction is RIGHT");
                return Direction.RIGHT;
            } else if (inRange(angle, 225, 315)) {
                System.out.println("The swipe direction is DOWN");
                return Direction.DOWN;
            } else if (inRange(angle, 135, 225)) {
                System.out.println("The swipe direction is LEFT");
                return Direction.LEFT;
            } else {
                return Direction.INVALID;
            }
        }

        /**
         * @param angle an angle
         * @param init the initial bound
         * @param end the final bound
         * @return returns true if the given angle is in the interval [init, end).
         */
        private static boolean inRange(double angle, float init, float end){
            return (angle >= init) && (angle < end);
        }

        // private static boolean inQuad()
    }
}