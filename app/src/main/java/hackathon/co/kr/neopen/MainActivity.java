package hackathon.co.kr.neopen;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import hackathon.co.kr.neopen.sdk.pen.IPenCtrl;
import hackathon.co.kr.neopen.sdk.pen.PenCtrl;
import hackathon.co.kr.neopen.sdk.pen.penmsg.IPenMsgListener;
import hackathon.co.kr.neopen.sdk.pen.penmsg.PenMsg;

public class MainActivity extends AppCompatActivity implements IPenMsgListener {
    private IPenCtrl iPenCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Temp name = new Temp("name", 3);
        TextView textView = findViewById(R.id.textView);
        textView.setText(name.getName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), KotlinActivity.class));
                finish();
            }
        });

        // create pen controller instance
        iPenCtrl = PenCtrl.getInstance();

        // Start pen controller
        // iPenCtrl.startup();

        // register pen event callback listener
        iPenCtrl.setListener(this);

        // register pen ncode callback listener
        // iPenCtrl.setDotListener(this);
    }

    @Override
    public void onReceiveMessage(String penAddress, PenMsg penMsg) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
