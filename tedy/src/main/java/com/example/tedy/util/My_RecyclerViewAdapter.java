package com.example.tedy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tedy.R;
import com.example.tedy.bean.ApKData;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class My_RecyclerViewAdapter extends RecyclerView.Adapter<My_RecyclerViewAdapter.My_ViewHolder> {

    private List<ApKData> data;
    private Context context;
    DBManger dbManger;

    public void onItemMove(int viewHolderPosition, int targetViewHolderPosition) {

        // Log.d("onItemMove", "onItemMove: "+targetViewHolderPosition+"----------"+viewHolderPosition);
//        Collections.swap(data,viewHolderPosition,targetViewHolderPosition);
//        Log.d("onItemMove", "onItemMove: "+data+"----------");
    }

    public My_RecyclerViewAdapter(List<ApKData> data, Context context, DBManger dbManger) {
        this.data = data;
        this.context = context;
        this.dbManger = dbManger;
    }

    public void pop() {
        data.remove(data.size() - 1);
    }

    public void removeAll() {

//        data.removeAll(data);
        notifyItemRangeRemoved(0,data.size());
        data.clear();
    }

    public void cloneList(List<ApKData> list) {
        notifyItemInserted(data.size());
        data.addAll(list);
    }


    @NonNull
    @Override
    public My_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.res_card, null);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_card, parent, false);
        return new My_ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull My_ViewHolder holder, int position) {
        byte[] apkIcon = data.get(position).getApkIcon();
        Bitmap bitmapFromByte = My_AndroidUtil.byteToBitmap(apkIcon);


        holder.apkicon.setImageBitmap(bitmapFromByte);
        holder.name.setText(data.get(position).getApkName());
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class My_ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        MaterialCardView resCard;
        ImageView apkicon;

        public My_ViewHolder(@NonNull View itemView) {
            super(itemView);
            resCard = itemView.findViewById(R.id.res_card);
            name = itemView.findViewById(R.id.tv);
            apkicon = itemView.findViewById(R.id.apkicon);
//            resCard.animate().setInterpolator(new DecelerateInterpolator()).scaleX(1.0f).setDuration(500).start();
            MaterialCardView card = itemView.findViewById(R.id.res_card);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    CharSequence text = name.getText();
                    Log.d(" public void onClick(View v) {", "onClick: " + text.toString());
                    String res = dbManger.selectPkgName(text.toString());
                    My_AndroidUtil.launchApp(context, res);
                }
            });


        }
    }
}
