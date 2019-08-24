package hackathon.co.kr.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hackathon.co.kr.base.BaseRecyclerView;
import hackathon.co.kr.neopen.R;
import hackathon.co.kr.neopen.databinding.FragmentOneBinding;
import hackathon.co.kr.neopen.databinding.ItemCorrectionBinding;
import hackathon.co.kr.ui.model.Correction;
import hackathon.co.kr.ui.viewModel.OneViewModel;
import org.jetbrains.annotations.NotNull;

import static hackathon.co.kr.ui.model.CorrectionKt.getDefaultCorrections;

public class OneFragment extends Fragment {

    FragmentOneBinding binding;
    BaseRecyclerView.Adapter<Correction, ItemCorrectionBinding> correctionAdapter;

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
        binding.setOneVM(ViewModelProviders.of(this).get(OneViewModel.class));

        binding.rvCorrections.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        correctionAdapter = new BaseRecyclerView.Adapter<Correction, ItemCorrectionBinding>(
                R.layout.item_correction,
                BR.icCorrection) {
            @NotNull
            @Override
            public ViewHolder<ItemCorrectionBinding> onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
                ViewHolder<ItemCorrectionBinding> holder = super.onCreateViewHolder(parent, viewType);

                holder.itemView.setOnClickListener(view -> {

                });

                return holder;
            }

            @Override
            public void onBindViewHolder(@NotNull ViewHolder<ItemCorrectionBinding> holder, int position) {
                super.onBindViewHolder(holder, position);
            }
        };

        binding.rvCorrections.setAdapter(correctionAdapter);
        correctionAdapter.replaceAll(getDefaultCorrections());
    }

//    public void onButtonClick(View view) {
//        Intent intent = new Intent(getActivity(), PenActivity.class);
//        startActivity(intent);
//    }

}
