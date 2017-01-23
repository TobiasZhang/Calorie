package cn.ft.calorie.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.binaryfork.spanny.Spanny;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.widget.BaseRecyclerAdapter;

/**
 * Created by TT on 2017/1/21.
 */
public class HomeSectionAdapter extends BaseRecyclerAdapter<Map<String, Object>, RecyclerView.ViewHolder> {
    private final int ITEM_HOME_INTAKE = R.layout.recyclerview_item_home_intake;
    private final int ITEM_HOME_BURN = R.layout.recyclerview_item_home_burn;
    private final int ITEM_HOME_WEIGHT = R.layout.recyclerview_item_home_weight;

    public HomeSectionAdapter(Context context) {
        super(context);
    }

    public HomeSectionAdapter(Context context, List<Map<String, Object>> datas) {
        super(context, datas);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        View view = mInflater.inflate(viewType, parent, false);
        switch (viewType) {
            case ITEM_HOME_INTAKE:
                vh = new IntakeVH(view);
                break;
            case ITEM_HOME_BURN:
                vh = new BurnVH(view);
                break;
            case ITEM_HOME_WEIGHT:
                vh = new WeightVH(view);
                break;
        }
        return vh;
    }

    @Override
    protected void onBindVH(RecyclerView.ViewHolder holder, int position, int viewType, Map<String, Object> data) {
        switch (viewType) {
            case ITEM_HOME_INTAKE://饮食记录
                IntakeVH intakeVH = (IntakeVH) holder;
                int intakeCalorie = Integer.parseInt(data.get("intakeCalorie").toString());
                if(intakeCalorie==0){
                    intakeVH.recordBlock.setVisibility(View.GONE);
                    intakeVH.unRecordBlock.setVisibility(View.VISIBLE);
                    intakeVH.processTagTxt.setText("未达成");
                }else{
                    intakeVH.recordBlock.setVisibility(View.GONE);
                    intakeVH.unRecordBlock.setVisibility(View.VISIBLE);
                    intakeVH.processTagTxt.setText("已达成");

                    Spanny intakeCalorieSpanny = new Spanny(intakeCalorie+"", new TextAppearanceSpan(mContext, R.style.HomeFooterGridBigTxt)).append("卡");
                    intakeVH.intakeCalorieTxt.setText(intakeCalorieSpanny);
                    Spanny intakefoodCategoryCountSpanny = new Spanny(data.get("foodCategoryCount").toString(), new TextAppearanceSpan(mContext, R.style.HomeFooterGridBigTxt)).append("种");
                    intakeVH.foodCategoryCountTxt.setText(intakefoodCategoryCountSpanny);
                }
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(mContext.getResources().getColor(R.color.homeIntakeForeground));
                gd.setShape(GradientDrawable.OVAL);
                intakeVH.editBtn.setBackground(gd);
                break;
            case ITEM_HOME_BURN://锻炼记录
                BurnVH burnVH = (BurnVH) holder;
                int intakeCalorie2 = Integer.parseInt(data.get("intakeCalorie").toString());
                int burnCalorie = Integer.parseInt(data.get("burnCalorie").toString());
                if(intakeCalorie2==0){
                    burnVH.recordBlock.setVisibility(View.GONE);
                    burnVH.unRecordBlock.setVisibility(View.VISIBLE);
                    burnVH.processTagTxt.setText("未达成");
                }else{
                    int remainingCalorie = intakeCalorie2 - burnCalorie;
                    burnVH.recordBlock.setVisibility(View.GONE);
                    burnVH.unRecordBlock.setVisibility(View.VISIBLE);
                    if(remainingCalorie > 0){
                        burnVH.processTagTxt.setText("进行中");

                    }
                    int process = (int) ((double)burnCalorie/intakeCalorie2*100);
                    burnVH.progressBar.setProgress(process);
                    burnVH.progressNumTxt.setText(burnVH.progressBar.getProgress()+"%");

                    Spanny burnCalorieSpanny = new Spanny(burnCalorie+"", new TextAppearanceSpan(mContext, R.style.HomeFooterGridBigTxt)).append("卡");
                    burnVH.burnCalorieTxt.setText(burnCalorieSpanny);
                    Spanny remainingCalorieSpanny =new Spanny(remainingCalorie+"" , new TextAppearanceSpan(mContext, R.style.HomeFooterGridBigTxt)).append("卡");
                    burnVH.remainingCalorieTxt.setText(remainingCalorieSpanny);

                }
                GradientDrawable gd2 = new GradientDrawable();
                gd2.setColor(mContext.getResources().getColor(R.color.homeBurnForeground));
                gd2.setShape(GradientDrawable.OVAL);
                burnVH.editBtn.setBackground(gd2);
                break;
            case ITEM_HOME_WEIGHT://体重记录
                WeightVH weightVH = (WeightVH) holder;
                int weight = Integer.parseInt(data.get("weight").toString());
                if(weight==0){
                    weightVH.recordBlock.setVisibility(View.GONE);
                    weightVH.unRecordBlock.setVisibility(View.VISIBLE);
                    weightVH.processTagTxt.setText("未达成");
                }else{
                    weightVH.recordBlock.setVisibility(View.GONE);
                    weightVH.unRecordBlock.setVisibility(View.VISIBLE);
                    weightVH.processTagTxt.setText("已达成");

                    weightVH.weightTxt.setText(weight + "kg");
                }
                GradientDrawable gd3 = new GradientDrawable();
                gd3.setColor(mContext.getResources().getColor(R.color.homeWeightForeground));
                gd3.setShape(GradientDrawable.OVAL);
                weightVH.editBtn.setBackground(gd3);
                break;
        }
    }
    @Override
    public int getItemViewType(int position) {
        int resLayout = -1;
        switch (position) {
            case 0:
                resLayout = ITEM_HOME_INTAKE;
                break;
            case 1:
                resLayout = ITEM_HOME_BURN;
                break;
            case 2:
                resLayout = ITEM_HOME_WEIGHT;
                break;
        }
        return resLayout;
    }

