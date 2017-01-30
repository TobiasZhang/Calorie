package cn.ft.calorie.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.widget.BaseRecyclerAdapter;

/**
 * Created by TT on 2017/1/21.
 */
public class IntakeCompleteAdapter extends BaseRecyclerAdapter<IntakeRecord, IntakeCompleteAdapter.MyVH4Item> {
    public IntakeCompleteAdapter(Context context) {
        super(context);
    }

    @Override
    protected MyVH4Item onCreateVH(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_intake_complete, parent, false);
        return new MyVH4Item(view);
    }

    @Override
    protected void onBindVH(MyVH4Item holder4Item, int position, int viewType, IntakeRecord item) {
        holder4Item.foodNameAndWeightTxt.setText(item.getFood().getName()+"|"+item.getFoodWeight()+"g");
        holder4Item.calorieTxt.setText(item.getCalorie() + mContext.getString(R.string.calorieUnit));
    }

    class MyVH4Item extends RecyclerView.ViewHolder {
        @BindView(R.id.foodNameAndWeightTxt)
        TextView foodNameAndWeightTxt;
        @BindView(R.id.calorieTxt)
        TextView calorieTxt;

        public MyVH4Item(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
