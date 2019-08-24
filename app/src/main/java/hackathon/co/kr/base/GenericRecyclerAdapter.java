package hackathon.co.kr.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import hackathon.co.kr.base.listener.BaseRecyclerListener;
import hackathon.co.kr.base.listener.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericRecyclerAdapter<T, L extends
        BaseRecyclerListener, VH extends BaseViewHolder<T, L>> extends
        RecyclerView.Adapter<VH> {

    private List<T> items;
    private L listener;
    private LayoutInflater layoutInflater;

    public GenericRecyclerAdapter(Context context, L listener) {
        this.listener = listener;
        layoutInflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (items.size() <= position) {
            return;
        }
        T item = items.get(position);
        holder.onBind(item, listener);
    }


    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setItems(List<T> items) {
        if (items == null) {
            throw new IllegalArgumentException("Cannot set 'null' item to the Recycler adapter");
        }
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return items;
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to the Recycler adapter");
        }
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addToBeginning(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to the Recycler adapter");
        }
        items.add(0, item);
        notifyItemInserted(0);
    }


    public void addAll(List<T> items) {
        if (items == null) {
            throw new IllegalArgumentException("Cannot add 'null' items to the Recycler adapter");
        }
        this.items.addAll(items);
        notifyItemRangeChanged(this.items.size() - items.size(), items.size());
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(T item) {
        int position = items.indexOf(item);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    /*
   사용 이유 : notifyDataSetChanged 하였을 때 item이 깜빡이는 문제를 해결 하기 위해
   깜박임의 원인 : notifyDataSetChanged를 하였을 때, Adapter가 이미 존재하는 전체 데이터셋의 아이템들 중 어떤 것이 변경된 것인지 모르므로,
   viewHolder를 전부 다시 매칭시켜주면서 발생
    해결 방법
    1. Adapter에게 각 아이템들의 유니크한 값들이 있다고 알려주는것. (setHasStableIds 메소드를 true로 설정 해주면, Adapter가 아이템들을 처적)
    2. 각 아이템의 유니크한 아이디값 부여 (getItemId 메소드를 override 하여서 구현)
     */
    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void setListener(L listener) {
        this.listener = listener;
    }

    public L getListener() {
        return listener;
    }

    @NonNull
    protected View inflate(@LayoutRes final int layout, @Nullable final ViewGroup parent, final boolean attachToRoot) {
        return layoutInflater.inflate(layout, parent, attachToRoot);
    }

    @NonNull
    protected View inflate(@LayoutRes final int layout, final @Nullable ViewGroup parent) {
        return inflate(layout, parent, false);
    }

}