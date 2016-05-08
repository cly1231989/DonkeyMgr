package taiji.org.donkeymgr.utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.baoyz.actionsheet.ActionSheet;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import taiji.org.donkeymgr.UILImageLoader;
import taiji.org.donkeymgr.UILPauseOnScrollListener;
import taiji.org.donkeymgr.dao.DonkeyDao;

/**
 * Created by hose on 2016/4/3.
 */
public class AddImageUtils {

    private int selectedItemNum;
    private Context context;
    private DonkeyDao donkeyDao;
    private FragmentManager fragmentManager;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;

    private GalleryFinal.OnHanlderResultCallback onHanlderResultCallback;

    public AddImageUtils(Context context, FragmentManager fragmentManager, int selectedItemNum, DonkeyDao donkeyDao, GalleryFinal.OnHanlderResultCallback onHanlderResultCallback) {
        this.context = context;
        this.selectedItemNum = selectedItemNum;
        this.donkeyDao = donkeyDao;
        this.fragmentManager = fragmentManager;
        this.onHanlderResultCallback = onHanlderResultCallback;
    }

    public void showAddImageBtn(){
        cn.finalteam.galleryfinal.ImageLoader imageLoader = new UILImageLoader();
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setMutiSelectMaxSize(20);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setEnablePreview(true);

        final FunctionConfig functionConfig = functionConfigBuilder.build();
        PauseOnScrollListener pauseOnScrollListener = new UILPauseOnScrollListener(false, true);

        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);

        ActionSheet.createBuilder(context, fragmentManager)
                .setCancelButtonTitle("取消(Cancel)")
                .setOtherButtonTitles("打开相册(Open Gallery)", "拍照(Camera)")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, onHanlderResultCallback);
                                break;
                            case 1:
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, onHanlderResultCallback);
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }
}
