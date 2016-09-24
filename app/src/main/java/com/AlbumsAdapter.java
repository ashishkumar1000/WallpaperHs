package com;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.model.WallpaperCategory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wallpaperhs.R;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<WallpaperCategory> categoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public AlbumsAdapter(Context mContext, List<WallpaperCategory> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final WallpaperCategory category = categoryList.get(position);
        holder.title.setText(category.getName());
        holder.count.setText(category.getSubtitle());

        Picasso picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(false);

        picasso.with(mContext)
                .load(category.getImgUrl())
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(mContext)
                                .load(category.getImgUrl())
                                .fit()
                                .centerCrop()
                                .into(holder.thumbnail, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopupMenu(holder.overflow);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof MainActivity) {
                    ((MainActivity) mContext).openViewPager(category.getImgListUrl());
                }
            }
        });
    }

    public void reloadList(List<WallpaperCategory> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
