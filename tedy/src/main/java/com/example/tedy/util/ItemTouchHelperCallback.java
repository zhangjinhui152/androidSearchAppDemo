package com.example.tedy.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback{
    public void setmAdapter(My_RecyclerViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    private My_RecyclerViewAdapter mAdapter;


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
        // swipeFlags为0，即不支持滑动
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        int originalPosition = viewHolder.getAdapterPosition();
//        int targetPosition = target.getAdapterPosition();
//        /***********************代码段一****************************/
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {


    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;//长按启用拖拽
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
