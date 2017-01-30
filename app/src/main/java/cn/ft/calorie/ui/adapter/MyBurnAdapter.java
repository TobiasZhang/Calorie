package cn.ft.calorie.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.TextureMapView;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.pojo.BurnRecord;
import cn.ft.calorie.pojo.sectionrecyclerview.MyIntakeSection;
import cn.ft.calorie.util.TimeUtils;
import cn.ft.calorie.widget.SectionedExpandableGridAdapter;

/**
 * Created by TT on 2017/1/21.
 */
public class MyBurnAdapter extends SectionedExpandableGridAdapter<MyIntakeSection, BurnRecord, RecyclerView.ViewHolder> {
    public final int VIEW_TYPE_SECTION = R.layout.recyclerview_item_section;
    public final int VIEW_TYPE_ITEM = R.layout.recyclerview_item_my_burn;


    public MyBurnAdapter(Context context, GridLayoutManager gridLayoutManager) {
        super(context, gridLayoutManager);
    }

    @Override
    protected boolean isSection(int position) {
        return mDataList.get(position) instanceof MyIntakeSection;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(viewType, parent, false);
        if (viewType == VIEW_TYPE_ITEM)
            return new MyVH4Item(view);
        else
            return new MyVH4Section(view);
    }

    @Override
    protected void onBindVH(RecyclerView.ViewHolder holder, int position, int viewType, Object data) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                MyVH4Item holder4Item = (MyVH4Item) holder;
                final BurnRecord item = (BurnRecord) data;
                holder4Item.distanceTxt.setText("跑步" + item.getDistance() + "m");
                holder4Item.durationTxt.setText("用时 " + TimeUtils.formatSeconds(item.getDuration()));
                holder4Item.speedTxt.setText("速度" + item.getSpeed() + "m/s");
                holder4Item.calorieTxt.setText(item.getCalorie() + mContext.getString(R.string.calorieUnit));
                holder4Item.mapImage.setImageURI(item.getMapImage());
                break;
            case VIEW_TYPE_SECTION:
                MyVH4Section holder4Section = (MyVH4Section) holder;
                final MyIntakeSection section = (MyIntakeSection) data;
                holder4Section.dateTxt.setText(section.getDate());
                holder4Section.totalCalorieTxt.setText(section.getCalorie() + mContext.getString(R.string.calorieUnit));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position)) {
            return VIEW_TYPE_SECTION;
        } else
            return VIEW_TYPE_ITEM;
    }

    class MyVH4Section extends RecyclerView.ViewHolder {
        //for section
        @BindView(R.id.dateTxt)
        TextView dateTxt;
        @BindView(R.id.intakeCalorieTxt)
        TextView totalCalorieTxt;

        public MyVH4Section(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class MyVH4Item extends RecyclerView.ViewHolder {
        //for item
        @BindView(R.id.distanceTxt)
        TextView distanceTxt;
        @BindView(R.id.durationTxt)
        TextView durationTxt;
        @BindView(R.id.speedTxt)
        TextView speedTxt;
        @BindView(R.id.calorieTxt)
        TextView calorieTxt;
        @BindView(R.id.mapImage)
        SimpleDraweeView mapImage;

        public MyVH4Item(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void sectionStateChanged(Section section) {
        if (!mRecyclerView.isComputingLayout()) {
            section.isExpanded = !section.isExpanded;
            myNotifyDataSetChanged();
        }
    }
}
