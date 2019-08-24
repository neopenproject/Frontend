package hackathon.co.kr.neopen.temp;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import hackathon.co.kr.neopen.DefaultFunctionKt;
import hackathon.co.kr.neopen.R;
import hackathon.co.kr.neopen.sdk.ink.structure.Dot;
import hackathon.co.kr.neopen.sdk.ink.structure.Stroke;
import hackathon.co.kr.neopen.sdk.metadata.MetadataCtrl;
import hackathon.co.kr.neopen.sdk.metadata.structure.Symbol;
import hackathon.co.kr.neopen.sdk.pen.bluetooth.BLENotSupportedException;
import hackathon.co.kr.neopen.sdk.pen.bluetooth.lib.ProtocolNotSupportedException;
import hackathon.co.kr.neopen.sdk.pen.offline.OfflineFileParser;
import hackathon.co.kr.neopen.sdk.pen.penmsg.JsonTag;
import hackathon.co.kr.neopen.sdk.pen.penmsg.PenMsgType;
import hackathon.co.kr.neopen.sdk.util.NLog;
import hackathon.co.kr.neopen.temp.provider.DbOpenHelper;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PenActivity extends AppCompatActivity
//		implements DrawablePage.DrawablePageListener, DrawableView.DrawableViewGestureListener
{
    public static final String TAG = "pensdk.sample";

    public static final int REQ_GPS_EXTERNAL_PERMISSION = 0x1002;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 4;

    private PenClientCtrl penClientCtrl;
    private MultiPenClientCtrl multiPenClientCtrl;

    private SampleView mSampleView;

    // Notification
    protected Notification.Builder mBuilder;
    protected NotificationManager mNotifyManager;
    protected Notification mNoti;

    public InputPasswordDialog inputPassDialog;

    private FwUpdateDialog fwUpdateDialog;

    private int currentSectionId = -1;
    private int currentOwnerId = -1;
    private int currentBookcodeId = -1;
    private int currentPagenumber = -1;
    private int connectionMode = 0;

    private ArrayList<String> connectedList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pen);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }

        mSampleView = new SampleView(this);
        FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ((FrameLayout) findViewById(R.id.sampleview_frame)).addView(mSampleView, 0, para);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("firmware_update"), PendingIntent.FLAG_UPDATE_CURRENT);

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(getApplicationContext());
        mBuilder.setContentTitle("Update Pen");
        mBuilder.setSmallIcon(R.drawable.ic_launcher_n);
        mBuilder.setContentIntent(pendingIntent);


        chkPermissions();
        Intent oIntent = new Intent();
        oIntent.setClass(this, NeoSampleService.class);
        startService(oIntent);

        penClientSetting();

        findViewById(R.id.iv_ble_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penClientCtrl.isConnected()) {
                    penClientCtrl.disconnect();
                }
                penClientSetting();
            }
        });

    }

    private void penClientSetting() {
        penClientCtrl = PenClientCtrl.getInstance(getApplicationContext());
        fwUpdateDialog = new FwUpdateDialog(PenActivity.this, penClientCtrl, mNotifyManager, mBuilder);
        penClientCtrl.connect("9C:7B:D2:05:6E:3F", null);
    }

    private void chkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int gpsPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            final int writeExternalPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (gpsPermissionCheck == PackageManager.PERMISSION_DENIED || writeExternalPermissionCheck == PackageManager.PERMISSION_DENIED) {
                ArrayList<String> permissions = new ArrayList<String>();
                if (gpsPermissionCheck == PackageManager.PERMISSION_DENIED)
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                if (writeExternalPermissionCheck == PackageManager.PERMISSION_DENIED)
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQ_GPS_EXTERNAL_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQ_GPS_EXTERNAL_PERMISSION) {
            boolean bGrantedExternal = false;
            boolean bGrantedGPS = false;
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    bGrantedExternal = true;
                } else if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    bGrantedGPS = true;
                }
            }

            if ((permissions.length == 1) && (bGrantedExternal || bGrantedGPS)) {
                bGrantedExternal = true;
                bGrantedGPS = true;
            }

            if (!bGrantedExternal || !bGrantedGPS) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission Check");
                builder.setMessage("PERMISSION_DENIED");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mBroadcastReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(Const.Broadcast.ACTION_PEN_MESSAGE);
        filter.addAction(Const.Broadcast.ACTION_PEN_DOT);
        filter.addAction(Const.Broadcast.ACTION_OFFLINE_STROKES);
        filter.addAction(Const.Broadcast.ACTION_WRITE_PAGE_CHANGED);
        filter.addAction("firmware_update");

        registerReceiver(mBroadcastReceiver, filter);


    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//        unregisterReceiver( mBTDuplicateRemoveBroadcasterReceiver );
        Intent oIntent = new Intent();
        oIntent.setClass(this, NeoSampleService.class);
        stopService(oIntent);

        if (penClientCtrl != null)
            penClientCtrl.disconnect();
        if (multiPenClientCtrl != null) {
            ArrayList<String> penAddressList = multiPenClientCtrl.getConnectDevice();
            for (String address : penAddressList)
                multiPenClientCtrl.disconnect(address);
        }

    }


    private void handleDot(String penAddress, Dot dot) {
        NLog.d("penAddress=" + penAddress + ",handleDot type =" + dot.dotType);
        mSampleView.addDot(penAddress, dot);
    }

    private void handleMsg(String penAddress, int penMsgType, String content) {
        Log.d(TAG, "penAddress=" + penAddress + ",handleMsg : " + penMsgType);

        switch (penMsgType) {
            // Message of the attempt to connect a pen
            case PenMsgType.PEN_CONNECTION_TRY:

                Util.showToast(this, "try to connect.");

                break;

            // Pens when the connection is completed (state certification process is not yet in progress)
            case PenMsgType.PEN_CONNECTION_SUCCESS:

                Util.showToast(this, "connection is successful.");
                break;


            case PenMsgType.PEN_AUTHORIZED:
                // OffLine Data set use
                if (connectionMode == 0)
                    penClientCtrl.setAllowOfflineData(true);
                else
                    multiPenClientCtrl.setAllowOfflineData(penAddress, true);
                Util.showToast(this, "connection is AUTHORIZED.");
                break;
            // Message when a connection attempt is unsuccessful pen
            case PenMsgType.PEN_CONNECTION_FAILURE:

                Util.showToast(this, "connection has failed.");

                break;


            case PenMsgType.PEN_CONNECTION_FAILURE_BTDUPLICATE:
                String connected_Appname = "";
                try {
                    JSONObject job = new JSONObject(content);

                    connected_Appname = job.getString("packageName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Util.showToast(this, String.format("The pen is currently connected to %s app. If you want to proceed, please disconnect the pen from %s app.", connected_Appname, connected_Appname));
                break;

            // When you are connected and disconnected from the state pen
            case PenMsgType.PEN_DISCONNECTED:

                Util.showToast(this, "connection has been terminated.");
                // Pen transmits the state when the firmware update is processed.
            case PenMsgType.PEN_FW_UPGRADE_STATUS:
            case PenMsgType.PEN_FW_UPGRADE_SUCCESS:
            case PenMsgType.PEN_FW_UPGRADE_FAILURE:
            case PenMsgType.PEN_FW_UPGRADE_SUSPEND: {
                if (fwUpdateDialog != null)
                    fwUpdateDialog.setMsg(penAddress, penMsgType, content);
            }
            break;


            // Offline Data List response of the pen
            case PenMsgType.OFFLINE_DATA_NOTE_LIST:

                try {
                    JSONArray list = new JSONArray(content);

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject jobj = list.getJSONObject(i);

                        int sectionId = jobj.getInt(JsonTag.INT_SECTION_ID);
                        int ownerId = jobj.getInt(JsonTag.INT_OWNER_ID);
                        int noteId = jobj.getInt(JsonTag.INT_NOTE_ID);
                        NLog.d(TAG, "offline(" + (i + 1) + ") note => sectionId : " + sectionId + ", ownerId : " + ownerId + ", noteId : " + noteId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // if you want to get offline data of pen, use this function.
                // you can call this function, after complete download.
                //
                break;

            // Messages for offline data transfer begins
            case PenMsgType.OFFLINE_DATA_SEND_START:

                break;

            // Offline data transfer completion
            case PenMsgType.OFFLINE_DATA_SEND_SUCCESS:
                if (connectionMode == 0) {
                    if (penClientCtrl.getProtocolVersion() == 1)
                        parseOfflineData(penAddress);
                } else {
                    if (multiPenClientCtrl.getProtocolVersion(penAddress) == 1)
                        parseOfflineData(penAddress);
                }

                break;

            // Offline data transfer failure
            case PenMsgType.OFFLINE_DATA_SEND_FAILURE:

                break;

            // Progress of the data transfer process offline
            // 오프라인 데이타를 전송 받을 때, 얼만큼 받았는지 확인 가능
            case PenMsgType.OFFLINE_DATA_SEND_STATUS: {
                try {
                    JSONObject job = new JSONObject(content);

                    int total = job.getInt(JsonTag.INT_TOTAL_SIZE);
                    int received = job.getInt(JsonTag.INT_RECEIVED_SIZE);

                    Log.d(TAG, "offline data send status => total : " + total + ", progress : " + received);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            // When the file transfer process of the download offline
            case PenMsgType.OFFLINE_DATA_FILE_CREATED: {
                try {
                    JSONObject job = new JSONObject(content);

                    int sectionId = job.getInt(JsonTag.INT_SECTION_ID);
                    int ownerId = job.getInt(JsonTag.INT_OWNER_ID);
                    int noteId = job.getInt(JsonTag.INT_NOTE_ID);
                    int pageId = job.getInt(JsonTag.INT_PAGE_ID);

                    String filePath = job.getString(JsonTag.STRING_FILE_PATH);

                    Log.d(TAG, "offline data file created => sectionId : " + sectionId + ", ownerId : " + ownerId + ", noteId : " + noteId + ", pageId : " + pageId + " filePath : " + filePath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            // Ask for your password in a message comes when the pen
            case PenMsgType.PASSWORD_REQUEST: {
                int retryCount = -1, resetCount = -1;

                try {
                    JSONObject job = new JSONObject(content);

                    retryCount = job.getInt(JsonTag.INT_PASSWORD_RETRY_COUNT);
                    resetCount = job.getInt(JsonTag.INT_PASSWORD_RESET_COUNT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (inputPassDialog == null)
                    inputPassDialog = new InputPasswordDialog(this, this);
                inputPassDialog.show(penAddress);
            }
            break;
            case PenMsgType.PEN_ILLEGAL_PASSWORD_0000: {
                if (inputPassDialog == null)
                    inputPassDialog = new InputPasswordDialog(this, this);
                inputPassDialog.show(penAddress);
            }
            break;

        }
    }

    public void inputPassword(String penAddress, String password) {
        if (connectionMode == 0) {
            penClientCtrl.inputPassword(password);
        } else {
            multiPenClientCtrl.inputPassword(penAddress, password);
        }
    }

    private void parseOfflineData(String penAddress) {
        // obtain saved offline data file list
        String[] files = OfflineFileParser.getOfflineFiles(penAddress);

        if (files == null || files.length == 0) {
            return;
        }

        for (String file : files) {
            try {
                // create offline file parser instance
                OfflineFileParser parser = new OfflineFileParser(file);

                // parser return array of strokes
                Stroke[] strokes = parser.parse();

                if (strokes != null) {
                    // check offline symbol
//					ArrayList<Stroke> strokeList = new ArrayList( Arrays.asList( strokes ));
                    mSampleView.addStrokes(penAddress, strokes);
                }

                // delete data file
                parser.delete();
                parser = null;
            } catch (Exception e) {
                Log.e(TAG, "parse file exeption occured.", e);
            }
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Const.Broadcast.ACTION_PEN_MESSAGE.equals(action)) {
                String penAddress = intent.getStringExtra(Const.Broadcast.PEN_ADDRESS);
                int penMsgType = intent.getIntExtra(Const.Broadcast.MESSAGE_TYPE, 0);
                String content = intent.getStringExtra(Const.Broadcast.CONTENT);

                handleMsg(penAddress, penMsgType, content);
            } else if (Const.Broadcast.ACTION_PEN_DOT.equals(action)) {
                String penAddress = intent.getStringExtra(Const.Broadcast.PEN_ADDRESS);
                Dot dot = intent.getParcelableExtra(Const.Broadcast.EXTRA_DOT);
                dot.color = Color.BLACK;
                handleDot(penAddress, dot);
            } else if (Const.Broadcast.ACTION_OFFLINE_STROKES.equals(action)) {
                String penAddress = intent.getStringExtra(Const.Broadcast.PEN_ADDRESS);
                Parcelable[] array = intent.getParcelableArrayExtra(Const.Broadcast.EXTRA_OFFLINE_STROKES);
                int sectionId = intent.getIntExtra(Const.Broadcast.EXTRA_SECTION_ID, -1);
                int ownerId = intent.getIntExtra(Const.Broadcast.EXTRA_OWNER_ID, -1);
                int noteId = intent.getIntExtra(Const.Broadcast.EXTRA_BOOKCODE_ID, -1);

                if (array != null) {
                    Stroke[] strokes = new Stroke[array.length];
                    for (int i = 0; i < array.length; i++) {
                        strokes[i] = ((Stroke) array[i]);
                    }
                    mSampleView.addStrokes(penAddress, strokes);
                }

                // DB에 저장 후, 오프라인 데이터를 삭제합니다.
                // 오프라인 데이터 요청 시, deleteOnFinished 를 true 로 요청했었다면, 아래의 과정은 필요없습니다.
                // (오프라인 데이터 요청은 PenClientCtrl, MutiPenClientCtrl 에서 확인할 수 있습니다)
                if (sectionId != -1 && ownerId != -1 && noteId != -1)
                    deleteOfflineData(penAddress, sectionId, ownerId, noteId);
            } else if (Const.Broadcast.ACTION_WRITE_PAGE_CHANGED.equals(action)) {
                int sectionId = intent.getIntExtra(Const.Broadcast.EXTRA_SECTION_ID, -1);
                int ownerId = intent.getIntExtra(Const.Broadcast.EXTRA_OWNER_ID, -1);
                int noteId = intent.getIntExtra(Const.Broadcast.EXTRA_BOOKCODE_ID, -1);
                int pageNum = intent.getIntExtra(Const.Broadcast.EXTRA_PAGE_NUMBER, -1);
                currentSectionId = sectionId;
                currentOwnerId = ownerId;
                currentBookcodeId = noteId;
                currentPagenumber = pageNum;
                mSampleView.changePage(sectionId, ownerId, noteId, pageNum);
            }
        }
    };

    private void deleteOfflineData(String address, int section, int owner, int note) {
        int[] noteArray = {note};
        if (connectionMode == 0) {
            try {
                penClientCtrl.removeOfflineData(section, owner, noteArray);
            } catch (ProtocolNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                multiPenClientCtrl.removeOfflineData(address, section, owner, noteArray);
            } catch (ProtocolNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getExternalStoragePath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return Environment.MEDIA_UNMOUNTED;
        }
    }
}
