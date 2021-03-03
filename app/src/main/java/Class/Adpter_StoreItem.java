package Class;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.equipme.R;

import java.util.ArrayList;

import Utils.SearchedListItem;
import Utils.StoreItem;

public class Adpter_StoreItem extends RecyclerView.Adapter<Adpter_StoreItem.MyStoreViewHolder>{

    private ArrayList<StoreItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    public Adpter_StoreItem(Context context, ArrayList<StoreItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyStoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.store_item, parent, false);
        return new MyStoreViewHolder(view);
    }



    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyStoreViewHolder holder, int position) {
        Log.d("pttt", "Binding " + position);
        StoreItem item = mData.get(position);
        Log.d("Pttt","serched list set text: " + item.getPartName());
        Log.d("Pttt","mData = "+mData.size());
        holder.store_IMG_image.setImageBitmap(item.getImageResBitmap());
        holder.store_LBL_part_name.setText(item.getPartName());
        holder.store_LBL_description.setText(item.getDescription());
        Log.d("Adpter", "BitMap Width: "+item.getImageResBitmap().getWidth());
    }

    public void updateDataset(ArrayList<StoreItem> updatedData){
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
    StoreItem getItem(int id) {
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
    public class MyStoreViewHolder extends RecyclerView.ViewHolder {

        TextView store_LBL_part_name;
        ImageView store_IMG_image;
        TextView store_LBL_description;


        MyStoreViewHolder(View itemView) {
            super(itemView);
            store_LBL_part_name = itemView.findViewById(R.id.store_LBL_part_name);
            store_IMG_image = itemView.findViewById(R.id.store_IMG_image);
            store_LBL_description = itemView.findViewById(R.id.store_LBL_description);

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
