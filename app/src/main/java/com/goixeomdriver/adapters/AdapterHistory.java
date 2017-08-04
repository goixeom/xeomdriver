package com.goixeomdriver.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goixeom.R;
import com.goixeomdriver.Activity.DetailBooking;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.models.History;
import com.goixeomdriver.utils.Constants;

import java.util.List;



/**
 * Created by DuongKK on 6/19/2017.
 */

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder>{


    Context context;
    List<History> list;

    public AdapterHistory(Context context, List<History> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final History discount = list.get(position);
        if(holder!=null){
            holder.tvTime.setText(discount.getDate());
            holder.tvDes.setText(discount.getEnd());
            holder.tvFrom.setText(discount.getStart());
            holder.tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i =new Intent(context, DetailBooking.class);
                    i.putExtra(Constants.BOOKING,discount.getId());
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvFrom;
        TextView tvDes;
        TextView tvTime;
        CustomTextView tvDetail;


        public ViewHolder(View itemView) {
            super(itemView);
            tvFrom = (TextView)itemView.findViewById(R.id.tv_from);
            tvDes = (TextView)itemView.findViewById(R.id.tv_des);
            tvTime = (TextView)itemView.findViewById(R.id.tv_time);
            tvDetail = (CustomTextView) itemView.findViewById(R.id.tv_go_detail);

        }
    }
}
