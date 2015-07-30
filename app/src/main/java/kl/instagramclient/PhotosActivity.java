package kl.instagramclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kl.instagramclient.adapter.InstagramPhotosAdapter;
import kl.instagramclient.data.InstagramPhoto;

public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "5ad7aed5621d42d8b007b5b4c25e5f30";

    public static final String TAG = "PhotosActivity";

    private InstagramPhotosAdapter adapter;
    private ArrayList<InstagramPhoto> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        photos = new ArrayList<>();
        adapter = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        lvPhotos.setAdapter(adapter);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, response.toString());
                JSONArray photosJSON = null;

                InstagramPhoto photo;
                JSONObject photoJSON;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        photoJSON = photosJSON.getJSONObject(i);
                        photo = new InstagramPhoto();
                        photo.setUsername(photoJSON.getJSONObject("user").getString("username"));

                        if (!photoJSON.isNull("caption")) {
                            photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
                        }

                        photo.setImageUrl(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));
                        photo.setUserPhotoUrl(photoJSON.getJSONObject("user").getString("profile_picture"));
                        photo.setCreatedTime(photoJSON.getLong("created_time"));
                        photos.add(photo);
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
