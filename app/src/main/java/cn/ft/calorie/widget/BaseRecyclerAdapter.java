package cn.ft.calorie.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    protected Context mContext;
    protected List<T> mDataList = new ArrayList<>();
    protected LayoutInflater mInflater;
    protected OnItemClickListener mOnclickListener;
    protected RecyclerView mRecyclerView;

    public BaseRecyclerAdapter(Context context) {
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseRecyclerAdapter(Context context, List<T> datas) {
        this.mContext = context;
        mDataList.addAll(datas);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseRecyclerAdapter(Context context, T[] datas) {
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Collections.addAll(mDataList, datas);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /** 更新数据，替换原有数据 */
    public void updateItems(List<T> items) {
        mDataList = items;
        notifyDataSetChanged();
    }

    /** 插入一条数据 */
    public void addItem(T item) {
        mDataList.add(0, item);
        notifyItemInserted(0);
    }

    /** 插入一条数据 */
    public void addItem(T item, int position) {
        position = Math.min(position, mDataList.size());
        mDataList.add(position, item);
        notifyItemInserted(position);
    }

    /** 在列表尾添加一串数据 */
    public void addItems(List<T> items) {
        int start = mDataList.size();
        mDataList.addAll(items);
        notifyItemRangeInserted(start, items.size());
    }

    /** 移除一条数据 */
    public void removeItem(int position) {
        if (position > mDataList.size() - 1) {
            return;
        }
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    /** 移除一条数据 */
    public void removeItem(T item) {
        int position = 0;
        ListIterator<T> iterator = mDataList.listIterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (next == item) {
                iterator.remove();
                notifyItemRemoved(position);
            }
            position++;
        }
    }

    /** 清除所有数据 */
    public void removeAll() {
        int count = mDataList.size();
        mDataList.clear();
        notifyItemRangeRemoved(0,count);
    }
    /**
     * 替换
     */
    public void replaceItems(List<T> datas){
        mDataList.clear();
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 点击事件
     */
    public interface OnItemClickListener<T>{
        void setItemClickListener(int position, T data, int viewType);
    }
    public void setItemClickLietener(OnItemClickListener<T> clickLietener){
        this.mOnclickListener =clickLietener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }
    @Override
    public void onClick(View v) {
        int childAdapterPosition = mRecyclerView.getChildAdapterPosition(v);
        if (mOnclickListener !=null) {
            mOnclickListener.setItemClickListener(childAdapterPosition, mDataList.get(childAdapterPosition),getItemViewType(childAdapterPosition));
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateVH(parent,viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.itemView.setOnClickListener(this);
        onBindVH(holder,position,getItemViewType(position),mDataList.get(position));
    }

    protected abstract VH onCreateVH(ViewGroup parent, int viewType);
    protected abstract void onBindVH(VH holder, int position ,int viewType,T data);
}
