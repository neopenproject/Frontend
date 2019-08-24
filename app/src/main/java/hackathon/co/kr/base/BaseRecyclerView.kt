package hackathon.co.kr.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView {
    abstract class Adapter<ITEM : Any, B : ViewDataBinding>(
            @LayoutRes private val layoutResId: Int,
            private val bindingVariableId: Int? = null
    ) : RecyclerView.Adapter<Adapter.ViewHolder<B>>() {
        private val items = mutableListOf<ITEM>()

        // create
        fun replaceAll(items: List<ITEM>?) {
            items?.let {
                this.items.run {
                    clear()
                    addAll(it)
                    notifyDataSetChanged()
                }
            }
        }

        // insert
        fun insertItems(item: List<ITEM>?, index: Int, lastIndex: Int) {
            item?.let {
                this.items.run {
                    addAll(index, item)
                    notifyItemRangeInserted(index, this.size)
                }
            }
        }

        // update
        fun replaceItem(item: ITEM?, index: Int) {
            item?.let {
                this.items.run {
                    this[index] = item
                    notifyItemChanged(index)
                }
            }
        }

        // delete
        fun deleteItem(index: Int) {
            this.items.run {
                this.removeAt(index)
                notifyItemRemoved(index)
            }
        }

        fun deleteItems(items: List<ITEM>?, index: Int) {
            this.items.run {
                this.removeAll(items as Collection<ITEM>)
                notifyItemRangeRemoved(index, items.size)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = object : ViewHolder<B>(
                layoutResId = layoutResId,
                parent = parent,
                bindingVariableId = bindingVariableId
        ) {}

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
            holder.onBindViewHolder(items[position])
        }

        abstract class ViewHolder<B : ViewDataBinding>(
                @LayoutRes layoutResId: Int,
                parent: ViewGroup,
                private val bindingVariableId: Int?
        ) : RecyclerView.ViewHolder(
                LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        ) {
            private var binding: B = DataBindingUtil.bind(itemView)!!

            fun onBindViewHolder(item: Any?) {
                try {
                    bindingVariableId?.let {
                        binding.setVariable(it, item)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}