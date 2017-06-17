package bjtu.practice.app.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import bjtu.practice.app.restaurant.R;

public class User extends AppCompatActivity {
    private Button mReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mReturnButton = (Button)findViewById(R.id.returnback);

    }
    public void back_to_login(View view) {
        //setContentView(R.layout.login);
        Intent intent3 = new Intent(User.this,Login.class) ;
        startActivity(intent3);
        finish();

    }
}
