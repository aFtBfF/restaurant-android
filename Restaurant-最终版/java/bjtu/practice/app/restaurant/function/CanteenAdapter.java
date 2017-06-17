package bjtu.practice.app.restaurant.function;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.activity.ChoseCanteenActivity;
import bjtu.practice.app.restaurant.model.CanteensItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class CanteenAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<CanteensItem> dataList;
    private ChoseCanteenActivity mContext;
    private NumberFormat nf;
    private LayoutInflater mInflater;
    private String userName;

    private static final int SUCCESS = 1;
    private static final int FALL = 2;

    public CanteenAdapter(ArrayList<CanteensItem> dataList, ChoseCanteenActivity mContext , String userName) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.userName = userName;
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
            convertView = mInflater.inflate(R.layout.item_canteens,parent,false);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ItemViewHolder) convertView.getTag();
        }
        CanteensItem item = dataList.get(position);
        holder.bindData(item);
        return convertView;
    }

    class ItemViewHolder implements View.OnClickListener{
        private TextView name,price,tvAdd,tvMinus,tvCount;
        private CanteensItem item;
        private RatingBar ratingBar;
        private ImageView picture;

        public Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    //加载网络成功进行UI的更新,处理得到的图片资源
                    case SUCCESS:
                        //通过message，拿到字节数组
                        byte[] Picture = (byte[]) msg.obj;
                        //使用BitmapFactory工厂，把字节数组转化为bitmap
                        Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                        //通过imageview，设置图片
                        picture.setImageBitmap(bitmap);

                        break;
                    //当加载网络失败执行的逻辑代码
                    case FALL:
                        Toast.makeText(mContext, "网络出现了问题", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        public ItemViewHolder(View itemView) {
            name = (TextView) itemView.findViewById(R.id.tvName);
           // price = (TextView) itemView.findViewById(R.id.tvPrice);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
            tvAdd = (TextView) itemView.findViewById(R.id.tvAdd);
            //ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            picture = (ImageView) itemView.findViewById(R.id.img);
            tvMinus.setOnClickListener(this);
            tvAdd.setOnClickListener(this);
        }

        public void bindData(CanteensItem item){
            this.item = item;
            name.setText(item.name);
            //ratingBar.setRating(item.rating);
//            item.count = mContext.getSelectedItemCountById(item.id);
            tvCount.setText(String.valueOf(item.count));
//            price.setText(nf.format(item.price));

            //1.创建一个okhttpclient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get
            Request request = new Request.Builder()
                    .url("http://" + mContext.getString(R.string.ip) + ":8080" + item.picture)
                    .build();
            //3.创建一个Call对象，参数是request对象，发送请求
            Call call = okHttpClient.newCall(request);
            //4.异步请求，请求加入调度
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //得到从网上获取资源，转换成我们想要的类型
                    byte[] Picture_bt = response.body().bytes();
                    //通过handler更新UI
                    Message message = handler.obtainMessage();
                    message.obj = Picture_bt;
                    message.what = SUCCESS;
                    handler.sendMessage(message);
                }
            });



            if(item.count<1){
                tvCount.setVisibility(View.GONE);
                tvMinus.setVisibility(View.GONE);
            }else{
                tvCount.setVisibility(View.VISIBLE);
                tvMinus.setVisibility(View.VISIBLE);
            }
        }

        @Override
        //这是选择餐厅页面的点击右箭头响应
        //本机应该开启新activity
        //向服务器发送请求餐厅的窗口信息和第一个窗口的菜品信息
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvAdd:
                    mContext.jump(String.valueOf(item.id));
                    break;
                default:
                    break;
            }






        }
    }

    private Animation getShowAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720, RotateAnimation.RELATIVE_TO_SELF,0.5f, RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,2f
                , TranslateAnimation.RELATIVE_TO_SELF,0
                , TranslateAnimation.RELATIVE_TO_SELF,0
                , TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    private Animation getHiddenAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720, RotateAnimation.RELATIVE_TO_SELF,0.5f, RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0
                , TranslateAnimation.RELATIVE_TO_SELF,2f
                , TranslateAnimation.RELATIVE_TO_SELF,0
                , TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1,0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }
}
