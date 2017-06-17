package bjtu.practice.app.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.text.NumberFormat;
import java.util.ArrayList;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.function.DetailOrderAdapter;
import bjtu.practice.app.restaurant.function.SelectAdapter_XiangQingDingDan;
import bjtu.practice.app.restaurant.model.DetailOrdersItem;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DetailOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgCart;
    private ViewGroup anim_mask_layout;
    private RecyclerView rvType,rvSelected;
    private TextView tvCount,tvCost,tvSubmit,tvTips;
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private StickyListHeadersListView listView;


    private ArrayList<DetailOrdersItem> dataList,typeList;
    private SparseArray<DetailOrdersItem> selectedList;
    private SparseIntArray groupSelect;

    private DetailOrderAdapter myAdapter;
    private SelectAdapter_XiangQingDingDan selectAdapter;
//    private TypeAdapter typeAdapter;

    private NumberFormat nf;
    private Handler mHanlder;

    private String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_order);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mHanlder = new Handler(getMainLooper());
        dataList = DetailOrdersItem.getGoodsList(orderId);
//        typeList = OrdersItem.getTypeList();
        selectedList = new SparseArray<>();
        groupSelect = new SparseIntArray();
        initView();
    }

    private void initView(){
        tvCost = (TextView) findViewById(R.id.tvCost);

        anim_mask_layout = (RelativeLayout) findViewById(R.id.containerLayout);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);

        listView = (StickyListHeadersListView) findViewById(R.id.itemListView);

        myAdapter = new DetailOrderAdapter(dataList,this , orderId);
        listView.setAdapter(myAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        }
        );
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){

            default:
                break;
        }
    }
}
