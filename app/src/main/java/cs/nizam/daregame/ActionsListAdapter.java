package cs.nizam.daregame;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by nizamcs on 30/1/16.
 */
public class ActionsListAdapter extends CursorAdapter {

    private int layoutResource;
    private LayoutInflater inflater;
    private float density = 2f;
    private ListView listView;
    private Cursor cursor;

    public ActionsListAdapter(Context context, Cursor c, int flags, int layout) {
        super(context, c, flags);
        layoutResource = layout;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layoutResource, parent, false);
    }

    @Override
    public void bindView(View workingView, Context context, Cursor cursor) {
        final ObjectHolder holder = getAudioObjectHolder(workingView);
        final String entry = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ACTION));

        /* set values here */
        holder.actionText.setText(entry);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mainView.getLayoutParams();
        params.rightMargin = 0;
        params.leftMargin = 0;
        holder.mainView.setLayoutParams(params);
        workingView.setOnTouchListener(new SwipeDetector(holder));

    }

    private ObjectHolder getAudioObjectHolder(View workingView) {
        Object tag = workingView.getTag();
        ObjectHolder holder = null;

        if (tag == null || !(tag instanceof ObjectHolder)) {
            holder = new ObjectHolder();
            holder.mainView = (LinearLayout)workingView.findViewById(R.id.audio_object_mainview);
            holder.deleteView = (RelativeLayout)workingView.findViewById(R.id.deleteview);
            holder.shareView = (RelativeLayout)workingView.findViewById(R.id.shareview);

            /* initialize other views here */
            holder.actionText = (TextView) workingView.findViewById(R.id.action_text);

            workingView.setTag(holder);
        } else {
            holder = (ObjectHolder) tag;
        }

        return holder;
    }

    public void setListView(ListView view) {
        listView = view;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public static class ObjectHolder {
        public LinearLayout mainView;
        public RelativeLayout deleteView;
        public RelativeLayout shareView;

        /* other views here */
        public TextView actionText;
    }

    public class SwipeDetector implements View.OnTouchListener {
        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 30; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX;
        private ObjectHolder holder;

        public SwipeDetector(ObjectHolder h) {
            holder = h;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    float deltaX = downX - upX;

                    if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listView != null && !motionInterceptDisallowed) {
                        listView.requestDisallowInterceptTouchEvent(true);
                        motionInterceptDisallowed = true;
                    }

                    if (deltaX > 0) {
                        holder.deleteView.setVisibility(View.GONE);
                    } else {
                        // if first swiped left and then swiped right
                        holder.deleteView.setVisibility(View.VISIBLE);
                    }

                    swipe(-(int) deltaX);
                    return true;
                }

                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    float deltaX = upX - downX;
                    if (deltaX > MIN_DISTANCE) {
                        // left or right
//                        swipeRemove();
                        Log.d("Nzm", "TODO:delete");
                    } else {
                        swipe(0);
                        Log.d("Nzm", "TODO:Don't delete");
                    }

                    if (deltaX < (-1*MIN_DISTANCE)) {
                        Log.d("Nzm", "TODO:edit");
                    }

                    if (listView != null) {
                        listView.requestDisallowInterceptTouchEvent(false);
                        motionInterceptDisallowed = false;
                    }

//                    holder.deleteView.setVisibility(View.VISIBLE);
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    holder.deleteView.setVisibility(View.VISIBLE);
                    return false;
            }

            return true;
        }

        private void swipe(int distance) {
            View animationView = holder.mainView;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
            params.rightMargin = -distance;
            params.leftMargin = distance;
            animationView.setLayoutParams(params);
        }

        private void swipeRemove() {
//            remove(getItem(position));
            notifyDataSetChanged();
        }
    }

    // swipe detector class here
}
