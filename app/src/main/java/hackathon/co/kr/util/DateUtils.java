package hackathon.co.kr.util;

import android.widget.TextView;

import java.util.Locale;

public class DateUtils {

    public static void updateCountDownText(TextView textView, long timeMillis) {
        int minutes = (int) (timeMillis / 1000) / 60;
        int seconds = (int) (timeMillis / 1000) % 60;
        textView.setText(String.format(Locale.KOREA, "%02d:%02d", minutes, seconds));
    }
}
