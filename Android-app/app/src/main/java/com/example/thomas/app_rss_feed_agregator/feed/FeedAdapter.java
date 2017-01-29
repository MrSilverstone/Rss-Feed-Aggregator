package com.example.thomas.app_rss_feed_agregator.feed;
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
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.thomas.app_rss_feed_agregator.R;
import com.example.thomas.app_rss_feed_agregator.category.CategoryActivity;
import com.example.thomas.app_rss_feed_agregator.feed.FeedActivity;
import com.example.thomas.app_rss_feed_agregator.models.Category;
import com.example.thomas.app_rss_feed_agregator.models.DataSet;
import com.example.thomas.app_rss_feed_agregator.models.Feed;

import java.lang.ref.WeakReference;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private final Context context;
    WeakReference<Context> mContextWeakReference;

    public FeedAdapter(Context context) {
        this.context = context;
        this.mContextWeakReference = new WeakReference<Context>(context);
    }



    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        CardView feed;
        TextView FeedName;
        WebView FeedPreview;
        public   TextView buttonViewOption;

        FeedViewHolder(final View itemView) {
            super(itemView);
            final Context  context = itemView.getContext();
            feed = (CardView) itemView.findViewById(R.id.feed);
            FeedName = (TextView) itemView.findViewById(R.id.feed_name);
            FeedPreview = (WebView) itemView.findViewById(R.id.feedPreview);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);

            FeedName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    if (getAdapterPosition() < DataSet.categories().size()) {
                        if(context instanceof FeedActivity) {
                            ((FeedActivity) context).launchFeedDetails((Feed) getItem(getAdapterPosition()));
                        }
                    }
                    else {
                        ((FeedActivity)context).displayAdd();
                    }
                }
            });

            itemView.setClickable(true);
        }

        public Object getItem(int position) {
            if (position < DataSet.feeds().size()) {
                Feed f = DataSet.feeds().valueAt(position);
                return f;
            }
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return DataSet.feeds().size();
    }


    @Override
    public FeedViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item, viewGroup, false);
        FeedViewHolder pvh = new FeedViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder FeedViewHolder, int i) {
        final Feed itemF = (Feed) getItem(i);
        if (i < DataSet.feeds().size()) {
            String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            FeedViewHolder.FeedName.setText(itemF.name());
            FeedViewHolder.FeedPreview.loadData(itemF.content(),"text/html", "UTF-8");

            FeedViewHolder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, FeedViewHolder.buttonViewOption);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.category_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.remove:
                                    if(context instanceof FeedActivity){
                                        ((FeedActivity)context).remove(itemF);
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

        }
    }

    public Object getItem(int position) {
        if (position < DataSet.feeds().size()) {
            Feed f = DataSet.feeds().valueAt(position);
            System.out.println("added feed000" + f.name());
            return f;
        }
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}