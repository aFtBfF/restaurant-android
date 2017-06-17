package bjtu.practice.app.restaurant.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrdersItem {
    public String id;
    public int typeId;
    public int rating;
    public String name;
    public String typeName;
    public double price;
    public int count;
    public String orderTime;
    private static String IP = "192.168.43.238";

    public OrdersItem(String id, double price, String orderTime , int typeId , String typeName) {
        this.id = id;
        this.price = price;
        this.orderTime = orderTime;
        this.name = name;
        this.typeId = typeId;
        this.typeName = typeName;
        rating = new Random().nextInt(5)+1;
    }

    private static ArrayList<OrdersItem> goodsList;
    private static ArrayList<OrdersItem> typeList;

    private static void initData(String userName){//这是各区域的餐厅的数据
        goodsList = new ArrayList<>();
        typeList = new ArrayList<>();
        OrdersItem item = null;

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url("http://"+ IP + ":8080" + "/orders/user/" + userName)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String order = null;
                order = response.body().string();
                if (!("".equals(order))) {
                    JSONArray array = null;
                    array = new JSONArray(order);
                    int preState = -10;
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = null;
                        object = array.getJSONObject(j);
                        double price = object.getDouble("price");
                        String orderTime = object.getString("ordertime");
                        String id = object.getString("id");
                        int state = object.getInt("state");

                        String stateName = null;
                        switch(state) {
                            case (0):
                                stateName = "等待接单";
                                break;
                            case (1):
                                stateName = "餐厅已接单";
                                break;
                            case (2):
                                stateName = "等待取餐";
                                break;
                            case (3):
                                stateName = "订单完成";
                                break;
                            case (4):
                                stateName = "订单取消";
                                break;
                        }

                        item = new OrdersItem(id , price , orderTime , state , stateName);
                        goodsList.add(item);
                        if(state != preState){
                            typeList.add(item);
                        }
                        preState = state;
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


        /*for(int i=1;i<4;i++){
            for(int j=1;j<6;j++){
                item = new OrdersItem(10*i+j,Math.random()*10,"订单"+(10*i+j),i,"我的订单");
                goodsList.add(item);
            }
            typeList.add(item);
        }*/
    }

    public static ArrayList<OrdersItem> getGoodsList(String userName){
        if(goodsList != null){
            goodsList.clear();
            goodsList = null;
        }
        if(goodsList==null){
            initData(userName);
        }
        return goodsList;
    }
    public static ArrayList<OrdersItem> getTypeList(String userName){
        if(typeList != null){
            typeList.clear();
            typeList = null;
        }
        if(typeList==null){
            initData(userName);
        }
        return typeList;
    }
}
