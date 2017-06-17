package bjtu.practice.app.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.text.NumberFormat;
import java.util.ArrayList;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.function.DividerDecoration;
import bjtu.practice.app.restaurant.function.OrderAdapter;
import bjtu.practice.app.restaurant.function.OrderTypeAdapter;
import bjtu.practice.app.restaurant.function.SelectAdapter_ChaDingDan;
import bjtu.practice.app.restaurant.model.OrdersItem;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChoseOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgCart;
    private ViewGroup anim_mask_layout;
    private RecyclerView rvType,rvSelected;
    private TextView tvCount,tvCost,tvSubmit,tvTips;
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private StickyListHeadersListView listView;


    private ArrayList<OrdersItem> dataList,typeList;
    private SparseArray<OrdersItem> selectedList;
    private SparseIntArray groupSelect;

    private OrderAdapter myAdapter;
    private SelectAdapter_ChaDingDan selectAdapter;
    private OrderTypeAdapter typeAdapter;

    private NumberFormat nf;
    private Handler mHanlder;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mHanlder = new Handler(getMainLooper());
        dataList = OrdersItem.getGoodsList(userName);
        typeList = OrdersItem.getTypeList(userName);
        selectedList = new SparseArray<>();
        groupSelect = new SparseIntArray();
        initView();
    }

    private void initView(){
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvCost = (TextView) findViewById(R.id.tvCost);

        rvType = (RecyclerView) findViewById(R.id.OrderTypeRecyclerView);

        anim_mask_layout = (RelativeLayout) findViewById(R.id.containerLayout);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);

        listView = (StickyListHeadersListView) findViewById(R.id.itemListView);

        rvType.setLayoutManager(new LinearLayoutManager(this));
        typeAdapter = new OrderTypeAdapter(this,typeList);
        rvType.setAdapter(typeAdapter);
        rvType.addItemDecoration(new DividerDecoration(this));

        myAdapter = new OrderAdapter(dataList,this);
        listView.setAdapter(myAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //if(dataList.get(firstVisibleItem) != null){
         //           OrdersItem item = dataList.get(firstVisibleItem);
                //}
                //else{
                //    Toast.makeText(ChoseOrderActivity.this, "还没有订单哦！" , Toast.LENGTH_SHORT).show();
                //}

                OrdersItem item  = null;
                try{
                    item = dataList.get(firstVisibleItem);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    return;
                }
                if(typeAdapter.selectTypeId != item.typeId) {
                    typeAdapter.selectTypeId = item.typeId;
                    typeAdapter.notifyDataSetChanged();
                    rvType.smoothScrollToPosition(getSelectedGroupPosition(item.typeId));
                }
            }
        });

    }

    //根据类别Id获取属于当前类别的数量
    public int getSelectedGroupCountByTypeId(int typeId){
        return groupSelect.get(typeId);
    }
    //根据类别id获取分类的Position 用于滚动左侧的类别列表
    public int getSelectedGroupPosition(int typeId){
        for(int i=0;i<typeList.size();i++){
            if(typeId==typeList.get(i).typeId){
                return i;
            }
        }
        return 0;
    }

    public void onTypeClicked(int typeId){
        listView.setSelection(getSelectedPosition(typeId));
    }

    private int getSelectedPosition(int typeId){
        int position = 0;
        for(int i=0;i<dataList.size();i++){
            if(dataList.get(i).typeId == typeId){
                position = i;
                break;
            }
        }
        return position;
    }

    public void playAnimation(int[] start_location){
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.button_add);
        setAnim(img,start_location);
    }

    private Animation createAnim(int startX,int startY){
        int[] des = new int[2];
        imgCart.getLocationInWindow(des);

        AnimationSet set = new AnimationSet(false);

        Animation translationX = new TranslateAnimation(0, des[0]-startX, 0, 0);
        translationX.setInterpolator(new LinearInterpolator());
        Animation translationY = new TranslateAnimation(0, 0, 0, des[1]-startY);
        translationY.setInterpolator(new AccelerateInterpolator());
        Animation alpha = new AlphaAnimation(1,0.5f);
        set.addAnimation(translationX);
        set.addAnimation(translationY);
        set.addAnimation(alpha);
        set.setDuration(500);

        return set;
    }

    private void addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {

        int x = location[0];
        int y = location[1];
        int[] loc = new int[2];
        vg.getLocationInWindow(loc);
        view.setX(x);
        view.setY(y-loc[1]);
        vg.addView(view);
    }

    private void setAnim(final View v, int[] start_location) {

        addViewToAnimLayout(anim_mask_layout, v, start_location);
        Animation set = createAnim(start_location[0],start_location[1]);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim_mask_layout.removeView(v);
                    }
                },100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(set);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){

            default:
                break;
        }
    }

    public void jump(String orderId){
        Intent intent = new Intent(ChoseOrderActivity.this,DetailOrderActivity.class) ;
        //intent.putExtra("username", userName);
        intent.putExtra("orderId", orderId);
        startActivity(intent);
    }
}
