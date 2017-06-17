package bjtu.practice.app.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.function.UserDataManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends Activity {                 //登录界面活动

    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                 //注册按钮
    private Button mLoginButton;                     //登录按钮
    private CheckBox mRememberCheck;                //记住密码勾选框
    private SharedPreferences login_sp;              //内部数据库
    private TextView mChangepwdText;                //修改密码文本
    private UserDataManager mUserDataManager;         //用户数据管理类
    private final OkHttpClient client = new OkHttpClient(); //okhttp对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //通过id找到相应的控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mChangepwdText = (TextView) findViewById(R.id.login_text_change_pwd);
        mRememberCheck = (CheckBox) findViewById(R.id.Login_Remember);

        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        mRegisterButton.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(mListener);
        mChangepwdText.setOnClickListener(mListener);

        ImageView image = (ImageView) findViewById(R.id.logo);             //使用ImageView显示logo
        image.setImageResource(R.drawable.logo);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    OnClickListener mListener = new OnClickListener() {                  //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_register:                            //登录界面的注册按钮
                    Intent intent_Login_to_Register = new Intent(Login.this,Register.class) ;    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login_btn_login:                              //登录界面的登录按钮
                        login();
                    break;
                case R.id.login_text_change_pwd:                             //登录界面的注销按钮
                    Intent intent_Login_to_reset = new Intent(Login.this,Resetpwd.class) ;    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_reset);
                    finish();
                    break;
            }
        }
    };

    public void login(){                                              //登录按钮监听事件
        String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
        String userPwd = mPwd.getText().toString().trim();
        SharedPreferences.Editor editor =login_sp.edit();
        if (isUserNameAndPwdValid(userName,userPwd)) {
            //int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            //int result=httpConnect.query(userName,userPwd);
            if(isUserNameAndPwdCorrect(userName,userPwd)){                                             //返回1说明用户名和密码均正确
                //保存用户名和密码
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);

                //是否记住密码
                if(mRememberCheck.isChecked()){
                    editor.putBoolean("mRememberCheck", true);
                }else{
                    editor.putBoolean("mRememberCheck", false);
                }
                editor.commit();

                Intent intent = new Intent(Login.this,ChoseCanteenActivity.class) ;    //切换Login Activity至ShoppingCartActivity Activity
                intent.putExtra("username", userName);
                startActivity(intent);
                finish();
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();//登录成功提示
            }else{
                Toast.makeText(this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }
    }
    /*public void cancel() {           //注销
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if(result==1){                                             //返回1说明用户名和密码均正确
//                Intent intent = new Intent(Login.this,User.class) ;    //切换Login Activity至User Activity
//                startActivity(intent);
                Toast.makeText(this, getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();//登录成功提示
                mPwd.setText("");
                mAccount.setText("");
                mUserDataManager.deleteUserDatabyname(userName);
            }else if(result==0){
                Toast.makeText(this, getString(R.string.cancel_fail), Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }

    }*/

    public boolean isUserNameAndPwdValid(String userName,String password) {
        if (userName.equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isUserNameAndPwdCorrect(String userName,String password){
        /*FormBody.Builder params=new FormBody.Builder();
        params.add("username", userName);
        params.add("password", password);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url("http://192.168.199.169:8080/user")
                .post(params.build())
                .build();

        Response response = null;
        boolean success = false;
        try {
            response = okHttpClient.newCall(request).execute();
            if(!("".equals(response.body().string()))){
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        boolean success = false;
        Request request = new Request.Builder()
                .url("http://" + getString(R.string.ip) + ":8080" + "/user" + "?" + "username=" + userName + "&" + "password=" + password)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.isSuccessful()){
            success = true;
            try {
                if(("".equals(response.body().string()))){
                    success = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(success==true){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }

}
