package com.glowingegg.qonzor.flykur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class ImageActivity extends AppCompatActivity {

    ImageView imageView;
    Picture picture;
    String searchString;
    int gridIndex;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = (ImageView) findViewById(R.id.imageView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        picture = getIntent().getParcelableExtra("picture");
        gridIndex = getIntent().getIntExtra("gridIndex", 0);
        searchString = getIntent().getStringExtra("searchString");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                mainIntent.putExtra("searchString", searchString).putExtra("gridIndex", gridIndex);
                startActivity(mainIntent);
            }
        });

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().destroy();
        ImageLoader.getInstance().init(config);
        String imageUrl = picture.getUrl();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUrl, imageView);
    }
}
