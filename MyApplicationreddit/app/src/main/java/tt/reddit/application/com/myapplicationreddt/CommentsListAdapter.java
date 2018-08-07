package tt.reddit.application.com.myapplicationreddt;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by lazaro on 8/6/18.
 */

public class CommentsListAdapter extends ArrayAdapter<Comment>{

    private static final String TAG = "CommentsListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;


    private static class ViewHolder {
        TextView comment;
        TextView author;
        TextView date_updated;
        ProgressBar mProgressBar;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public CommentsListAdapter(Context context, int resource, ArrayList<Comment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String comment = getItem(position).getComment();
        String author = getItem(position).getAuthor();
        String date_updated = getItem(position).getUpdated();

        try{

            final View result;

            //ViewHolder object
            final CommentsListAdapter.ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder= new CommentsListAdapter.ViewHolder();
                holder.comment = (TextView) convertView.findViewById(R.id.comment_commentlayout);
                holder.author = (TextView) convertView.findViewById(R.id.author_commentlayout);
                holder.date_updated = (TextView) convertView.findViewById(R.id.updated_commentlayout);
                holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.commentlayout_ProgressBar);

                result = convertView;

                convertView.setTag(holder);
            }
            else{
                holder = (CommentsListAdapter.ViewHolder) convertView.getTag();
                result = convertView;
                holder.mProgressBar.setVisibility(View.VISIBLE);
            }

            lastPosition = position;

            holder.comment.setText(comment);
            holder.author.setText(author);
            holder.date_updated.setText(date_updated);
            holder.mProgressBar.setVisibility(View.GONE);

            return convertView;
        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }
    }
}