    class IntakeVH extends RecyclerView.ViewHolder {
        @BindView(R.id.processTagTxt)
        TextView processTagTxt;
        @BindView(R.id.foodCategoryCountTxt)
        TextView foodCategoryCountTxt;
        @BindView(R.id.intakeCalorieTxt)
        TextView intakeCalorieTxt;
        @BindView(R.id.editBtn)
        ImageButton editBtn;

        @BindView(R.id.unRecordBlock)
        ViewGroup unRecordBlock;
        @BindView(R.id.recordBlock)
        ViewGroup recordBlock;

        public IntakeVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BurnVH extends RecyclerView.ViewHolder {
        @BindView(R.id.processTagTxt)
        TextView processTagTxt;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.progressNumTxt)
        TextView progressNumTxt;
        @BindView(R.id.burnCalorieTxt)
        TextView burnCalorieTxt;
        @BindView(R.id.remainingCalorieTxt)
        TextView remainingCalorieTxt;
        @BindView(R.id.editBtn)
        ImageButton editBtn;

        @BindView(R.id.unRecordBlock)
        ViewGroup unRecordBlock;
        @BindView(R.id.recordBlock)
        ViewGroup recordBlock;

        public BurnVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class WeightVH extends RecyclerView.ViewHolder {
        @BindView(R.id.processTagTxt)
        TextView processTagTxt;
        @BindView(R.id.editBtn)
        ImageButton editBtn;
        @BindView(R.id.weightTxt)
        TextView weightTxt;

        @BindView(R.id.unRecordBlock)
        ViewGroup unRecordBlock;
        @BindView(R.id.recordBlock)
        ViewGroup recordBlock;

        public WeightVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
