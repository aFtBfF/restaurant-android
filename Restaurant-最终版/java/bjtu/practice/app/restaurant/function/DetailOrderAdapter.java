package bjtu.practice.app.restaurant.function;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.activity.DetailOrderActivity;
import bjtu.practice.app.restaurant.model.DetailOrdersItem;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class DetailOrderAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<DetailOrdersItem> dataList;

    private NumberFormat nf;
    private LayoutInflater mInflater;

    private static String IP = "192.168.43.238";
    private String orderId;

    public DetailOrderAdapter(ArrayList<DetailOrdersItem> dataList, DetailOrderActivity mContext , String orderId) {
        this.dataList = dataList;
        this.orderId = orderId;

        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.activity_view_top, parent, false);
        }
        TextView tvOrderID_import = (TextView) convertView.findViewById(R.id.tvOrderID_import);
        TextView tvOrderTime_import = (TextView) convertView.findViewById(R.id.tvOrderTime_import);
        TextView tvCanteenID_import = (TextView) convertView.findViewById(R.id.tvCanteenID_import);
        TextView tvPrice_import = (TextView) convertView.findViewById(R.id.tvPrice_import);
        TextView tvOrderState_import = (TextView) convertView.findViewById(R.id.tvOrderState_import);

        int state = 0;
        String canteenName = null;
        String id = null, orderTime = null;
        double price = 0;

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url("http://"+ IP + ":8080" + "/orders/order/" + orderId)
                    .build();
            System.out.println(orderId);
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String order = null;
                order = response.body().string();
                JSONObject object = new JSONObject(order);
                id = object.getString("id");
                orderTime = object.getString("ordertime");
                canteenName = object.getString("restaurant");
                price = object.getDouble("price");
                state = object.getInt("state");
            }
        }
        catch(IOException e1){
            e1.printStackTrace();
        }
        catch(JSONException e2){
            e2.printStackTrace();
        }

        tvOrderID_import.setText(id);
        tvOrderTime_import.setText(orderTime);
        tvCanteenID_import.setText(""+canteenName);
        tvPrice_import.setText(""+price);
        switch(state){
            case(0):tvOrderState_import.setText("等待接单");break;
            case(1):tvOrderState_import.setText("餐厅已接单");break;
            case(2):tvOrderState_import.setText("等待取餐");break;
            case(3):tvOrderState_import.setText("订单完成");break;
            case(4):tvOrderState_import.setText("订单取消");break;
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return dataList.get(position).number;
    }

    @Override
    public int getCount() {
        if(dataList==null){
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_detailorders,parent,false);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ItemViewHolder) convertView.getTag();
        }
        DetailOrdersItem item = dataList.get(position);
        holder.bindData(item);
        return convertView;
    }

    class ItemViewHolder implements View.OnClickListener{
        private TextView name,fromWindow,number;
        private DetailOrdersItem item;


        public ItemViewHolder(View itemView) {
            name = (TextView) itemView.findViewById(R.id.tvName_import);
            fromWindow = (TextView) itemView.findViewById(R.id.tvFromWindow_import);
            number = (TextView) itemView.findViewById(R.id.tvNum_import);

        }

        public void bindData(DetailOrdersItem item){
            this.item = item;
            name.setText(""+item.dish);
            fromWindow.setText(""+item.window);
            number.setText(""+item.number);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
