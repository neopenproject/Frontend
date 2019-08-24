package hackathon.co.kr.neopen;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import hackathon.co.kr.neopen.sdk.pen.IPenCtrl;
import hackathon.co.kr.neopen.sdk.pen.PenCtrl;
import hackathon.co.kr.neopen.sdk.pen.bluetooth.BLENotSupportedException;
import hackathon.co.kr.neopen.sdk.pen.penmsg.IPenMsgListener;
import hackathon.co.kr.neopen.sdk.pen.penmsg.PenMsg;
import hackathon.co.kr.neopen.temp.DeviceListActivity;
import hackathon.co.kr.neopen.temp.SampleView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
