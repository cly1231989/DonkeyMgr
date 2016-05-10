package taiji.org.donkeymgr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;
import taiji.org.donkeymgr.utils.AddImageUtils;
import taiji.org.donkeymgr.utils.DaoUtils;
import taiji.org.donkeymgr.utils.FileUtils;
import taiji.org.donkeymgr.utils.SettingUtils;

/**
 * Created by hose on 2016/3/19.
} */

public class MyAdapter extends RecyclerView.Adapter {

    private ArrayList<String> stringArrayList;
    private Context context;

    private int selectedItemNum;
    private DonkeyDao donkeyDao;

    public MyAdapter(Context context) {
        this.context = context;
        stringArrayList = new ArrayList<>();
        donkeyDao = DaoUtils.getDonkeyDao(context);

        //getDatas(0, GlobalData.COUNT_PER_PAGE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //获取背景菜单
        View mybg = LayoutInflater.from(parent.getContext()).inflate(R.layout.bg_menu, null);
        mybg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //获取item布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //生成返回RecyclerView.ViewHolder
        return new MyHolder(context, mybg, view, RecyclerViewDragHolder.EDGE_RIGHT).getDragViewHolder();
    }

    class MyHolder extends RecyclerViewDragHolder {

        private TextView donkeynum;
        private ImageView notsync;
        private TextView deleteItem;
        //private TextView openBlog;
        private TextView uploadImage;

        public MyHolder(Context context, View bgView, View topView) {
            super(context, bgView, topView);
        }

        public MyHolder(Context context, View bgView, View topView, int mTrackingEdges) {
            super(context, bgView, topView, mTrackingEdges);
        }

        @Override
        public void initView(View itemView) {
            donkeynum = (TextView) itemView.findViewById(R.id.donkeynum);
            deleteItem = (TextView) itemView.findViewById(R.id.delete);
            uploadImage = (TextView) itemView.findViewById(R.id.upload_image);
            notsync = (ImageView) itemView.findViewById(R.id.notsync);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) RecyclerViewDragHolder.getHolder(holder);

        if ( position >= stringArrayList.size() ){
            myHolder.donkeynum.setText("");
            myHolder.notsync.setVisibility(View.INVISIBLE);
        }
        else {
            String data = stringArrayList.get(position);
            myHolder.donkeynum.setText(data);

            int num = Integer.parseInt(data);
            Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, num);
            if( donkey.hasSync() )
                myHolder.notsync.setVisibility(View.GONE);
            else
                myHolder.notsync.setVisibility(View.VISIBLE);

            myHolder.topView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myHolder.isOpen()) {
                        myHolder.close();
                        return;
                    }

                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("num", stringArrayList.get(position));

                    context.startActivity(intent);
                }
            });
            myHolder.topView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        if (myHolder.isOpen()) {
                            myHolder.close();
                            return;
                        }
                    }
                }
            });

            myHolder.notsync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                Uri uri = Uri.parse("https://blog.smemo.info");
                    //                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    //                context.startActivity(it);
                }
            });
            myHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("确认删除吗？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedItemNum = Integer.parseInt(stringArrayList.get(position));
                            Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, selectedItemNum);
                            donkey.setDeleteflag(true);
                            donkeyDao.update(donkey);

                            stringArrayList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            myHolder.uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemNum = Integer.parseInt(stringArrayList.get(position));

                    if (!SettingUtils.isOnline()) {
                        AddImageUtils addImageUtils = new AddImageUtils(context, ((MainActivity) context).getSupportFragmentManager(), selectedItemNum, donkeyDao, mOnHanlderResultCallback);
                        addImageUtils.showAddImageBtn();
                    } else {
                        Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, selectedItemNum);
                        if ( !donkey.hasSync() ){
                            Toast.makeText(context, "请同步后再编辑图片", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(context, EditImagesActivity.class);
                            intent.putExtra("selectedItemNum", selectedItemNum);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                String path = FileUtils.getImageDirPath(context, selectedItemNum);

                for (PhotoInfo photo: resultList) {
                    String imagePath = FileUtils.getImageName(path);
                    FileUtils.copyFile(photo.getPhotoPath(), imagePath);
                }

                Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, selectedItemNum);
                donkey.setVersion(donkey.getVersion() + 1);
                donkeyDao.update(donkey);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public long getTotalCount(){
        return donkeyDao.queryBuilder().where(DonkeyDao.Properties.Deleteflag.notEq(true)).count();
    }

    public int getDatas(int beginIndex, int count){
        long totalCount = getTotalCount();
        if (beginIndex >= totalCount)
            return 0;

        List<Donkey> donkeys = donkeyDao.queryBuilder().where(DonkeyDao.Properties.Deleteflag.notEq(true)).offset(beginIndex).limit(count).list();
        for (Donkey donkey: donkeys) {
            stringArrayList.add(Integer.toString(donkey.getSn()));
        }

        notifyDataSetChanged();
        return donkeys.size();
    }

    public void addItem(String sn){
        stringArrayList.add(sn);
        notifyDataSetChanged();
    }

    public void delItem(String sn){
        stringArrayList.remove(sn);
    }

    public void search(int searchNum){
        stringArrayList.clear();
        Donkey donkey = DaoUtils.getDonkeyBySn(donkeyDao, searchNum);
        if (donkey != null )
            stringArrayList.add(Integer.toString(donkey.getSn()));

        notifyDataSetChanged();
    }

    public void clear(){
        stringArrayList.clear();
    }
}