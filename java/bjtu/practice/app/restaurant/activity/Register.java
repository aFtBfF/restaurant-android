package bjtu.practice.app.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import bjtu.practice.app.restaurant.R;
import bjtu.practice.app.restaurant.function.UserDataManager;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private UserDataManager mUserDataManager;         //用户数据管理类
    private final OkHttpClient client = new OkHttpClient(); //okhttp对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }

    }
    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:                       //确认按钮的监听事件
                    register_check();
                    break;
                case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }
    };
    public void register_check() {                                //确认按钮的监听事件
        String userName = mAccount.getText().toString().trim();
        String userPwd = mPwd.getText().toString().trim();
        String userPwdCheck = mPwdCheck.getText().toString().trim();
        if (isUserNameAndPwdValid(userName , userPwd , userPwdCheck)) {
            if(userPwd.equals(userPwdCheck)==false){     //两次密码输入不一样
                Toast.makeText(this, getString(R.string.pwd_not_the_same), Toast.LENGTH_SHORT).show();
                return ;
            }
            //检查用户是否存在
            //int count=mUserDataManager.findUserByName(userName);
            //用户已经存在时返回，给出提示文字
            if(isRegisterSuccess(userName,userPwd)){
                /*Toast.makeText(this, getString(R.string.name_already_exist, userName), Toast.LENGTH_SHORT).show();
                return ;*/
                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换User Activity至Login Activity
                startActivity(intent_Register_to_Login);
                finish();
            }
            else {
                Toast.makeText(this, getString(R.string.name_already_exist, userName), Toast.LENGTH_SHORT).show();
                return ;
                /*UserData mUser = new UserData(userName, userPwd);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser); //新建用户信息
                if (flag == -1) {
                    Toast.makeText(this, getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                    Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                }*/
            }
        }
    }

    private boolean isRegisterSuccess(String userName, String password) {
        boolean success = false;
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("username",userName);
        builder.add("password",password);
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url("http://" + getString(R.string.ip) + ":8080" + "/user")
                .post(formBody)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.isSuccessful())
        {
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

    public boolean isUserNameAndPwdValid(String userName , String password , String passwordCheck) {
        if (userName.equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(passwordCheck.equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
