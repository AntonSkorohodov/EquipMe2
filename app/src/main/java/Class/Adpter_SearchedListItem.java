package Class;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipme.R;
import java.util.ArrayList;
import Utils.SearchedListItem;

public class Adpter_SearchedListItem extends RecyclerView.Adapter<Adpter_SearchedListItem.MyViewHolder> {

    private ArrayList<SearchedListItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adpter_SearchedListItem(Context context, ArrayList<SearchedListItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.searched_list_item, parent, false);
        return new MyViewHolder(view);
    }



    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("pttt", "Binding " + position);
        SearchedListItem item = mData.get(position);
        Log.d("Pttt","serched list set text: " + item.getPartName());
        Log.d("Pttt","mData = "+mData.size());
        holder.searched_list_IMG_image.setImageBitmap(item.getImageResBitmap());
        holder.searched_list_LBL_part_name.setText(item.getPartName());
        holder.searched_list_LBL_description.setText(item.getDescription());
        holder.searched_list_LBL_measered_distance.setText(item.getMeaseredDistance() + " Km");
        Log.d("Adpter", "BitMap Width: "+item.getImageResBitmap().getWidth());
    }

    public void updateDataset(ArrayList<SearchedListItem> updatedData){
        mData=updatedData;
        notifyDatasetChanged();
    }

    private void notifyDatasetChanged() {

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    SearchedListItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onReportClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView searched_list_LBL_part_name;
        ImageView searched_list_IMG_image;
        TextView searched_list_LBL_description;
        TextView searched_list_LBL_measered_distance;

        MyViewHolder(View itemView) {
            super(itemView);
            searched_list_LBL_part_name = itemView.findViewById(R.id.searched_list_LBL_part_name);
            searched_list_IMG_image = itemView.findViewById(R.id.searched_list_IMG_image);
            searched_list_LBL_description = itemView.findViewById(R.id.searched_list_LBL_description);
            searched_list_LBL_measered_distance = itemView.findViewById(R.id.searched_list_LBL_measered_distance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

        }
    }
}

