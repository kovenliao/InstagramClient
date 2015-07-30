package kl.instagramclient.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kl.instagramclient.R;
import kl.instagramclient.data.InstagramPhoto;

/**
 * Created by kovenliao on 7/30/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private static class ViewHolder {
        TextView username;
        TextView caption;
        ImageView photo;
        TextView like;
        ImageView userPhoto;
        TextView createdTime;
    }

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.caption =  (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.like = (TextView) convertView.findViewById(R.id.tvLike);
            viewHolder.userPhoto = (ImageView) convertView.findViewById(R.id.ivUserPhoto);
            viewHolder.createdTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(photo.getUsername());
        viewHolder.caption.setText(photo.getCaption());
        viewHolder.photo.setImageResource(0);
        viewHolder.like.setText(photo.getLikesCount() + " likes");
        viewHolder.userPhoto.setImageResource(0);

        String relativeTime = DateUtils.getRelativeTimeSpanString(photo.getCreatedTime() * 1000,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        viewHolder.createdTime.setText(relativeTime);

        Picasso.with(getContext())
                .load(photo.getImageUrl())
//                .placeholder(R.drawable.placeholder)
                .into(viewHolder.photo);

        Picasso.with(getContext())
                .load(photo.getUserPhotoUrl())
                .into(viewHolder.userPhoto);

        return convertView;
    }
}
