package bjtu.practice.app.restaurant.function;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.activity.ChoseCanteenActivity;
import bjtu.practice.app.restaurant.model.CanteensItem;


public class SelectAdapter_XuanCanTing extends RecyclerView.Adapter<SelectAdapter_XuanCanTing.ViewHolder>{
    private ChoseCanteenActivity activity;
    private SparseArray<CanteensItem> dataList;
    private NumberFormat nf;
    private LayoutInflater mInflater;
    public SelectAdapter_XuanCanTing(ChoseCanteenActivity activity, SparseArray<CanteensItem> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_canteens,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CanteensItem item = dataList.valueAt(position);
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
        private CanteensItem item;
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
//                case R.id.tvAdd:
//                    activity.add(item, true);
//                    break;
//                case R.id.tvMinus:
//                    activity.remove(item, true);
//                    break;
                default:
                    break;
            }
        }

        public void bindData(CanteensItem item){
            this.item = item;
            tvName.setText(item.name);
            tvCost.setText(nf.format(item.count*item.price));
            tvCount.setText(String.valueOf(item.count));
        }
    }
}
