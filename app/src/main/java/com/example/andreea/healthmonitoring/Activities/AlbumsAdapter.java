package com.example.andreea.healthmonitoring.Activities;

import android.content.Context;
import android.content.Intent;
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
import com.example.andreea.healthmonitoring.R;

import java.util.List;

import model.Food;
import model.User;

/**
 * Created by Andreea on 13.12.2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Food> foodsList;
    private Resources resources;
    private int idCover;
    private List<Integer> covers;
    private Food selectedFood;
    private User userAfterLogin;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView thumbnail, overflow, star1, star2, star3, star4, star5;
        //Button share;
        ImageView share;

        public MyViewHolder(View view) {
            super(view);
            //view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            //stars = (TextView) view.findViewById(R.id.stars);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            star1 = (ImageView) view.findViewById(R.id.star1);
            star2 = (ImageView) view.findViewById(R.id.star2);
            star3 = (ImageView) view.findViewById(R.id.star3);
            star4 = (ImageView) view.findViewById(R.id.star4);
            star5 = (ImageView) view.findViewById(R.id.star5);
            //share = (Button) view.findViewById(R.id.share);
            share = (ImageView) view.findViewById(R.id.share);
            share.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            for (Food food : foodsList) {
                if (food.getFoodname().equals(title.getText())) {
                    shareTextUrl(food.getUrl());
                }
            }
            //else Toast.makeText(mContext, "Isn't Cheese post", Toast.LENGTH_SHORT).show();
//            //Toast.makeText(mContext, "dddda", Toast.LENGTH_SHORT).show();
//            Toast.makeText(mContext, "da, am dat click pe share", Toast.LENGTH_SHORT).show();
//            Intent myIntent= new Intent(Intent.ACTION_SEND);
//            myIntent.setType("text/plain");
//            String shareBody="Your body here";
//            String shareSub="Your Subject here";
//            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
//            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//            mContext.startActivity(Intent.createChooser(myIntent, "Share using"));

        }

        private void shareTextUrl(String url) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
            share.putExtra(Intent.EXTRA_TEXT, url);

            mContext.startActivity(Intent.createChooser(share, "Share link!"));
        }

    }


    public AlbumsAdapter(Context mContext, List<Food> foodsList, List<Integer> covers, User userAfterLogin) {
        this.mContext = mContext;
        this.foodsList = foodsList;
        this.covers = covers;
        this.userAfterLogin=userAfterLogin;
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
        holder.title.setText(food.getFoodname() + "");
        //holder.stars.setText(food.getStars() + " stars");
        int numberOfStars = food.getStars();
        selectedFood = food;
        switch (numberOfStars) {
            case 1:
                holder.star1.setImageResource(R.drawable.iconstar);
                // loading album cover using Glide library
                Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);


                holder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        showPopupMenu(holder.overflow);
                    }
                });

                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        //Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, AddFoodActivity.class);
                        intent.putExtra("userAfterLogin", userAfterLogin);
                        intent.putExtra("caller", "AlbumsAdapter");
                        intent.putExtra("food", selectedFood);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        mContext.startActivity(intent);
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
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        showPopupMenu(holder.overflow);
                    }
                });

                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        //Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, AddFoodActivity.class);
                        intent.putExtra("userAfterLogin", userAfterLogin);
                        intent.putExtra("caller", "AlbumsAdapter");
                        intent.putExtra("food", selectedFood);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        mContext.startActivity(intent);
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
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        showPopupMenu(holder.overflow);
                    }
                });

                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        //Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, AddFoodActivity.class);
                        intent.putExtra("userAfterLogin", userAfterLogin);
                        intent.putExtra("caller", "AlbumsAdapter");
                        intent.putExtra("food", selectedFood);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        mContext.startActivity(intent);
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
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        showPopupMenu(holder.overflow);
                    }
                });

                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        //Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, AddFoodActivity.class);
                        intent.putExtra("userAfterLogin", userAfterLogin);
                        intent.putExtra("caller", "AlbumsAdapter");
                        intent.putExtra("food", selectedFood);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        mContext.startActivity(intent);
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
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        showPopupMenu(holder.overflow);
                    }
                });

                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
                        Toast.makeText(mContext, "ai apasat " + selectedFood.getFoodname(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, AddFoodActivity.class);
                        intent.putExtra("userAfterLogin", userAfterLogin);
                        intent.putExtra("caller", "AlbumsAdapter");
                        intent.putExtra("food", selectedFood);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //startActivity(intent);
                        mContext.startActivity(intent);
                    }
                });

                return;
            default:
                // loading album cover using Glide library
                Glide.with(mContext).load(covers.get(position)).into(holder.thumbnail);

                holder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (Food food : foodsList) {
                            if (food.getFoodname().equals(holder.title.getText())) {
                                selectedFood = food;
                            }
                        }
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
                    Intent intent = new Intent(mContext, NutritionActivity.class);
                    intent.putExtra("selectedFood", selectedFood);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                    return true;
                case R.id.actionMoreAbout:
                    Toast.makeText(mContext, "More about", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(mContext, MoreAboutActivity.class);
                    intent2.putExtra("selectedFood", selectedFood);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent2);
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