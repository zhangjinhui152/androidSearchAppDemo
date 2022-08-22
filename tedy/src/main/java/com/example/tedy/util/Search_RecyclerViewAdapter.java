package com.example.tedy.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tedy.MyAccessbilityService;
import com.example.tedy.R;
import com.example.tedy.bean.ApKData;
import com.example.tedy.bean.SearchBlock;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class Search_RecyclerViewAdapter extends RecyclerView.Adapter<Search_RecyclerViewAdapter.My_ViewHolder> {

    public List<SearchBlock> getData() {
        return data;
    }

    private List<SearchBlock> data;
    private Context context;
    DBManger dbManger;

    public ImageView getSearchButtom() {
        return SearchButtom;
    }

    public void setSearchButtom(ImageView searchButtom) {
        SearchButtom = searchButtom;
    }

    private ImageView SearchButtom;

    public TextInputEditText getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(TextInputEditText searchInput) {
        this.searchInput = searchInput;
    }

    private TextInputEditText searchInput;

    public TextInputLayout getSearchInput_L() {
        return searchInput_L;
    }

    public void setSearchInput_L(TextInputLayout searchInput_L) {
        this.searchInput_L = searchInput_L;
    }

    private TextInputLayout searchInput_L;


    public Search_RecyclerViewAdapter(List<SearchBlock> data, Context context, DBManger dbManger) {
        this.data = data;
        this.context = context;
        this.dbManger = dbManger;
    }

    public void add(SearchBlock block){
        this.data.add(block);
        notifyItemInserted(this.data.size());
    }
    public void remove(SearchBlock oldBlock){
        this.data.remove(oldBlock);
        notifyItemRemoved(this.data.size());
    }


    @NonNull
    @Override
    public My_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.searchblock, null);
        return new My_ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull My_ViewHolder holder, int position) {
        byte[] apkIcon = data.get(position).getApkIcon();
        Bitmap bitmapFromByte = My_AndroidUtil.byteToBitmap(apkIcon);
        if (apkIcon != null){
            holder.apkIcon.setImageBitmap(bitmapFromByte);
        }

        holder.sb = data.get(position);

    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class My_ViewHolder extends RecyclerView.ViewHolder {
        ImageView apkIcon;
        SearchBlock sb;

        public My_ViewHolder(@NonNull View itemView) {
            super(itemView);
            apkIcon = itemView.findViewById(R.id.apkicon);

            apkIcon.setOnClickListener(v -> {

                searchInput_L.setHint(sb.getSearchName());

                switch (sb.getType()){
                    case BASE:
                        Log.d("BASE", "BASE: ");
                        setSearchButtomImage();
                        break;
                    case URL_SCHEME:
                        Log.d("URL_SCHEME", "URL_SCHEME: ");
                        setSearchButtomImage();
                        FileGet.getSettingBlock().setCurrSearchBlock(sb);
//                        searchInput.setText(sb.getSearchName());
                        break;
                    case ACCESSIBILITY:
                        Log.d("ACCESSIBILITY", "ACCESSIBILITY: ");
                        FileGet.getSettingBlock().setCurrSearchBlock(sb);
                        if (!MyAccessbilityService.isServiceCreated()){
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                          FileGet.getMac().startActivity(intent);
                        }

                        setSearchButtomImage();
                        break;
                    case ADD:
                        Log.d("ADD", "add: ");
//                        add(new SearchBlock("Base",null,null,null, SearchType.ADD));
                        FileGet.getSettingBlock().scaleCard();
                        FileGet.getMac().foldSearchBlockCard2();
                        break;
                }



            });
            apkIcon.setOnLongClickListener(v -> {
                Log.d("TAG", "My_ViewHolder: ");
                FileGet.getSettingBlock().scaleCard();
                FileGet.getMac().foldSearchBlockCard2();
                FileGet.getSettingBlock().setCurrSearchBlock(sb);
                FileGet.getSettingBlock().WriteSettingInput(FileGet.getMacView());

                return true;
            });

        }

        private void setSearchButtomImage() {
            //设置 主搜索按钮的图片
            Log.d("TAG", "My_ViewHolder: ");
            ImageView searchButtom = getSearchButtom();
            List<ApKData> tedy = dbManger.selectRes2("tedy");
            ApKData apKData = tedy.get(0);
            byte[] apkIcon = apKData.getApkIcon();
            Bitmap bitmap = My_AndroidUtil.byteToBitmap(apkIcon);
            searchButtom.setImageBitmap(bitmap);


            if (sb.getApkIcon() != null){
                searchButtom.setImageBitmap(My_AndroidUtil.byteToBitmap(sb.getApkIcon()));
            }
        }
    }
}
