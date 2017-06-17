package bjtu.practice.app.restaurant.function;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.activity.ChoseOrderActivity;
import bjtu.practice.app.restaurant.model.OrdersItem;

public class OrderTypeAdapter extends RecyclerView.Adapter<OrderTypeAdapter.ViewHolder> {
    public int selectTypeId;

    public ChoseOrderActivity activity;
    public ArrayList<OrdersItem> dataList;

    public OrderTypeAdapter(ChoseOrderActivity activity, ArrayList<OrdersItem> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrdersItem item = dataList.get(position);

        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if(dataList==null){
            return 0;
        }
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvCount,type;
        private OrdersItem item;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            type = (TextView) itemView.findViewById(R.id.type);
            itemView.setOnClickListener(this);
        }

        public void bindData(OrdersItem item){
            this.item = item;
            type.setText(item.typeName);
            int count = activity.getSelectedGroupCountByTypeId(item.typeId);
            tvCount.setText(String.valueOf(count));
            if(count<1){
                tvCount.setVisibility(View.GONE);
            }else{
                tvCount.setVisibility(View.VISIBLE);
            }
            if(item.typeId==selectTypeId){
                itemView.setBackgroundColor(Color.WHITE);
            }else{
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }

        }

        @Override
        public void onClick(View v) {
            activity.onTypeClicked(item.typeId);
        }
    }
}
