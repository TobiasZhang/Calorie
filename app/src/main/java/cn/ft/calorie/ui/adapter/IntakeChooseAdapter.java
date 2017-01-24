package cn.ft.calorie.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.pojo.Food;
import cn.ft.calorie.util.TimeUtils;
import cn.ft.calorie.widget.BaseRecyclerAdapter;

/**
 * Created by TT on 2017/1/21.
 */
public class IntakeChooseAdapter extends BaseRecyclerAdapter<Food, IntakeChooseAdapter.MyVH4Item> {
    public IntakeChooseAdapter(Context context) {
        super(context);
    }

    @Override
    protected MyVH4Item onCreateVH(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_intake_choose, parent, false);
        return new MyVH4Item(view);
    }
    @Override
    protected void onBindVH(MyVH4Item holder4Item, int position, int viewType, Food item) {
        holder4Item.foodNameTxt.setText(item.getName());
    }

    class MyVH4Item extends RecyclerView.ViewHolder {
        @BindView(R.id.foodNameTxt)
        TextView foodNameTxt;

        public MyVH4Item(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
