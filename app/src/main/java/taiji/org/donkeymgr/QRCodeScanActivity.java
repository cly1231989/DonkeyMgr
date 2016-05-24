package taiji.org.donkeymgr;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

import org.greenrobot.eventbus.EventBus;

import taiji.org.donkeymgr.msgs.QRCodeScanResult;
import taiji.org.donkeymgr.utils.HandlerUtils;

public class QRCodeScanActivity extends ToolBarActivity implements OnQRCodeReadListener {
    private boolean first = true;
    private QRCodeReaderView qrCodeReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);

        qrCodeReader = (QRCodeReaderView) findViewById(R.id.qrcode_reader_view);
        qrCodeReader.setOnQRCodeReadListener(this);
        ImageView line_image = (ImageView) findViewById(R.id.qrcode_reader_green_line);
        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.85f);
        mAnimation.setDuration(2000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        line_image.setAnimation(mAnimation);
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        //myTextView.setText(text);

        Integer sn = 0;
        try {
            sn = Integer.parseInt(text.substring(text.lastIndexOf("/") + 1));
        }catch (NumberFormatException e){
            return;
        }

        if (!first)
            return;

        first = false;
        EventBus.getDefault().post(new QRCodeScanResult(sn));
        finish();
    }


    // Called when your device have no camera
    @Override
    public void cameraNotFound() {
        Toast.makeText(this, "没有检查到相机，请检查相机是否正常", Toast.LENGTH_SHORT).show();
        finish();;
    }

    // Called when there's no QR codes in the camera preview image
    @Override
    public void QRCodeNotFoundOnCamImage() {
        //Toast.makeText(this, "没有扫描到二维码，请重新尝试", Toast.LENGTH_SHORT).show();
    }
}
