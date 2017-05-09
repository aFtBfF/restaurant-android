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

public class GoodsItem{
    public int id;
    public int typeId;
    public int rating;
    public String name;
    public String typeName;
    public double price;
    public int count;
    public String picture;
    private static String IP = "192.168.43.238";

    public GoodsItem(int id, double price, String name, int typeId, String typeName , String picture) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.typeId = typeId;
        this.typeName = typeName;
        this.picture = picture;
        rating = new Random().nextInt(5)+1;
    }

    private static ArrayList<GoodsItem> goodsList;
    private static ArrayList<GoodsItem> typeList;

    private static void initData(String restaurantId){
        goodsList = new ArrayList<>();
        typeList = new ArrayList<>();
        GoodsItem item = null;
        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url("http://" + IP + ":8080" + "/restaurants/" + restaurantId)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String windows = null;
                windows = response.body().string();
                if (!("".equals(windows))) {
                    JSONArray array = null;
                    array = new JSONArray(windows);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = null;
                        object = new JSONObject(array.getString(i));
                        int theWindow = object.getInt("id");

                        Request request2 = new Request.Builder()
                                .url("http://" + IP +":8080" + "/restaurants/" + restaurantId + "/windows/" + String.valueOf(theWindow))
                                .build();
                        Response response2 = null;
                        response2 = client.newCall(request2).execute();
                        if (response2.isSuccessful()) {
                            String menu = null;
                            menu = response2.body().string();
                            if (!("".equals(menu))) {
                                JSONArray array2 = null;
                                array2 = new JSONArray(menu);
                                for (int j = 0; j < array2.length(); j++) {
                                    JSONObject object2 = null;
                                    object2 = new JSONObject(array2.getString(j));
                                    double price = object2.getDouble("price");
                                    String restaurant = object2.getString("restaurant");
                                    String name = object2.getString("name");
                                    String pic = object2.getString("pic");
                                    int window = object2.getInt("window");
                                    int id = object2.getInt("id");

                                    item = new GoodsItem(id , price , name , window , "窗口" + String.valueOf(window) , pic);
                                    goodsList.add(item);
                                }
                            }
                        }
                        typeList.add(item);
                    }
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
    }

    public static ArrayList<GoodsItem> getGoodsList(String restaurantId){
        if(goodsList==null){
            initData(restaurantId);
        }
        return goodsList;
    }
    public static ArrayList<GoodsItem> getTypeList(String restaurantId){
        if(typeList==null){
            initData(restaurantId);
        }
        return typeList;
    }
}
