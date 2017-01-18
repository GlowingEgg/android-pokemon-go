package com.glowingegg.qonzor.flykur;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import static com.glowingegg.qonzor.flykur.R.id.searchTextView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    ArrayList<Picture> pictures = new ArrayList<>();
    int gridIndex = 0;
    String searchString;

    GridView pictureGrid;
    EditText searchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchTextView = (EditText) findViewById(R.id.searchTextView);
        pictureGrid = (GridView) findViewById(R.id.pictureGrid);
        PicturesAdapter picturesAdapter = new PicturesAdapter(this, R.id.picture_view, pictures);
        pictureGrid.setAdapter(picturesAdapter);

        pictureGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Picture picture = pictures.get(position);
                searchString = searchTextView.getText().toString();
                gridIndex = pictureGrid.getFirstVisiblePosition();
                Intent imageIntent = new Intent(getBaseContext(), ImageActivity.class);
                imageIntent.putExtra("picture", picture).putExtra("searchString", searchString).putExtra("gridIndex", gridIndex);
                startActivity(imageIntent);
            }
        });

        searchTextView.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String searchKey = searchTextView.getText().toString();
                            AsyncPictureRetrieval asyncPictureRetrieval = new AsyncPictureRetrieval();
                            asyncPictureRetrieval.execute(searchKey);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(configuration);

        /**
         * Do Stuff
         */

        gridIndex = getIntent().getIntExtra("gridIndex", 0);
        searchString = getIntent().getStringExtra("searchString");
        if(searchString !=null){
            AsyncPictureRetrieval asyncPictureRetrieval = new AsyncPictureRetrieval();
            asyncPictureRetrieval.execute(searchString);
        }
    }

    private class AsyncPictureRetrieval extends AsyncTask<String, Void, ArrayList<Picture>>{
        protected ArrayList<Picture> doInBackground(String... searchKey){
            ArrayList<Picture> pics = NetworkingSingleton.getInstance().getPictures(searchKey[0]);
            return pics;
        }
        protected void onPostExecute(ArrayList<Picture> pics){
            if(pics != null) {
                pictures.clear();
                pictures.addAll(pics);
                pictureGrid.smoothScrollToPosition(gridIndex);
                Log.d(TAG, "finished");
            }
        }
    }
    public class PicturesAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Picture> pictures;
        private int resource;

        public PicturesAdapter(Context context, int resource, ArrayList<Picture> pictures){
            this.mContext = context;
            this.pictures = pictures;
            this.resource = resource;
        }

        @Override
        public int getCount(){
            return pictures.size();
        }
        @Override
        public long getItemId(int position){
            return Long.parseLong(pictures.get(position).getId());
        }
        @Override
        public Object getItem(int position){
            return pictures.get(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Picture picture = (Picture) getItem(position);
            String imageUrl = picture.getUrl();
            if(convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.image_container, parent, false);
            }

            ImageView pictureView = (ImageView) convertView.findViewById(R.id.picture_view);

            //Set the picture
            //Will need a class for loading an image from the url in picture object
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageUrl, pictureView);


            return convertView;
        }
    }

}
