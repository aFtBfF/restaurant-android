package bjtu.practice.app.restaurant.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailOrdersItem {
    public int number;
    public String dish;
    public String window;
    private static String IP = "192.168.43.238";

    public DetailOrdersItem(int number , String dish , String window) {
        this.number = number;
        this.dish = dish;
        this.window = window;
    }

    private static ArrayList<DetailOrdersItem> goodsList;
//    private static ArrayList<DetailOrdersItem> typeList;

    private static void initData(String orderId){//是页面下半部分的餐品条目
        goodsList = new ArrayList<>();
//        typeList = new ArrayList<>();
        DetailOrdersItem item = null;

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url("http://"+ IP + ":8080" + "/orders/order/" + orderId)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String order = null;
                order = response.body().string();
                JSONObject jo = new JSONObject(order);
                String menu = jo.getString("menu");
                if (!("".equals(menu))) {
                    JSONArray array = null;
                    array = new JSONArray(menu);
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = null;
                        object = array.getJSONObject(j);
                        int number = object.getInt("number");
                        String dish = object.getString("dish");
                        String window = object.getString("window");

                        item = new DetailOrdersItem(number , dish , window);
                        goodsList.add(item);
                    }
                }
            }
        }
        catch(IOException e1){
            e1.printStackTrace();
        }
        catch(JSONException e2){
            e2.printStackTrace();
        }
        /*for(int i=1;i<2;i++){
            for(int j=1;j<6;j++){
                item = new DetailOrdersItem(10*i+j,Math.random()*10,"订单"+(10*i+j),i,"按照类型分类"+i);
                goodsList.add(item);
            }
//            typeList.add(item);
        }*/
    }

    public static ArrayList<DetailOrdersItem> getGoodsList(String orderId){
        if(goodsList != null){
            goodsList.clear();
            goodsList = null;
        }
        if(goodsList==null){
            initData(orderId);
        }
        return goodsList;
    }
//    public static ArrayList<DetailOrdersItem> getTypeList(){
//        if(typeList==null){
//            initData();
//        }
//        return typeList;
//    }
}
