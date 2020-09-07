package com.parhamcodeappsgmail.phoneplus.Fragment.MainCon;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.parhamcodeappsgmail.phoneplus.R;

import io.noties.tumbleweed.Tween;
import io.noties.tumbleweed.android.ViewTweenManager;
import io.noties.tumbleweed.android.types.Alpha;
import io.noties.tumbleweed.android.types.Translation;

/**
 * Created by ravi on 29/09/17.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
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
            final View foregroundView = ((recycleAdaptermain.MyViewHolder) viewHolder).viewForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, @NonNull RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((recycleAdaptermain.MyViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((recycleAdaptermain.MyViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        View hv=((recycleAdaptermain.MyViewHolder) viewHolder).hv;

        if(hv==null)return super.getSwipeDirs(recyclerView, viewHolder);
        else if(hv.getVisibility()==View.VISIBLE)return 0;
        else return super.getSwipeDirs(recyclerView, viewHolder);


    }

    @Override
    public void onChildDraw(Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((recycleAdaptermain.MyViewHolder) viewHolder).viewForeground;
        final View swipel = ((recycleAdaptermain.MyViewHolder) viewHolder).swipeleft;
        final View swiper = ((recycleAdaptermain.MyViewHolder) viewHolder).swiperight;
        if(dX<0){
            swipel.setVisibility(View.VISIBLE);
            swiper.setVisibility(View.GONE);
        }
        else {
            swipel.setVisibility(View.GONE);
            swiper.setVisibility(View.VISIBLE);
        }


        //Log.i("SWIPE", "DX: "+dX);

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            final Float alpha =1 - Math.abs (dX) / ( viewHolder.itemView.getWidth ());
            foregroundView.setAlpha (alpha);
            foregroundView.setTranslationX (dX);
        }
        mainconfrag.swiped=true;



        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
       // mainconfrag.swiped=false;
        final View foregroundView = ((recycleAdaptermain.MyViewHolder) viewHolder).viewForeground;
        Tween.to(foregroundView, Translation.XY, .01f).target(0f, .0F)
                .start(tweenManager(foregroundView));
        nfadein(foregroundView);
    }


    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

    }


    private  ViewTweenManager tweenManager(@NonNull View view) {
        return ViewTweenManager.get(R.id.tween_manager, view);
    }
    private void nfadein(View view){
        Tween.to(view, Alpha.VIEW, .7f)
                .target(1f)
                .start(tweenManager(view));
    }


}
