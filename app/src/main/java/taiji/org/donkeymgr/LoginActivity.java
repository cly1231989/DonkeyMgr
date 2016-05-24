package taiji.org.donkeymgr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import taiji.org.donkeymgr.bean.OperaResult;
import taiji.org.donkeymgr.bean.UserInfo;
import taiji.org.donkeymgr.msgs.LoginResultMsgEvent;
import taiji.org.donkeymgr.utils.SettingUtils;

public class LoginActivity extends ToolBarActivity {
    Button loginBtn;
    EditText accountEditText;
    EditText pwdEditText;
    CheckBox remeberCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.loginButton);
        accountEditText = (EditText)findViewById(R.id.accountEditText);
        pwdEditText = (EditText)findViewById(R.id.pwdEditText);
        remeberCheckBox = (CheckBox)findViewById(R.id.remeberCheckBox);

        UserInfo userInfo = new UserInfo();
        SettingUtils.getUserInfo(this, userInfo);
        if(userInfo.getUsername().compareTo("") != 0) {
            accountEditText.setText(userInfo.getUsername());
            pwdEditText.setText(userInfo.getPwd());
            remeberCheckBox.setChecked(true);
        }

        loginBtn.setOnClickListener(loginClick);

    }

    private View.OnClickListener loginClick = new View.OnClickListener() {

        ProgressDialog p_dialog;

        @Override
        public void onClick(View v) {
            if (accountEditText.getText().length() == 0){
                Toast.makeText(v.getContext(), "请输入账号", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pwdEditText.getText().length() == 0){
                Toast.makeText(v.getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = SettingUtils.makeServerAddress(LoginActivity.this, "login-rest");
            p_dialog = ProgressDialog
                    .show(LoginActivity.this,
                            "请稍候",
                            "正在为您登录...",
                            true);

            OkHttpUtils
                .post()
                .url(url)
                .addParams("name", accountEditText.getText().toString())
                .addParams("pwd", pwdEditText.getText().toString())
                .build()
                .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            p_dialog.cancel();
                            SettingUtils.setIsOnline(false);
                            Toast.makeText(LoginActivity.this, "登录过程发生错误 " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response) {
                            p_dialog.cancel();

                            OperaResult operaResult = JSON.parseObject(response, OperaResult.class);
                            if (!operaResult.isSuccess()) {
                                SettingUtils.setIsOnline(false);
                                Toast.makeText(LoginActivity.this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                            } else {
                                if (remeberCheckBox.isChecked()) {
                                    UserInfo userInfo = new UserInfo( accountEditText.getText().toString(), pwdEditText.getText().toString() );
                                    SettingUtils.saveUserInfo(LoginActivity.this, userInfo);
                                }

                                EventBus.getDefault().post( new LoginResultMsgEvent(true) );
                                LoginActivity.this.finish();
                            }
                        }
                    });
        }
    };
}
