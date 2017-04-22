package com.greenthumb.greenthumb;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.greenthumb.greenthumb.LayoutDisplay.LayoutDisplay;
import com.greenthumb.greenthumb.LayoutPoints.LayoutPointsDBUpdater;
import com.greenthumb.greenthumb.Utils.FontAssetHelper;

public class LayoutActivity extends AppCompatActivity {

    private TextView addFarm, addFarmHeading;
    private Typeface icon_font, firamonobold, firasansbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        addFarm = (TextView) findViewById(R.id.addFarm);
        addFarmHeading = (TextView) findViewById(R.id.addFarmHeading);

        icon_font = FontAssetHelper.getTypeFaceCustomIcon_fonts(LayoutActivity.this);
        firamonobold = FontAssetHelper.getTypeface_FiraMonoBold(LayoutActivity.this);
        firasansbold = FontAssetHelper.getTypeface_FiraSansBold(LayoutActivity.this);

        if(addFarm!=null && icon_font!=null) {
            addFarm.setTypeface(icon_font);
            addFarm.setText(R.string.AddFarm);
        }

        if(addFarm!=null){
            addFarm.setOnClickListener(new OnClickListener());
        }

        if (addFarmHeading!=null){
            addFarmHeading.setTypeface(firasansbold);
        }

        new LayoutPointsDBUpdater(LayoutActivity.this);
    }

    class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view.getId() == addFarm.getId()){
                Intent intent = new Intent(LayoutActivity.this, LayoutDisplay.class);
                startActivity(intent);
            }
        }
    }
}
