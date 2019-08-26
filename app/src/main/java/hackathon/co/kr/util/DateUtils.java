package hackathon.co.kr.util;

import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static void updateCountDownText(TextView textView, long timeMillis) {
        int minutes = (int) ((timeMillis % 3600) / 60);
        int seconds = (int) timeMillis % 60;
        textView.setText(String.format(Locale.KOREA, "%02d:%02d", minutes, seconds));
    }

    public static void updateCountDownText2(TextView textView, long timeMillis) {
        int minutes = (int) ((timeMillis % 3600) / 60);
        int seconds = (int) timeMillis % 60;
        textView.setText(String.format(Locale.KOREA, "소요시간 : %02d:%02d", minutes, seconds));
    }
}
