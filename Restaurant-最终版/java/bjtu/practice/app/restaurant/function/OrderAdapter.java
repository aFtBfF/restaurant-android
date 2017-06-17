package bjtu.practice.app.restaurant.function;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.activity.ChoseOrderActivity;
import bjtu.practice.app.restaurant.model.OrdersItem;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class OrderAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<OrdersItem> dataList;
    private ChoseOrderActivity mContext;
    private NumberFormat nf;
    private LayoutInflater mInflater;

    public OrderAdapter(ArrayList<OrdersItem> dataList, ChoseOrderActivity mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.item_header_quyu_view, parent, false);
        }
        ((TextView)(convertView)).setText(dataList.get(position).typeName);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return dataList.get(position).typeId;
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
            convertView = mInflater.inflate(R.layout.item_orders,parent,false);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ItemViewHolder) convertView.getTag();
        }
        OrdersItem item = dataList.get(position);
        holder.bindData(item);
        return convertView;
    }

    class ItemViewHolder implements View.OnClickListener{
        private TextView orderId,orderTime,price,tvAdd;
        private OrdersItem item;


        public ItemViewHolder(View itemView) {
            orderId = (TextView) itemView.findViewById(R.id.tvOrderId);
            orderTime = (TextView) itemView.findViewById(R.id.tvOrderTime);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
           // price = (TextView) itemView.findViewById(R.id.tvPrice);
            tvAdd = (TextView) itemView.findViewById(R.id.tvAdd);

            tvAdd.setOnClickListener(this);
        }

        public void bindData(OrdersItem item){
            this.item = item;
            orderId.setText(item.id);
            orderTime.setText(item.orderTime);
            price.setText(""+item.price);
//            item.count = mContext.getSelectedItemCountById(item.id);
//            tvCount.setText(String.valueOf(item.count));
//            price.setText(nf.format(item.price));
        }

        @Override
        //这是选择餐厅页面的点击右箭头响应
        //本机应该开启新activity
        //向服务器发送请求餐厅的窗口信息和第一个窗口的菜品信息
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvAdd:
                    mContext.jump(item.id);
                    break;
                default:
                    break;
            }
        }
    }

    private Animation getShowAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    private Animation getHiddenAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1,0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }
}
