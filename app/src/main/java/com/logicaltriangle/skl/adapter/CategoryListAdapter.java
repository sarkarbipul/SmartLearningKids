package com.logicaltriangle.skl.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

import com.logicaltriangle.skl.CategoryDetailsActivity;
import com.logicaltriangle.skl.MyApp;
import com.logicaltriangle.skl.R;
import com.logicaltriangle.skl.model.Category;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    public Context context;

    public List<Category> categoryList;

    public CategoryListAdapter(Context context, List<Category> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoriesItemTV.setText(category.getCatName());
        Log.d(TAG, "" + position + ": " + category.getCatImgUrl());
        //https://s-alamenterprise.com/images/sae_logo.png

        String fileName = MyApp.getInstance().common.getFileNameFromUrl(category.getCatImgUrl());
        if (fileName != ""){
            if (fileName.contains("/"))
                fileName.replace("/", "");
            try {
                String filePath = MyApp.getInstance().filesDir + fileName;
                File file = new File(filePath);
                if (file.isFile()){
                    Glide.with(context)
                            .load(Uri.fromFile(file))
                            .into(holder.categoriesItemIV);
                }
                else {
                    Glide.with(context)
                            .load( Uri.parse(category.getCatImgUrl()) )
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.categoriesItemIV);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        //View view;
        ImageView categoriesItemIV;
        TextView categoriesItemTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPos = getAdapterPosition();
                    //MainActivity.categoriesIV.setImageResource(images[adapterPos]);
                    //MainActivity.categoriesTV.setText(names[adapterPos]);
                    Intent intent = new Intent(context, CategoryDetailsActivity.class);
                    Category cat = categoryList.get(adapterPos);
                    intent.putExtra("categoryID", cat.getCatId());
                    intent.putExtra("categoryName", cat.getCatName());
                    context.startActivity(intent);
                }
            });
            categoriesItemIV = itemView.findViewById(R.id.categoriesItemIV);
            categoriesItemTV = itemView.findViewById(R.id.categoriesItemTV);
        }
    }

}
