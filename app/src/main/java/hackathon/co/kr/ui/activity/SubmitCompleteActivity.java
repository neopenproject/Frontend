package hackathon.co.kr.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import hackathon.co.kr.neopen.R;
import hackathon.co.kr.util.DateUtils;

public class SubmitCompleteActivity extends AppCompatActivity {

    private TextView tvTitle;

    private TextView tvSubTitle;

    private TextView tvConsumeTimer;

    private ImageView userQuizeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complete);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        tvTitle = findViewById(R.id.tv_title);
        tvSubTitle = findViewById(R.id.tv_sub_title);
        tvConsumeTimer = findViewById(R.id.tv_consume_timer);
        userQuizeImage = findViewById(R.id.user_quiz_image);

        String title = getIntent().getStringExtra("TITLE");
        String sub_title = getIntent().getStringExtra("SUB_TITLE");
        String using_timer = getIntent().getStringExtra("USING_TIMER");
        String image_url = getIntent().getStringExtra("IMAGE_URL");

        if (title != null) {
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        else {
            tvTitle.setVisibility(View.GONE);
        }

        if (sub_title != null) {
            tvSubTitle.setText(sub_title);
            tvSubTitle.setVisibility(View.VISIBLE);
        }
        else {
            tvSubTitle.setVisibility(View.GONE);
        }

        DateUtils.updateCountDownText2(tvConsumeTimer,  Long.parseLong(using_timer));

        Glide.with(getBaseContext()).load(image_url).into(userQuizeImage);

        findViewById(R.id.ll_comback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
