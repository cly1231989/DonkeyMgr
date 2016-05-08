package taiji.org.donkeymgr;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;
import taiji.org.donkeymgr.bean.ImageItemInfo;
import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;
import taiji.org.donkeymgr.utils.AddImageUtils;
import taiji.org.donkeymgr.utils.DaoUtils;
import taiji.org.donkeymgr.utils.FileUtils;
import taiji.org.donkeymgr.utils.HandlerUtils;
import taiji.org.donkeymgr.utils.NetworkUtils;
import taiji.org.donkeymgr.utils.SettingUtils;

public class EditImagesActivity extends ToolBarActivity {

    private int selectedItemNum;
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;

    private DonkeyDao donkeyDao;
    private ProgressDialog p_dialog;

    private UploadImagesHandler uploadImagesHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_images);

        selectedItemNum = getIntent().getIntExtra("selectedItemNum", 0);

        mRecyclerView = (RecyclerView) findViewById(R.id.imagelistrecyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        uploadImagesHandler = new UploadImagesHandler();
        HandlerUtils.setUploadImagesHandler(uploadImagesHandler);
        FileUtils.deleteDirBySn(this, selectedItemNum);

        donkeyDao = DaoUtils.getDonkeyDao(EditImagesActivity.this);
        downloadImages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.deleteDirBySn(EditImagesActivity.this, selectedItemNum);
    }

    boolean downloadImage(final String remoteSubPath, String imageUrl, String destDir, final String fileName){
        OkHttpUtils
            .get()
            .url(imageUrl)
            .build()
            .execute(new FileCallBack(destDir, fileName) {
                @Override
                public void inProgress(float progress, long total) {

                }

                @Override
                public void onError(Call call, Exception e) {
                    Toast.makeText(EditImagesActivity.this, "下载图片" + fileName + "失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(File response) {
                    mAdapter.addItem( response.getAbsolutePath(), remoteSubPath);
                }
            });
        return true;
    }

    void downloadImages() {
        String url = SettingUtils.makeServerAddress(EditImagesActivity.this, "donkey/images");
        p_dialog = ProgressDialog
                .show(EditImagesActivity.this,
                        "请稍候",
                        "正在下载图片...",
                        true);

        Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, selectedItemNum);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", Long.toString(donkey.getIdonserver()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        p_dialog.cancel();
                        SettingUtils.setIsOnline(false);
                        Toast.makeText(EditImagesActivity.this, "下载过程发生错误 " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        EditImagesActivity.this.finish();
                    }

                    @Override
                    public void onResponse(String response) {
                        //p_dialog.cancel();

                        ImagesInfo imagesInfo = JSON.parseObject(response, ImagesInfo.class);
                        for (String imagePath : imagesInfo.getImages()) {
                            JSONObject jsonObject = JSON.parseObject(imagePath);
                            String remoteSubPath = jsonObject.getString("url");

                            String imageDirPath = FileUtils.getThumbImageDirPath(EditImagesActivity.this, EditImagesActivity.this.selectedItemNum);
                            String imageName = FileUtils.getImageName(imageDirPath);
                            File imageFile = new File(imageName);
                            try {
                                imageFile.createNewFile();
                            } catch (IOException e) {
                                int a = 0;
                            }

                            downloadImage(remoteSubPath, SettingUtils.makeServerAddress(EditImagesActivity.this, remoteSubPath), imageDirPath, imageFile.getName());
                        }

                        p_dialog.cancel();
                    }
                });
    }

    public class UploadImagesHandler extends Handler {
        public final static int MSG_UPDATE_UI = 1;

        public UploadImagesHandler() {
        }

        public UploadImagesHandler(Looper L) {
            super(L);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_UI:
                    List<ImageItemInfo> itemsInfo = HandlerUtils.getItemsInfo();
                    for (ImageItemInfo itemInfo : itemsInfo) {
                        mAdapter.addItem(itemInfo.getLocalImagePath(), itemInfo.getRemoteImagePath());
                    }

                    mAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {
        private List<ImageItemInfo> mList = new LinkedList<>();

        public HomeAdapter() {
        }

        public void addItem(String localFilePath, String remoteFilePath){
            mList.add( new ImageItemInfo(localFilePath, remoteFilePath) );
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    EditImagesActivity.this).inflate(R.layout.image_item, parent,
                    false));
            return holder;
        }


        private class UploadImagesAsyncTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... params) {
                String path = FileUtils.getImageDirPath(EditImagesActivity.this, selectedItemNum);
                List<PhotoInfo> resultList = GlobalData.getResultList();
                for (PhotoInfo photo: resultList) {
                    String imagePath = FileUtils.getImageName(path);
                    FileUtils.copyFile(photo.getPhotoPath(), imagePath);
                }

                Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, selectedItemNum);
                List<ImageItemInfo> itemsInfo = NetworkUtils.uploadImages(EditImagesActivity.this, donkey.getIdonserver(), selectedItemNum, true);
                if(itemsInfo != null){
                    //GlobalData.setItemsInfo(itemsInfo);
                    HandlerUtils.notifyImageUploadFinished(itemsInfo);
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                p_dialog = ProgressDialog
                        .show(EditImagesActivity.this,
                                "请稍候",
                                "正在上传图片...",
                                true);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                p_dialog.cancel();
            }
        }

        private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null) {
                    UploadImagesAsyncTask asyncTask = new UploadImagesAsyncTask();
                    GlobalData.setResultList(resultList);

                    asyncTask.execute();
//                    String path = FileUtils.getImageDirPath(EditImagesActivity.this, selectedItemNum);
//                    p_dialog = ProgressDialog
//                            .show(EditImagesActivity.this,
//                                    "请稍候",
//                                    "正在上传图片...",
//                                    true);
//
//                    for (PhotoInfo photo: resultList) {
//                        String imagePath = FileUtils.getImageName(path);
//                        FileUtils.copyFile(photo.getPhotoPath(), imagePath);
//                    }
//
//                    Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, selectedItemNum);
//                    List<ImageItemInfo> itemsInfo = NetworkUtils.uploadImages(EditImagesActivity.this, donkey.getIdonserver(), selectedItemNum, false);
//                    if(itemsInfo == null){
//                        Toast.makeText(EditImagesActivity.this, "图片上传失败，请稍后重试", Toast.LENGTH_SHORT).show();
//                    }else {
//                        for (ImageItemInfo itemInfo : itemsInfo) {
//                            mList.add(itemInfo);
//                        }
//                    }
//
//                    p_dialog.cancel();
//                    notifyDataSetChanged();
//
//                    donkey.setVersion(donkey.getVersion() + 1);
//                    donkeyDao.update(donkey);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Toast.makeText(EditImagesActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };

        void deleteImage(final int pos){
            String remoteFilePath = mList.get(pos).getRemoteImagePath();
            String url = SettingUtils.makeServerAddress(EditImagesActivity.this, "donkey/images/delimage");
            p_dialog = ProgressDialog
                .show(EditImagesActivity.this,
                    "请稍候",
                    "正在删除图片...",
                    true);

            OkHttpUtils
                .post()
                .url(url)
                .addParams("img", remoteFilePath)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(EditImagesActivity.this, "删除失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Map<String, String> result = JSON.parseObject(response, new HashMap<String, String>().getClass());
                        if( result.get("result").compareToIgnoreCase("success") != 0 ) {
                            Toast.makeText(EditImagesActivity.this, "删除失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            File file = new File(mList.get(pos).getLocalImagePath());
                            file.delete();
                            mList.remove(pos);
                            notifyDataSetChanged();
                        }

                    }
                });

            p_dialog.cancel();

        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            if (position == mList.size()) {
                holder.iconImage.setImageResource(R.drawable.sns_add_item);
                holder.delImage.setVisibility(View.GONE);
            }  else {
                String image = mList.get(position).getLocalImagePath();
                BitmapFactory.Options opt = new BitmapFactory.Options();

                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bm = BitmapFactory.decodeFile(mList.get(position).getLocalImagePath());
                holder.iconImage.setImageBitmap(bm);
                holder.delImage.setVisibility(View.VISIBLE);

//                if(bm != null && !bm.isRecycled()) {
//                    bm.recycle();
//                    bm = null;
//                    System.gc();
//                }
            }

            holder.delImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    deleteImage(pos);
                }
            });

            holder.iconImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    if ( pos == mList.size() ) {
                        AddImageUtils addImageUtils = new AddImageUtils(EditImagesActivity.this, EditImagesActivity.this.getSupportFragmentManager(), selectedItemNum, donkeyDao, mOnHanlderResultCallback);
                        addImageUtils.showAddImageBtn();
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    if (pos == mList.size() + 1){
                        Toast.makeText(EditImagesActivity.this, "add", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            // 如果设置了回调，则设置点击事件
//            if (mOnItemClickLitener != null)
//            {
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int pos = holder.getLayoutPosition();
//                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
//                    }
//                });
//            }
        }

        @Override
        public int getItemCount(){
            return mList.size() + 1;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView iconImage;
            ImageView delImage;

            public MyViewHolder(View view){
                super(view);
                iconImage = (ImageView) view.findViewById(R.id.item_icon);
                delImage = (ImageView) view.findViewById(R.id.item_del);
            }
        }
    }
}
