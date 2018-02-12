package com.example.andreea.healthmonitoring;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BubbleActivity extends AppCompatActivity {

    final String[] titles = getResources().getStringArray(R.array.countries);
    final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
    final TypedArray images = getResources().obtainTypedArray(R.array.images);

    //private val mediumTypeface by lazy { Typeface.createFromAsset(assets, ROBOTO_MEDIUM) };

    private BubblePicker bubblePicker;
//    String[] name = {
//            "Consulting",
//            "Nearest Hospital",
//            "Healthy tips",
//            "Your Healthy level"
//    };
//    int images[] = {
//            R.drawable.a,
//            R.drawable.b,
//            R.drawable.c,
//            R.drawable.d
//    };
//
//    int colors[] = {
//            Color.parseColor("#303F9F"),
//            Color.parseColor("#EEFF41"),
//            Color.parseColor("#C2185B"),
//            Color.parseColor("#00C853")
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bubble);
//        bubblePicker = (BubblePicker) findViewById(R.id.picker);
//        ArrayList<PickerItem> listItems = new ArrayList<PickerItem>();
//        for (int i = 0; i < name.length; i++) {
//            PickerItem item = new PickerItem(name[i], colors[i], Color.WHITE, getDrawable(images[i]));
//            listItems.add(item);
//        }
//        bubblePicker.setItems(listItems);
//        bubblePicker.setListener(new BubblePickerListener() {
//            @Override
//            public void onBubbleSelected(PickerItem pickerItem) {
//                Toast.makeText(BubbleActivity.this, " " + pickerItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onBubbleDeselected(PickerItem pickerItem) {
//                Toast.makeText(BubbleActivity.this, "Deselected", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                Typeface mediumTypeface;
                //item.setTypeface(mediumTypeface);
                item.setTextColor(ContextCompat.getColor(BubbleActivity.this, android.R.color.white));
                item.setBackgroundImage(ContextCompat.getDrawable(BubbleActivity.this, images.getResourceId(position, 0)));
                return item;
            }
        });

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {

            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bubblePicker.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bubblePicker.onPause();
    }
}
