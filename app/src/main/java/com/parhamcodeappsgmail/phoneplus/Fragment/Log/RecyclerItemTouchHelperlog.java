package com.parhamcodeappsgmail.phoneplus.Fragment.Log;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Tools.CitemTouchHelper;
import com.parhamcodeappsgmail.phoneplus.Tools.Message;

import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.android.ViewTweenManager;
import io.noties.tumbleweed.android.types.Alpha;
import io.noties.tumbleweed.android.types.Translation;

/**
 * Created by ravi on 29/09/17.
 */

public class RecyclerItemTouchHelperlog extends CitemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;
     View foregroundView ;
    public RecyclerItemTouchHelperlog(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((recycleAdapterlog.MyViewHolder) viewHolder).logitm;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((recycleAdapterlog.MyViewHolder) viewHolder).logitm;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((recycleAdapterlog.MyViewHolder) viewHolder).logitm;
        getDefaultUIUtil().clearView(foregroundView);
//        Message.message(foregroundView.getContext(),"ClearView called",1);
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        View hv=((recycleAdapterlog.MyViewHolder) viewHolder).hv;
        if(hv==null)return super.getSwipeDirs(recyclerView, viewHolder);
        else if(hv.getVisibility()==View.VISIBLE)return 0;
        else return super.getSwipeDirs(recyclerView, viewHolder);


    }

    @Override
    public void onChildDraw(Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((recycleAdapterlog.MyViewHolder) viewHolder).logitm;
        final View swipel = ((recycleAdapterlog.MyViewHolder) viewHolder).swipelogleft;
        final View swiper = ((recycleAdapterlog.MyViewHolder) viewHolder).swipelogright;
        if(dX<0){
            swipel.setVisibility(View.VISIBLE);
            swiper.setVisibility(View.GONE);
        }
        else {
            swipel.setVisibility(View.GONE);
            swiper.setVisibility(View.VISIBLE);
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            final float alpha =1 - Math.abs (dX) / ( viewHolder.itemView.getWidth ());
            foregroundView.setAlpha (alpha);
            foregroundView.setTranslationX (dX);
        }

        recentconfrag.swipedlog=true;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        final View foregroundView = ((recycleAdapterlog.MyViewHolder) viewHolder).logitm;
//        foregroundView.setVisibility(View.VISIBLE);
        Tween.to(foregroundView, Translation.XY, .01f).target(0f, .0F)
                .start(tweenManager(foregroundView));
        nfadein(foregroundView);
        recentconfrag.swipedlog=false;
//        Message.message(foregroundView.getContext(),"onswipe called",1);

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

    }
    @NonNull
    private ViewTweenManager  tweenManager(@NonNull View view)

    {
        return ViewTweenManager.get(R.id.tween_manager, view);
    }
    private void  nfadein(View view){
        Tween.to(view, Alpha.VIEW, .7f)
                .target(1f)
                .start(tweenManager(view));
    }

    public void restoreswipe(){

    }
}
