package com.ruyiruyi.merchant.ui.multiType.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/*步骤：自定义Listener实现RecyclerView.OnItemTouchListener接口 ；
        实现实现接口的三个方法并添加抽象方法onItemClick；*/
public abstract class OnDingDanItemTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;
    //构造
    public OnDingDanItemTouchListener(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        /*实例化GestureDetectorCompat类其中第二个参数是手势监听OnGestureListener，但我们不需要使用它，我们可以实现它的一个简单的
    封装类SimpleOnGestureListener它里面实现的都的空操作，所以我们可以自己实现特定的方法来执行我们的操作。*/
        mGestureDetectorCompat = new GestureDetectorCompat(mRecyclerView.getContext(),new MyGestureListener());
    }

    //拦截触摸事件
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    //处理触摸事件
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    //前两个方法只要向下面用到的GestureDetectorCompat传递MotionEvent为了获取触摸的坐标,最后再定义个abstract回调
    public abstract void onItemClick(RecyclerView.ViewHolder vh);


    /*
        自定义MyGestureListener第一个是简单的点击屏幕时执行，第二个是长按屏幕时执行。所以根据这两个方法，我们就可以从前面OnItemTouchListener传过来的MotionEvent获取点击的位置，
        根据位置使用RecyclerView的findChildViewUnder获取到点击的ItemView,再通过RecyclerView的getChildViewHolder获取ItemView的ViewHolder最后
        再调用OnItemClick方法。
    */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder VH = mRecyclerView.getChildViewHolder(child);
                onItemClick(VH);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }
    }
}