package hackathon.co.kr.base.listener;

public interface OnRecyclerObjectClickListener<T> extends BaseRecyclerListener {
    void onItemClicked(T item);
}
