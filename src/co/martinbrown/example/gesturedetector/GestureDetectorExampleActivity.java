package co.martinbrown.example.gesturedetector;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GestureDetectorExampleActivity extends Activity implements OnTouchListener {

    GestureDetector detector;
    int swipe = 0;
    MyAdapter myAdapter;

    class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = super.getView(position, convertView, parent);

            if(v != null) {
                v.setOnTouchListener(GestureDetectorExampleActivity.this);
            }
            else {
            	Toast.makeText(getApplicationContext(), "View is null", Toast.LENGTH_LONG).show();
            }

            return v;
        }

    };

    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            Toast.makeText(getApplicationContext(), "You double tapped", Toast.LENGTH_SHORT).show();

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            if (e1.getX() - e2.getX() > 50
                    && Math.abs(velocityX) > 50) {
                swipe = -1;

            }

            else if (e2.getX() - e1.getX() > 50
                    && Math.abs(velocityX) > 50) {
                swipe = 1;
            }

            return true;
        }
    }

    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        detector = new GestureDetector(this, new MyGestureDetector());
        
        lv = (ListView) findViewById(R.id.listView1);

        String[] sList = getResources().getStringArray(R.array.bucket_list);

        List<String> myList = new LinkedList<String>();
        for(int i = 0; i < sList.length; i++) {
            myList.add(sList[i]);
        }

        myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, myList);

        lv.setAdapter(myAdapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector.onTouchEvent(event))
            return true;
        else
            return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final View view = v;

        if(detector.onTouchEvent(event)) {

            if(swipe == 1) {

                float x = v.getTranslationX();
                float y = v.getTranslationY();
                TranslateAnimation anim = new TranslateAnimation(x, x + v.getWidth(), y, y);
                anim.setDuration(1000);

                anim.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        myAdapter.remove(((TextView)view).getText().toString());

                        myAdapter.notifyDataSetChanged();
                    }
                });

                v.startAnimation(anim);

            }

            return true;
        }

        swipe = 0;

        return true;
    }
}