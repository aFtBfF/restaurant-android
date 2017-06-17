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

public class CanteensItem {
    public int id;
    public int typeId;
    public int rating;
    public String name;
    public String typeName;
    public double price;
    public int count;
    public String picture;
    private static String IP = "192.168.43.238";

    public CanteensItem(int id, double price, String name, int typeId, String typeName , String picture) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.typeId = typeId;
        this.typeName = typeName;
        this.picture = picture;
        rating = new Random().nextInt(5)+1;
    }

    private static ArrayList<CanteensItem> goodsList;
    private static ArrayList<CanteensItem> typeList;

    private static void initData(){//这是各区域的餐厅的数据
        goodsList = new ArrayList<>();
        typeList = new ArrayList<>();
        CanteensItem item = null;
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url("http://"+ IP + ":8080" + "/restaurants")
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String restaurants = null;
                restaurants = response.body().string();
                if (!("".equals(restaurants))) {
                    JSONArray array = null;
                    array = new JSONArray(restaurants);
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = null;
                        object = array.getJSONObject(j);
                        String name = object.getString("name");
                        String pic = object.getString("pic");
                        int id = object.getInt("id");

                        item = new CanteensItem(id , 0 , name , 1 , "北京交通大学" , pic);
                        goodsList.add(item);
                    }
                    typeList.add(item);
                }
                /*for (int i = 1; i < 15; i++) {
                    for (int j = 1; j < 10; j++) {
                        item = new GoodsItem(100 * i + j, Math.random() * 100, "商品" + (100 * i + j), i, "种类" + i);
                        goodsList.add(item);
                    }
                    typeList.add(item);
                }*/
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
                item = new CanteensItem(10*i+j, Math.random()*10,"食堂"+(10*i+j),i,"区域"+i);
                goodsList.add(item);
            }
            typeList.add(item);
        }*/
    }

    public static ArrayList<CanteensItem> getGoodsList(){
        if(goodsList != null){
            goodsList.clear();
            goodsList = null;
        }
        if(goodsList==null){
            initData();
        }
        return goodsList;
    }
    public static ArrayList<CanteensItem> getTypeList(){
        if(typeList != null){
            typeList.clear();
            typeList = null;
        }
        if(typeList==null){
            initData();
        }
        return typeList;
    }
}
