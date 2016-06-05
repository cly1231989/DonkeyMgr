package taiji.org.donkeymgr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.RecyclerViewUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import taiji.org.donkeymgr.bean.OperaResult;
import taiji.org.donkeymgr.bean.UserInfo;
import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;
import taiji.org.donkeymgr.msgs.BeginSyncMessageEvent;
import taiji.org.donkeymgr.msgs.EndSyncMessageEvent;
import taiji.org.donkeymgr.msgs.LoginResultMsgEvent;
import taiji.org.donkeymgr.msgs.LogoutMsgEvent;
import taiji.org.donkeymgr.msgs.QRCodeScanResult;
import taiji.org.donkeymgr.utils.DaoUtils;
import taiji.org.donkeymgr.utils.HandlerUtils;
import taiji.org.donkeymgr.utils.NetworkUtils;
import taiji.org.donkeymgr.utils.SettingUtils;
import taiji.org.donkeymgr.utils.UploadImageThread;

public class MainActivity extends AppCompatActivity {

    private int lastIndex = -1;
    //private int searchNum = -1;
    private boolean isSync = false;
    private boolean isExit = false;
    private Object lock = new Object();
    private final static int SCANNIN_GREQUEST_CODE = 11;

    //private DonkeyDao donkeyDao;
    private Toolbar toolbar;
    private ImageView sycimage;
    private MyAdapter mDataAdapter = null;
    private RecyclerView mRecyclerView;
    private AddItemHandler addItemHandler = null;
    //private PreviewHandler mHandler = new PreviewHandler(this);
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    private Animation animation;
    private ProgressDialog p_dialog;
    private SyncThread syncThread;
    //private UploadImageThread uploadImageThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle("");
        UploadImageThread.init(this);
        DaoUtils.init(this);

