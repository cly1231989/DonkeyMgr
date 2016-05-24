package taiji.org.donkeymgr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import taiji.org.donkeymgr.bean.OperaResult;
import taiji.org.donkeymgr.msgs.LogoutMsgEvent;
import taiji.org.donkeymgr.utils.SettingUtils;

public class ModifyPwdActivity extends ToolBarActivity {

    Button modifyPwdBtn;
    Button logoutBtn;

    EditText oldPwdEditText;
    EditText newPwdEditText;
    EditText newPwdEditText1;

    ProgressDialog p_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);

        oldPwdEditText = (EditText)findViewById(R.id.oldPwdEditText);
        newPwdEditText = (EditText)findViewById(R.id.newPwdEditText);
        newPwdEditText1 = (EditText)findViewById(R.id.newPwdEditText2);

        logoutBtn = (Button)findViewById(R.id.logoutButton);
        modifyPwdBtn = (Button)findViewById(R.id.modifyPwdBtn);
        modifyPwdBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String url = SettingUtils.makeServerAddress(ModifyPwdActivity.this, "user/pwd");
                                                if (oldPwdEditText.getText().length() == 0) {
                                                    Toast.makeText(v.getContext(), "请输入原密码", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                if (newPwdEditText.getText().length() == 0) {
                                                    Toast.makeText(v.getContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                if (newPwdEditText.getText().toString().compareTo(newPwdEditText1.getText().toString()) != 0) {
                                                    Toast.makeText(v.getContext(), "两次输入的新密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                p_dialog = ProgressDialog
                                                    .show(ModifyPwdActivity.this,
                                                            "请稍等",
                                                            "正在为您修改密码...",
                                                            true);

                                                OkHttpUtils
                                                    .post()
                                                    .url(url)
                                                    .addParams("oldpwd", oldPwdEditText.getText().toString())
                                                    .addParams("pwd", newPwdEditText.getText().toString())
                                                    .build()
                                                    .execute(new StringCallback() {
                                                        @Override
                                                        public void onError(okhttp3.Call call, Exception e) {
                                                            p_dialog.cancel();
                                                            Toast.makeText(ModifyPwdActivity.this, "修改过程发生错误 " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            p_dialog.cancel();

                                                            OperaResult operaResult = JSON.parseObject(response, OperaResult.class);
                                                            if (operaResult.isSuccess()) {
                                                                Toast.makeText(ModifyPwdActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
//                                                                Intent intent = new Intent();
//                                                                intent.putExtra("result", 0);
//                                                                setResult(RESULT_OK, intent);
                                                                ModifyPwdActivity.this.finish();
                                                            } else if (operaResult.getResult().compareToIgnoreCase("old_pwd_error") == 0) {
                                                                Toast.makeText(ModifyPwdActivity.this, "原密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                                                            } else {

                                                            }
                                                        }
                                                    });
                                            }
                                        }
            );

        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                String url = "http://192.168.1.100:8080/DonkeyMgrSystem/logout-rest";
                OkHttpUtils
                        .post()
                        .url(url)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {

                            }
                        });

                SettingUtils.setIsOnline(false);
                SettingUtils.clearUserInfo(ModifyPwdActivity.this);
                EventBus.getDefault().post(new LogoutMsgEvent());
                ModifyPwdActivity.this.finish();
            }
        });
    }
}
