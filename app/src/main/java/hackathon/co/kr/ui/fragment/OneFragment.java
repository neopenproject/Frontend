package hackathon.co.kr.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import hackathon.co.kr.neopen.R;
import hackathon.co.kr.neopen.databinding.FragmentOneBinding;
import hackathon.co.kr.neopen.temp.PenActivity;

public class OneFragment extends Fragment {

    FragmentOneBinding binding;

    public static OneFragment newInstance() {
        return new OneFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_one, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setFragment(this);
    }

    public void onButtonClick(View view){
        Intent intent = new Intent(getActivity(), PenActivity.class);
        startActivity(intent);
    }

}
