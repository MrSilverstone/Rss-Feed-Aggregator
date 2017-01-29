package com.example.thomas.app_rss_feed_agregator.menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thomas.app_rss_feed_agregator.R;
import com.example.thomas.app_rss_feed_agregator.feed.FeedActivity;
import com.example.thomas.app_rss_feed_agregator.models.Category;
import com.example.thomas.app_rss_feed_agregator.models.DataSet;
import com.example.thomas.app_rss_feed_agregator.models.Feed;

import java.lang.ref.WeakReference;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.CategoryViewHolder> {
    private final Context context;
    WeakReference<Context> mContextWeakReference;

    public MenuAdapter(Context context) {
        this.context = context;
        this.mContextWeakReference = new WeakReference<Context>(context);
    }



    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout category;
        TextView CategoryName;
        TextView CategoryFeeds;
        Category   Cat;

        CategoryViewHolder(View itemView) {
            super(itemView);
            final Context context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    Intent intent = new Intent(context, FeedActivity.class);
                    Cat = ((Category) getItem(getAdapterPosition()));
                    intent.putExtra("category", Cat.name());
                    context.startActivity(intent);
                }
            });

            CategoryName = (TextView) itemView.findViewById(R.id.category_name);
            itemView.setClickable(true);
        }

        public Object getItem(int position) {
            if (position < DataSet.categories().size()) {
                Category c = DataSet.categories().valueAt(position);
                return c;
            }
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return DataSet.categories().size();
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_menu, viewGroup, false);
        CategoryViewHolder pvh = new CategoryViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder CategoryViewHolder, int i) {

        Category item = (Category) getItem(i);
        if (item != null) {
            CategoryViewHolder.CategoryName.setText(item.name());
        }
    }

    public Object getItem(int position) {
        if (position < DataSet.categories().size()) {
            Category c = DataSet.categories().valueAt(position);
            return c;
        }
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
