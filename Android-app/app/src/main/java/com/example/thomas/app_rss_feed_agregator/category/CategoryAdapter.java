package com.example.thomas.app_rss_feed_agregator.category;

import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.thomas.app_rss_feed_agregator.R;
import com.example.thomas.app_rss_feed_agregator.feed.FeedActivity;
import com.example.thomas.app_rss_feed_agregator.models.Category;
import com.example.thomas.app_rss_feed_agregator.models.DataSet;
import com.example.thomas.app_rss_feed_agregator.models.Feed;

import java.lang.ref.WeakReference;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final Context context;
    WeakReference<Context> mContextWeakReference;

    public CategoryAdapter(Context context) {
        this.context = context;
        this.mContextWeakReference = new WeakReference<Context>(context);
    }



    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView category;
        TextView CategoryName;
        TextView CategoryCount;
        public   TextView buttonViewOption;
        Category Cat;

        CategoryViewHolder(final View itemView) {
            super(itemView);
            final Context  context = itemView.getContext();
            category = (CardView) itemView.findViewById(R.id.category);
            CategoryName = (TextView) itemView.findViewById(R.id.category_name);
            CategoryCount = (TextView) itemView.findViewById(R.id.category_afeeds);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);

            CategoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    if (getAdapterPosition() < DataSet.categories().size()) {
                        Category Cat;
                        Cat = ((Category) getItem(getAdapterPosition()));
                        if(context instanceof CategoryActivity){
                            ((CategoryActivity)context).retrieveFeeds(Cat.name());
                        }
                    }
                    else {
                        ((CategoryActivity)context).displayAdd();
                    }
                }
            });

            itemView.setClickable(true);
        }

        public Object getItem(int position) {
            if (position < DataSet.categories().size()) {
                Category c = DataSet.categories().valueAt(position);
                System.out.println("added category000" + c.name());
                 return c;
            }
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return DataSet.categories().size() + 1;
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        CategoryViewHolder pvh = new CategoryViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder CategoryViewHolder, int i) {
        final Category itemCat = (Category) getItem(i);
        if (i < DataSet.categories().size()) {
            System.out.println("ADDED         : " + itemCat.name());
            CategoryViewHolder.CategoryName.setText(itemCat.name());
            String feeds = DataSet.data().get(itemCat).size() + " feeds";
            CategoryViewHolder.CategoryCount.setText(feeds);
            final int id = i;
            CategoryViewHolder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, CategoryViewHolder.buttonViewOption);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.category_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.remove:
                                    if(context instanceof CategoryActivity){
                                        ((CategoryActivity)context).remove(id, itemCat);
                                    }
                                    //handle menu3 click
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });

        } else {
            System.out.println("HELLO");

            CategoryViewHolder.CategoryName.setText("Add new category");
            CategoryViewHolder.CategoryCount.setText("");
        }

    }

    public Object getItem(int position) {
        if (position < DataSet.categories().size()) {
            Category c = DataSet.categories().valueAt(position);
            System.out.println("added category" + c.name());
            return c;
        }
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
