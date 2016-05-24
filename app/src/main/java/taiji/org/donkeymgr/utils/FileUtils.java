package taiji.org.donkeymgr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;
import taiji.org.donkeymgr.dao.UploadImageInfo;

/**
 * Created by hose on 2016/4/3.
 */
public class FileUtils {

    public static String getImageDirPath(Context context, int sn){
        String path = context.getExternalFilesDir(null).getAbsolutePath() + "/" + sn + "/";
        File file = new File(path);
        if(!file.exists())
            file.mkdir();

        return path;
    }

    public static String getThumbImageDirPath(Context context, int sn){
        String path = context.getExternalFilesDir(null).getAbsolutePath() + "/" + sn + "_thumb/";
        File file = new File(path);
        if(!file.exists())
            file.mkdir();

        return path;
    }

    public static String getImageName(String path){
        int index = 1;
        while (true){
            File file = new File(path + index + ".jpg");
            if ( !file.exists() ){
                return file.getAbsolutePath();
            }

            index++;
        }
    }

    public static void deleteDir(String dirName){
        File dir = new File(dirName);
        File[] files = dir.listFiles();
        if(files != null && files.length != 0){
            for(File file:files){
                file.delete();
            }
        }

        dir.delete();
    }

    public static void deleteDirBySn(Context context, int sn){
        deleteDir(getImageDirPath(context, sn));
        deleteDir(getThumbImageDirPath(context, sn));
    }

    public static void deleteDirById(Context context, DonkeyDao donkeyDao, Long id){
        Donkey donkey = DaoUtils.getDeletedDonkeyByIdOnServer(donkeyDao, id);
        deleteDirBySn(context, donkey.getSn());
    }

    public static void makeUploadImageInfos(Context context, List<PhotoInfo> resultList, String mageDirPath, Donkey donkey){
        for (PhotoInfo photo: resultList) {
            String imagePath = FileUtils.getImageName(mageDirPath);
            FileUtils.copyFile(photo.getPhotoPath(), imagePath);
            File image = new File(imagePath);
            if(!image.exists()){
                Toast.makeText(context, "copy file failed", Toast.LENGTH_SHORT).show();
                continue;
            }

            String uploadImageUrl = SettingUtils.makeServerAddress(context, "donkey/images/upload");
            UploadImageInfo uploadImageInfo = new UploadImageInfo(donkey.getId(), donkey.getIdonserver(), imagePath , uploadImageUrl);
            DaoUtils.getUploadImageDao().insert(uploadImageInfo);
        }
        UploadImageThread.getInstance().startSync();
    }

    public static boolean copyFile(String oldPath, String newPath) {
        File sourceFile = new File(oldPath);
        File destFile = new File(newPath);

        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(sourceFile);
            outStream = new FileOutputStream(destFile);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inStream != null)
                    inStream.close();

                if(in != null)
                    in.close();

                if(outStream != null)
                    outStream.close();

                if(out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private static Bitmap bitmap = null;

    public static boolean makeThumbImage(String srcImageFile, String thumbImageFile){
        File srcImage = new File(srcImageFile);

        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(srcImageFile, newOpts);

        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;

        newOpts.inSampleSize = 1;
        if(height >= width){
            newOpts.inSampleSize = height / 150;
        }else
        {
            newOpts.inSampleSize = width  / 150;
        }

        bitmap = BitmapFactory.decodeFile(srcImageFile, newOpts);

        File thumbImage = new File(thumbImageFile);
        FileOutputStream fOut = null;
        try {
            thumbImage.createNewFile();
            fOut = new FileOutputStream(thumbImage);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
                System.gc();
            }

        }

        //Bitmap thumb = Bitmap.createBitmap(bitmap, 0, 0, width1, height1);

        return  true;
    }

    public static boolean makeThumbImages(String srcDir, String destDir){
        File dir = new File(srcDir);
        File[] images = dir.listFiles();
        if(images == null || images.length == 0)
            return true;

        for (File image:images) {
            if(bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle() ;
                bitmap=null;
                System.gc();
            }

            bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            double ratio = 1.0;
            int height1 = 0;
            int width1 = 0;
            if(height >= width){
                height1 = 150;
                ratio = height * 1.0 / 150;
                width1 = (int) (width * 1.0 / ratio);
            }else
            {
                width1 = 150;
                ratio = width * 1.0 / 150;
                height1 = (int) (height * 1.0 / ratio);
            }

            File thumbImage = new File(destDir + image.getName());
            FileOutputStream fOut = null;
            try {
                thumbImage.createNewFile();
                fOut = new FileOutputStream(thumbImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Bitmap thumb = Bitmap.createBitmap(bitmap, 0, 0, width1, height1);
        }
        return  true;
    }
}
