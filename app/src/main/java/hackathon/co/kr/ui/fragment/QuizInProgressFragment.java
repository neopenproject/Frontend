package hackathon.co.kr.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hackathon.co.kr.adapter.QuizInProgressRVAdapter;
import hackathon.co.kr.base.listener.OnRecyclerObjectClickListener;
import hackathon.co.kr.base.listener.OnRecyclerPositionClickListener;
import hackathon.co.kr.neopen.R;
import hackathon.co.kr.neopen.temp.PenActivity;
import hackathon.co.kr.util.network.NetworkUtil;
import hackathon.co.kr.util.network.model.Posts;
import hackathon.co.kr.util.network.model.ProblemResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizInProgressFragment extends Fragment {

    private RecyclerView recyclerView;
    private QuizInProgressRVAdapter quizInProgressRVAdapter;
    public static QuizInProgressFragment newInstance() {
        return new QuizInProgressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_in_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        quizInProgressRVAdapter = new QuizInProgressRVAdapter(getActivity(), new OnRecyclerObjectClickListener<Posts>() {
            @Override
            public void onItemClicked(Posts item) {
                Intent intent = new Intent(getActivity(), PenActivity.class);
                intent.putExtra("QUIZ_PK", item.getId());
                intent.putExtra("IMAGE_URL", item.getProblem_img());
                intent.putExtra("TIME_VALUE", item.getMax_time());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(quizInProgressRVAdapter);

        getData();

    }

    private void getData() {
        NetworkUtil.getInstance().getAnswerPost().enqueue(new Callback<ProblemResponse>() {
            @Override
            public void onResponse(Call<ProblemResponse> call, Response<ProblemResponse> response) {
                if (response.body() != null) {
                    quizInProgressRVAdapter.addAll(response.body().getResult().getProblem_posts());
                }
                else {
                    Toast.makeText(getActivity(), "서버 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProblemResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
