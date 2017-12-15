package com.example.andreea.healthmonitoring;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import model.Food;

/**
 * Created by Andreea on 13.12.2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder>{

    private Context mContext;
    private List<Food> foodsList;
    private Resources resources;
    private int idCover;
    private List<Integer> covers;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail, overflow, star1, star2, star3, star4, star5;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            //stars = (TextView) view.findViewById(R.id.stars);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            star1 = (ImageView) view.findViewById(R.id.star1);
            star2 = (ImageView) view.findViewById(R.id.star2);
            star3 = (ImageView) view.findViewById(R.id.star3);
            star4 = (ImageView) view.findViewById(R.id.star4);
            star5 = (ImageView) view.findViewById(R.id.star5);
        }
    }


    public AlbumsAdapter(Context mContext, List<Food> foodsList, List<Integer>covers) {
        this.mContext = mContext;
        this.foodsList = foodsList;
        this.covers=covers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Food food = foodsList.get(position);
        holder.title.setText(food.getFoodname()+"");
        //holder.stars.setText(food.getStars() + " stars");
        int numberOfStars= food.getStars();
        switch(numberOfStars)
        {
            case 1:
                holder.star1.setImageResource(R.drawable.iconstar);
                // loading album cover using Glide library
                Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);

                holder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(holder.overflow);
                    }
                });
                return;
            case 2:
                holder.star1.setImageResource(R.drawable.iconstar);
                holder.star2.setImageResource(R.drawable.iconstar);
                // loading album cover using Glide library
                Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);

                holder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(holder.overflow);
                    }
                });
                return;
            case 3:
                holder.star1.setImageResource(R.drawable.iconstar);
                holder.star2.setImageResource(R.drawable.iconstar);
                holder.star3.setImageResource(R.drawable.iconstar);
                // loading album cover using Glide library
                Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);

                holder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(holder.overflow);
                    }
                });
                return;
            case 4:
                holder.star1.setImageResource(R.drawable.iconstar);
                holder.star2.setImageResource(R.drawable.iconstar);
                holder.star3.setImageResource(R.drawable.iconstar);
                holder.star4.setImageResource(R.drawable.iconstar);
                // loading album cover using Glide library
                Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);

                holder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(holder.overflow);
                    }
                });
                return;
            case 5:
                holder.star1.setImageResource(R.drawable.iconstar);
                holder.star2.setImageResource(R.drawable.iconstar);
                holder.star3.setImageResource(R.drawable.iconstar);
                holder.star4.setImageResource(R.drawable.iconstar);
                holder.star5.setImageResource(R.drawable.iconstar);
                // loading album cover using Glide library
                Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);

                holder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(holder.overflow);
                    }
                });
                return;
            default:
                // loading album cover using Glide library
                Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);

                holder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(holder.overflow);
                    }
                });
                return;

        }


//        // loading album cover using Glide library
//        Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);
//
//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
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
                case R.id.action_Nutrition:
                    Toast.makeText(mContext, "Nutrition", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.actionMoreAbout:
                    Toast.makeText(mContext, "More about", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }
}
