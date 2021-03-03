package Class;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.equipme.R;

import java.util.ArrayList;

import Utils.MyEquipmentRequestItem;

public class Adpter_MyEquipmentRequestItem extends RecyclerView.Adapter<Adpter_MyEquipmentRequestItem.MyStoreViewHolder> {

    private ArrayList<MyEquipmentRequestItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    public Adpter_MyEquipmentRequestItem(Context context, ArrayList<MyEquipmentRequestItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed


    @Override
    public MyStoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_equipment_request_item, parent, false);
        return new MyStoreViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyStoreViewHolder holder, int position) {
        Log.d("pttt", "Binding " + position);
        MyEquipmentRequestItem item = mData.get(position);
        holder.my_equipment_request_IMG_image.setImageBitmap(item.getImageResBitmap());
        holder.my_equipment_request_LBL_part_name.setText(item.getPartName());
        holder.my_equipment_request_LBL_description.setText(item.getDescription());
        holder.my_equipment_request_LBL_date.setText(item.getDate());
        holder.my_equipment_request_LBL_name.setText(item.getRequesterName());
        if(item.getAccpeted().equals("accepted")){
            holder.my_equipment_request_LYT_item.setCardBackgroundColor(Color.parseColor("#183A00"));
            holder.my_equipment_request_BTN_accept.setVisibility(View.GONE);
            holder.my_equipment_request_BTN_accept.setClickable(false);
            holder.my_equipment_request_BTN_decline.setVisibility(View.GONE);
            holder.my_equipment_request_BTN_decline.setClickable(false);
            holder.my_equipment_request_LBL_answer.setText("ACCEPTED");
        }else if(item.getAccpeted().equals("declined")){
            holder.my_equipment_request_LYT_item.setCardBackgroundColor(Color.parseColor("#3A0000"));
            holder.my_equipment_request_BTN_accept.setVisibility(View.GONE);
            holder.my_equipment_request_BTN_accept.setClickable(false);
            holder.my_equipment_request_BTN_decline.setVisibility(View.GONE);
            holder.my_equipment_request_BTN_decline.setClickable(false);
            holder.my_equipment_request_LBL_answer.setText("DECLINED");
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    MyEquipmentRequestItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position,String request);
        void onReportClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyStoreViewHolder extends RecyclerView.ViewHolder {


        TextView my_equipment_request_LBL_answer;
        TextView my_equipment_request_LBL_part_name;
        ImageView my_equipment_request_IMG_image;
        TextView my_equipment_request_LBL_description;
        TextView my_equipment_request_LBL_name;
        TextView my_equipment_request_LBL_date;
        Button my_equipment_request_BTN_accept;
        Button my_equipment_request_BTN_decline;
        androidx.cardview.widget.CardView my_equipment_request_LYT_item;


        MyStoreViewHolder(View itemView) {
            super(itemView);
            my_equipment_request_LBL_part_name = itemView.findViewById(R.id.my_equipment_request_LBL_part_name);
            my_equipment_request_IMG_image = itemView.findViewById(R.id.my_equipment_request_IMG_image);
            my_equipment_request_LBL_description = itemView.findViewById(R.id.my_equipment_request_LBL_description);
            my_equipment_request_LBL_name = itemView.findViewById(R.id.my_equipment_request_LBL_name);
            my_equipment_request_LBL_date = itemView.findViewById(R.id.my_equipment_request_LBL_date);
            my_equipment_request_LBL_answer = itemView.findViewById(R.id.my_equipment_request_LBL_answer);
            my_equipment_request_BTN_accept = itemView.findViewById(R.id.my_equipment_request_BTN_accept);
            my_equipment_request_BTN_decline = itemView.findViewById(R.id.my_equipment_request_BTN_decline);
            my_equipment_request_LYT_item = itemView.findViewById(R.id.my_equipment_request_LYT_item);


            my_equipment_request_BTN_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition(),"accepted");
                    }
                }
            });

            my_equipment_request_BTN_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition(),"declined");
                    }
                }
            });


        }
    }
}
