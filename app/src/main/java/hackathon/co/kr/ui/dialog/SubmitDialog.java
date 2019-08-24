package hackathon.co.kr.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import hackathon.co.kr.neopen.R;

public class SubmitDialog {

    private Context mContext;
    private PositiveListener positiveListener;
    private NegativeListener negativeListener;

    public SubmitDialog(Context context, PositiveListener positiveListener, NegativeListener negativeListener) {
        mContext = context;
        this.positiveListener = positiveListener;
        this.negativeListener = negativeListener;
    }

    public void init() {

        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_submit);
        dialog.show();

        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        TextView tvOk = dialog.findViewById(R.id.tv_ok);


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negativeListener.onNegative();
                dialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveListener.onPositive();
                dialog.dismiss();
            }
        });
    }

    public interface PositiveListener extends DefaultListener{
        void onPositive();
    }

    public interface NegativeListener extends DefaultListener{
        void onNegative();
    }

    public interface DefaultListener {

    }


}
