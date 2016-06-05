package taiji.org.donkeymgr;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
import taiji.org.donkeymgr.utils.SettingUtils;

public class EditImagesActivity extends ToolBarActivity {

    private int selectedItemNum;
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;

    private DonkeyDao donkeyDao;
    private ProgressDialog p_dialog;

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

        donkeyDao = DaoUtils.getDonkeyDao();
        downloadImages();
    }

    boolean downloadImage(final String remoteSubPath, String imageUrl){

        ImageSize mImageSize = new ImageSize(100, 100);

        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().loadImage(imageUrl, mImageSize, options, new SimpleImageLoadingListener(){

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                mAdapter.updateItem ( remoteSubPath, loadedImage);
            }

        });

        return true;
    }

    void downloadImages() {
        String url = SettingUtils.makeServerAddress(EditImagesActivity.this, "donkey/images");
        Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, selectedItemNum);

        p_dialog = ProgressDialog
                .show(EditImagesActivity.this,
                        "请稍候",
                        "正在获取图片信息...",
                        true);

        OkHttpUtils
                .post()
                .url(url)
                .addParams("id", Long.toString(donkey.getIdonserver()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        p_dialog.cancel();
                        //SettingUtils.setIsLogin(false);
                        Toast.makeText(EditImagesActivity.this, "下载过程发生错误 " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        EditImagesActivity.this.finish();
                    }

                    @Override
                    public void onResponse(String response) {

                        ImagesInfo imagesInfo = JSON.parseObject(response, ImagesInfo.class);
                        for (String imagePath : imagesInfo.getImages()) {
                            JSONObject jsonObject = JSON.parseObject(imagePath);
                            String remoteSubPath = jsonObject.getString("url");

                            mAdapter.addItem(null, remoteSubPath);
                            downloadImage( remoteSubPath, SettingUtils.makeServerAddress(EditImagesActivity.this, remoteSubPath) );
                        }

                        p_dialog.cancel();
                    }
                });
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {
        private List<ImageItemInfo> mList = new LinkedList<>();

        public HomeAdapter() {
        }

        public void addItem(Bitmap image, String remoteFilePath){
            mList.add( new ImageItemInfo(image, remoteFilePath) );
            notifyItemInserted(mList.size() - 1);
        }

        public void updateItem(String remoteFilePath, Bitmap image){
            int i = 0;
            for(ImageItemInfo itemInfo:mList){
                if (itemInfo.getRemoteImagePath().compareTo(remoteFilePath) == 0){
                    itemInfo.setImage(image);
                    break;
                }

                i++;
            }

            this.notifyItemChanged(i);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    EditImagesActivity.this).inflate(R.layout.image_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            if (position == mList.size()) {
                holder.iconImage.setImageResource(R.drawable.sns_add_item);
                holder.delImage.setVisibility(View.GONE);
            }  else {
                holder.delImage.setVisibility(View.VISIBLE);
                Bitmap image = mList.get(position).getImage();
                if (image == null)
                    holder.iconImage.setImageResource(R.drawable.pictures_no);
                else
                    holder.iconImage.setImageBitmap(image);
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

        private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null) {
                    Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, selectedItemNum);
                    String path = FileUtils.getImageDirPath(EditImagesActivity.this, selectedItemNum);
                    FileUtils.makeUploadImageInfos(EditImagesActivity.this, resultList, path, donkey);
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
                            mList.remove(pos);
                            notifyItemRemoved(pos);
                        }

                    }
                });

            p_dialog.cancel();

        }
    }
}
