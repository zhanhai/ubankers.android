package cn.com.ubankers.www.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

    public class MainListView extends ListView {
            // 自定义ListView
            private float xDistance, yDistance, xLast, yLast;
            public MainListView(Context context, AttributeSet attrs, int defStyle) {
                    super(context, attrs, defStyle);
            }

            public MainListView(Context context, AttributeSet attrs) {
                    super(context, attrs);
            }

            public MainListView(Context context) {
                    super(context);
            }
           
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                    switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                            xDistance = yDistance = 0f;
                            xLast = ev.getX();
                            yLast = ev.getY();
                            break;
                    case MotionEvent.ACTION_MOVE:
                            final float curX = ev.getX();
                            final float curY = ev.getY();

                            xDistance += Math.abs(curX - xLast);
                            yDistance += Math.abs(curY - yLast);
                            xLast = curX;
                            yLast = curY;

                            if (xDistance > yDistance) {
                                    return false;   
                            }
                    }

                    return super.onInterceptTouchEvent(ev);
            }
            
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // TODO Auto-generated method stub
            	int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
            }
//       	
            
    }