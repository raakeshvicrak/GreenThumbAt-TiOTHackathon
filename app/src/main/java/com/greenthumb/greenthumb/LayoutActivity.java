package com.greenthumb.greenthumb;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.greenthumb.greenthumb.LayoutPoints.LayoutPointsDBUpdater;
import com.greenthumb.greenthumb.Maps.MapsActivity;
import com.greenthumb.greenthumb.Utils.FontAssetHelper;
import com.greenthumb.greenthumb.Vision.VisionActivity;

public class LayoutActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView addFarm, addFarmHeading;
    private Button btnImage;
    private Typeface icon_font, firamonobold, firasansbold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        btnImage = (Button) findViewById(R.id.btnImg);
        addFarm = (TextView) findViewById(R.id.addFarm);
        addFarmHeading = (TextView) findViewById(R.id.addFarmHeading);

        icon_font = FontAssetHelper.getTypeFaceCustomIcon_fonts(LayoutActivity.this);
        firamonobold = FontAssetHelper.getTypeface_FiraMonoBold(LayoutActivity.this);
        firasansbold = FontAssetHelper.getTypeface_FiraSansBold(LayoutActivity.this);

        if(addFarm!=null && icon_font!=null) {
            addFarm.setTypeface(icon_font);
            addFarm.setText(R.string.AddFarm);
        }

        /*if(addFarm!=null){
            addFarm.setOnClickListener(new OnClickListener());
        }*/

        if (addFarmHeading!=null){
            addFarmHeading.setTypeface(firasansbold);
        }
        addFarm.setOnClickListener(this);
        btnImage.setOnClickListener(this);

        new LayoutPointsDBUpdater(LayoutActivity.this);
    }

    @Override
    public void onClick(View v) {
        if(v == addFarm){
            Intent intent = new Intent(LayoutActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        if(v == btnImage){
            Log.e("Hello Madrina", "Hlee");
            Intent intent = new Intent(LayoutActivity.this, VisionActivity.class);
            startActivity(intent);
        }
    }

    /*class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view.getId() == addFarm.getId()){
                Intent intent = new Intent(LayoutActivity.this, MapsActivity.class);
                startActivity(intent);
            }
            if(view.getId() == btnImage.getId()){
                Intent intent = new Intent(LayoutActivity.this, VisionActivity.class);
                startActivity(intent);
            }
        }
    }*/
}
