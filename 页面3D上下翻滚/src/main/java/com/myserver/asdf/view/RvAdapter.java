package com.myserver.asdf.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.myserver.asdf.R;

import java.util.List;


/**
 * 创建者：wanglei
 * <p>时间：17/10/1  10:56
 * <p>类描述：
 * <p>修改人：
 * <p>修改时间：
 * <p>修改备注：
 */
public class RvAdapter extends RecyclerView.Adapter {

    List<Integer> list;
    int ii;

    public RvAdapter(List<Integer> list,int ii) {
        this.list = list;
        this.ii = ii;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GridViewHolder) {
            if (ii != 0)
                ((GridViewHolder) holder).item_iv.setBackgroundResource(R.mipmap.delete);
            ((GridViewHolder) holder).item_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class GridViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_iv;

        public GridViewHolder(View itemView) {
            super(itemView);
            item_iv = (ImageView) itemView.findViewById(R.id.item_iv);

        }
    }


    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }
}



