package taiji.org.donkeymgr;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import taiji.org.donkeymgr.utils.SettingUtils;

public class SettingActivity extends ToolBarActivity {

    Switch autoSyncSwitch;
    Switch wifiSyncSwitch;
    EditText serverAddress;
    Button okBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        autoSyncSwitch = (Switch)findViewById(R.id.autoSyncSwitch);
        wifiSyncSwitch = (Switch)findViewById(R.id.wifiSyncSwitch);
        serverAddress = (EditText)findViewById(R.id.serverAddressEditText);
        okBtn = (Button)findViewById(R.id.okButton);

        serverAddress.setText( SettingUtils.getServerAddress(SettingActivity.this) );
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serverAddress.getText().toString().length() == 0){
                    Toast.makeText(SettingActivity.this, "请输入服务器地址", Toast.LENGTH_SHORT).show();
                }

                SettingUtils.saveServerAddress( SettingActivity.this, serverAddress.getText().toString() );
                Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                SettingActivity.this.finish();
            }
        });

        autoSyncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingUtils.setAutoync(SettingActivity.this, isChecked);
            }
        });

        wifiSyncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingUtils.setWifiSync(SettingActivity.this, isChecked);
            }
        });
    }
}
