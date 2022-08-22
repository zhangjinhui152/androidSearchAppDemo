package com.example.tedy.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tedy.R;

public class ItemAnimation extends DefaultItemAnimator {
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        View viewById = holder.itemView.findViewById(R.id.res_card);
        viewById.clearAnimation();
        viewById.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(0f).start();

        return super.animateRemove(holder);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        View viewById = holder.itemView.findViewById(R.id.res_card);
        viewById.clearAnimation();
//        viewById.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(100f).start();
        viewById.animate().setStartDelay(50).setInterpolator(new AccelerateDecelerateInterpolator()).translationY(-15f).start();

//        TextView viewById1 = holder.itemView.findViewById(R.id.tv);
//        Log.d("TAG", "animateAdd: "+viewById1.getText());

        return super.animateAdd(holder);

    }

}