        EventBus.getDefault().register(this);
        login();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SettingUtils.isLogin())
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                else
                    startActivity(new Intent(MainActivity.this, ModifyPwdActivity.class));
            }
        });

        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.add_fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("isAdd", true);
                startActivity(intent);
            }
        });

        FloatingActionButton scanQRFab = (FloatingActionButton) findViewById(R.id.scan_qr_fab);
        scanQRFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QRCodeScanActivity.class));
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDataAdapter = new MyAdapter(this);
        lastIndex += mDataAdapter.getDatas(lastIndex + 1, GlobalData.COUNT_PER_PAGE);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        Header header = new Header(this);
        final ClearEditText searchEditText = (ClearEditText)header.findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH) || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if(searchEditText.getText().toString().isEmpty()){
                        Message msg = new Message();
                        msg.what = MainActivity.AddItemHandler.MSG_ADD_CLAER_SEARCH;
                        HandlerUtils.getAddItemHandler().sendMessage(msg);
                        return true;
                    }

                    if( HandlerUtils.isNumeric(searchEditText.getText().toString()) ) {
                        int searchNum = Integer.parseInt(searchEditText.getText().toString());
                        MainActivity.this.mDataAdapter.search(searchNum);
                    }else{
                        String farmer = searchEditText.getText().toString();
                        MainActivity.this.mDataAdapter.search(farmer);
                    }
                    return true;
                }

                return false;
            }
        });
        RecyclerViewUtils.setHeaderView(mRecyclerView, header);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        sycimage = (ImageView)findViewById(R.id.syncimage);
        sycimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //synchronized (lock){lock.notify();}
                syncThread.startSync();
                UploadImageThread.getInstance().startSync();
            }
        });

        initImageLoader(this);
        addItemHandler = new AddItemHandler();
        HandlerUtils.setAddItemHandler(addItemHandler);

        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_around_center_point);

        syncThread = new SyncThread();
        syncThread.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (SettingUtils.canAutoSync(MainActivity.this)) {
                    MainActivity.this.syncThread.startSync();
                    UploadImageThread.getInstance().startSync();
                };
            }
        }, 5000, 120 * 1000) ;
    }

    private void startAnimation(){
        sycimage.startAnimation(animation);
    }

    private void stopAnimation(){
        sycimage.clearAnimation();
    }

    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

    void login() {
        UserInfo userInfo = new UserInfo();
        SettingUtils.getUserInfo(MainActivity.this, userInfo);
        if(userInfo.getUsername().compareTo("") == 0) {
            toolbar.setNavigationIcon(R.drawable.head_image_unlogin);
            return;
        }

        String url = SettingUtils.makeServerAddress(MainActivity.this, "login-rest");
        p_dialog = ProgressDialog.show(MainActivity.this,
                "请等待",
                "正在为您登录...",
                true);

        OkHttpUtils
            .post()
            .url(url)
            .addParams("name", userInfo.getUsername())
            .addParams("pwd", userInfo.getPwd())
            .build()
            .execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    p_dialog.cancel();
                    SettingUtils.setIsLogin(false);
                    toolbar.setNavigationIcon(R.drawable.head_image_unlogin);
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {
                    p_dialog.cancel();
                    OperaResult operaResult = JSON.parseObject(response, OperaResult.class);
                    EventBus.getDefault().post( new LoginResultMsgEvent(operaResult.isSuccess()) );
                }
            });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity( new Intent(this, SettingActivity.class) );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void notifyDataSetChanged() {
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItem(int num) {
        if (mDataAdapter.getItemCount() < GlobalData.COUNT_PER_PAGE) {
            mDataAdapter.addItem(Integer.toString(num));
            lastIndex++;
        }
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if(state == LoadingFooter.State.Loading) {
                //Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            long totalCount = mDataAdapter.getTotalCount(); //.getItemCount(); //
            if (lastIndex < totalCount-1) {
                RecyclerViewStateUtils.setFooterViewState(MainActivity.this, mRecyclerView, GlobalData.COUNT_PER_PAGE, LoadingFooter.State.Loading, null);
                lastIndex += mDataAdapter.getDatas(lastIndex+1, GlobalData.COUNT_PER_PAGE);
                RecyclerViewStateUtils.setFooterViewState(MainActivity.this.mRecyclerView, LoadingFooter.State.Normal);
                //requestData();
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(MainActivity.this, mRecyclerView, GlobalData.COUNT_PER_PAGE, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(MainActivity.this, mRecyclerView, GlobalData.COUNT_PER_PAGE, LoadingFooter.State.Loading, null);
            //requestData();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBeginSyncMessageEvent(BeginSyncMessageEvent event){
        startAnimation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEndSyncMessageEvent(EndSyncMessageEvent event){
        stopAnimation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginResultMsgEvent(LoginResultMsgEvent event){
        if( !event.isSuccess() ){
            SettingUtils.setIsLogin(false);
            toolbar.setNavigationIcon(R.drawable.head_image_unlogin);
        } else {
            SettingUtils.setIsLogin(true);
            toolbar.setNavigationIcon(R.drawable.head_image_login);
            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            UploadImageThread.getInstance().start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutMsgEvent(LogoutMsgEvent event) {
        toolbar.setNavigationIcon(R.drawable.head_image_unlogin);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQRCodeScanResult(QRCodeScanResult event) {
        int sn = event.getSn();
        DonkeyDao donkeyDao = DaoUtils.getDonkeyDao();
        Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, sn);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        if (donkey == null) {
            intent.putExtra("isAdd", true);
        }

        intent.putExtra("num", Integer.toString(sn));
        startActivity(intent);
    }

    public class AddItemHandler extends Handler {
        public final static int MSG_ADD_ITEM = 1;
        public final static int MSG_ADD_CLAER_SEARCH = 2;
        public final static int MSG_START_SYNC = 3;
        public final static int MSG_STOP_SYNC = 4;
        public final static int MSG_UPDATE_UI = 5;
        public final static int MSG_DELETE_ITEM = 6;

        public AddItemHandler() {
        }

        public AddItemHandler(Looper L) {
            super(L);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ADD_ITEM:
                    Bundle bundle = msg.getData();
                    int num = bundle.getInt("num");
                    MainActivity.this.addItem(num);
                    break;
                case MSG_ADD_CLAER_SEARCH:
                    clear();
                    lastIndex += MainActivity.this.mDataAdapter.getDatas(lastIndex + 1, GlobalData.COUNT_PER_PAGE);//.search(searchNum);
                    break;
                case MSG_START_SYNC:
                    startAnimation();
                    break;
                case MSG_STOP_SYNC:
                    stopAnimation();
                    break;
                case MSG_UPDATE_UI:
                    notifyDataSetChanged();
                    break;
                case MSG_DELETE_ITEM:
                    updateUIForDelete();
                    break;
            }

        }
    }

    private void updateUIForDelete(){
        for (Integer sn:GlobalData.deletedOnServerSnList) {
            mDataAdapter.delItem(Integer.toString(sn));
        }

        notifyDataSetChanged();
    }

    private void clear(){
        //searchNum = -1;
        lastIndex = -1;
        MainActivity.this.mDataAdapter.clear();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        isExit = true;
        OkHttpUtils.getInstance().cancelTag(this);//取消以Activity.this作为tag的请求
        EventBus.getDefault().unregister(this);
    }

    public class SyncThread extends Thread {
        public Handler downloadHandler = null;

        public void startSync(){
            if (downloadHandler != null)
                downloadHandler.sendEmptyMessage(0);
        }
        @Override
        public void run() {
            Looper.prepare();

            downloadHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {

                    EventBus.getDefault().post( new BeginSyncMessageEvent() );
                    DonkeyDao donkeyDao = DaoUtils.getDonkeyDao();

                    if (NetworkUtils.downloadIdList(MainActivity.this, donkeyDao)) {
                        if (NetworkUtils.downloadDonkeys(MainActivity.this, donkeyDao)) {
                            if(SettingUtils.isLogin())
                                NetworkUtils.uploadDonkeys(MainActivity.this, donkeyDao);
                        }
                    }

                    EventBus.getDefault().post( new EndSyncMessageEvent() );
                }
            };

            Looper.loop();
        }
    }
}
