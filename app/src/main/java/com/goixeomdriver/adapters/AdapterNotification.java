package com.goixeomdriver.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.aakira.expandablelayout.Utils;
import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.models.NotificationData;

import java.util.List;


/**
 * Created by DuongKK on 6/19/2017.
 */

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {


    Context context;
    List<NotificationData> list;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public AdapterNotification(Context context, List<NotificationData> list) {
        this.context = context;
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NotificationData discount = list.get(position);
        if (holder != null) {
//            DrawableCompat.setTint(holder.imgArrow.getDrawable(), ContextCompat.getColor(context, R.color.colorGray));
            holder.tvContent.setText(discount.getContent());
            holder.tvDate.setText(discount.getDate());
            holder.tvTitle.setText(discount.getTitle());
            holder.tvShort.setText(discount.getContent());
            holder.tvTime.setText("Lúc " + discount.getTime());
            holder.expandableLinearLayout.setExpanded(false);
            if(expandState.get(position,false)){
                createRotateAnimator(holder.imgArrow, 0f, 90f).start();
                holder.imgArrow.setImageResource(R.drawable.ic_down_arrow);
                holder.expandableLinearLayout.setExpanded(true);

            }else{
                createRotateAnimator(holder.imgArrow, 90f, 0f).start();
                holder.imgArrow.setImageResource(R.drawable.ic_next);
                holder.expandableLinearLayout.setExpanded(false);
            }
            holder.layoutRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.expandableLinearLayout.toggle();
                    if (holder.expandableLinearLayout.isExpanded()) {
                        createRotateAnimator(holder.imgArrow, 0f, 90f).start();
                        holder.imgArrow.setImageResource(R.drawable.ic_down_arrow);
                        expandState.put(position, true);
                    } else {
                        createRotateAnimator(holder.imgArrow, 90f, 0f).start();
                        holder.imgArrow.setImageResource(R.drawable.ic_next);
                        expandState.put(position, false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvTitle;
        CustomTextView tvShort;
        CustomTextView tvContent;
        CustomTextView tvDate;
        CustomTextView tvTime;
        LinearLayout layoutRoot;
        net.cachapa.expandablelayout.ExpandableLayout expandableLinearLayout;
        ImageView imgArrow;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (CustomTextView) itemView.findViewById(R.id.tv_title);
            tvShort = (CustomTextView) itemView.findViewById(R.id.tv_short);
            tvContent = (CustomTextView) itemView.findViewById(R.id.tv_content);
            tvDate = (CustomTextView) itemView.findViewById(R.id.tv_date);
            tvTime = (CustomTextView) itemView.findViewById(R.id.tv_time);
            expandableLinearLayout = (net.cachapa.expandablelayout.ExpandableLayout) itemView.findViewById(R.id.expandableLayout);
            layoutRoot = (LinearLayout) itemView.findViewById(R.id.ll_root);
            imgArrow = (ImageView) itemView.findViewById(R.id.img_arrow);
        }

    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(100);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
