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

import Utils.CartItem;
import Utils.MyEquipmentRequestItem;

public class Adpter_CartItem extends RecyclerView.Adapter<Adpter_CartItem.MyStoreViewHolder> {

    private ArrayList<CartItem> mData;
    private LayoutInflater mInflater;

    public Adpter_CartItem(Context context, ArrayList<CartItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed


    @Override
    public MyStoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cart_item, parent, false);
        return new MyStoreViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyStoreViewHolder holder, int position) {
        Log.d("pttt", "Binding " + position);
        CartItem item = mData.get(position);
        holder.cart_IMG_image.setImageBitmap(item.getImageResBitmap());
        holder.cart_LBL_part_name.setText(item.getPartName());
        holder.cart_LBL_description.setText(item.getDescription());
        holder.cart_LBL_date.setText(item.getDate());
        holder.cart_LBL_name.setText(item.getOwnerName());
        holder.cart_LBL_address.setText(item.getOwnerAddress());
        if(item.getAccpeted().equals("accepted")){
            holder.cart_LYT_item.setCardBackgroundColor(Color.parseColor("#183A00"));
            holder.cart_LBL_answer.setText("ACCEPTED");
        }else if(item.getAccpeted().equals("declined")){
            holder.cart_LYT_item.setCardBackgroundColor(Color.parseColor("#3A0000"));
            holder.cart_LBL_answer.setText("DECLINED");
        }else{
            holder.cart_LBL_answer.setText("AWAITING");
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class MyStoreViewHolder extends RecyclerView.ViewHolder {


        TextView cart_LBL_answer;
        TextView cart_LBL_part_name;
        ImageView cart_IMG_image;
        TextView cart_LBL_description;
        TextView cart_LBL_name;
        TextView cart_LBL_date;
        TextView cart_LBL_address;
        androidx.cardview.widget.CardView cart_LYT_item;


        MyStoreViewHolder(View itemView) {
            super(itemView);
            cart_LBL_part_name = itemView.findViewById(R.id.cart_LBL_part_name);
            cart_IMG_image = itemView.findViewById(R.id.cart_IMG_image);
            cart_LBL_description = itemView.findViewById(R.id.cart_LBL_description);
            cart_LBL_name = itemView.findViewById(R.id.cart_LBL_name);
            cart_LBL_date = itemView.findViewById(R.id.cart_LBL_date);
            cart_LBL_answer = itemView.findViewById(R.id.cart_LBL_answer);
            cart_LYT_item = itemView.findViewById(R.id.cart_LYT_item);
            cart_LBL_address = itemView.findViewById(R.id.cart_LBL_address);

        }
    }
}
