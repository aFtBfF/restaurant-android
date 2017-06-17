package bjtu.practice.app.restaurant.function;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.activity.DetailOrderActivity;
import bjtu.practice.app.restaurant.model.DetailOrdersItem;


public class SelectAdapter_XiangQingDingDan extends RecyclerView.Adapter<SelectAdapter_XiangQingDingDan.ViewHolder>{
    private DetailOrderActivity activity;
    private SparseArray<DetailOrdersItem> dataList;
    private NumberFormat nf;
    private LayoutInflater mInflater;
    public SelectAdapter_XiangQingDingDan(DetailOrderActivity activity, SparseArray<DetailOrdersItem> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_orders,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailOrdersItem item = dataList.valueAt(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if(dataList==null) {
            return 0;
        }
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private DetailOrdersItem item;
        private TextView tvCost,tvCount,tvAdd,tvMinus,tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
            tvAdd = (TextView) itemView.findViewById(R.id.tvAdd);
            tvMinus.setOnClickListener(this);
            tvAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                default:
                    break;
            }
        }

        public void bindData(DetailOrdersItem item){
            this.item = item;
        }
    }
}
