package hackathon.co.kr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import hackathon.co.kr.base.GenericRecyclerAdapter;
import hackathon.co.kr.base.listener.BaseViewHolder;
import hackathon.co.kr.base.listener.OnRecyclerObjectClickListener;
import hackathon.co.kr.base.listener.OnRecyclerPositionClickListener;
import hackathon.co.kr.neopen.R;
import hackathon.co.kr.util.network.model.Posts;

public class QuizInProgressRVAdapter extends GenericRecyclerAdapter<Posts, OnRecyclerObjectClickListener<Posts>, QuizInProgressRVAdapter.QuizInProgressVH> {

    private Context mContext;

    public QuizInProgressRVAdapter(Context context, OnRecyclerObjectClickListener listener) {
        super(context, listener);
        mContext = context;
    }

    @NonNull
    @Override
    public QuizInProgressVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new QuizInProgressVH(LayoutInflater.from(mContext).inflate(R.layout.item_quiz_in_progress, viewGroup, false));
    }

    class QuizInProgressVH extends BaseViewHolder<Posts, OnRecyclerObjectClickListener<Posts>> {
        private ConstraintLayout rootView;
        private TextView tvTitle;
        private TextView tvSubTitle;
        private RelativeLayout rlSubjectLayout;
        private TextView tvSubjectTitle;
        private RelativeLayout rlCategoryLayout;
        private TextView tvCategoryTitle;

        QuizInProgressVH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubTitle = itemView.findViewById(R.id.tv_sub_title);
            rlSubjectLayout = itemView.findViewById(R.id.rl_subject_layout);
            tvSubjectTitle = itemView.findViewById(R.id.tv_subject_title);
            rlCategoryLayout = itemView.findViewById(R.id.rl_category_layout);
            tvCategoryTitle = itemView.findViewById(R.id.tv_category_title);
            rootView = (ConstraintLayout) itemView;
        }

        @Override
        public void onBind(Posts item, @Nullable OnRecyclerObjectClickListener<Posts> listener) {
            if (listener != null) {
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClicked(item);
                    }
                });
            }

            tvTitle.setText(item.getTitle());
            tvSubTitle.setText(item.getSub_title());
            if (item.getSubject().equals("")) {
                tvSubjectTitle.setVisibility(View.GONE);
                rlSubjectLayout.setVisibility(View.GONE);
            }
            else {
                tvSubjectTitle.setVisibility(View.VISIBLE);
                rlSubjectLayout.setVisibility(View.VISIBLE);
                tvSubjectTitle.setText(item.getSubject());
            }

            if (item.getCategory().equals("")) {
                tvCategoryTitle.setVisibility(View.GONE);
                rlCategoryLayout.setVisibility(View.GONE);
            }
            else {
                tvCategoryTitle.setVisibility(View.VISIBLE);
                rlCategoryLayout.setVisibility(View.VISIBLE);
                tvCategoryTitle.setText(item.getCategory());

            }
        }
    }
}
