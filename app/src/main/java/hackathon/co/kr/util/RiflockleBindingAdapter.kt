package hackathon.co.kr.util

import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import hackathon.co.kr.neopen.R
import kotlinx.android.synthetic.main.activity_pen.view.*

@BindingAdapter("typeSetting")
fun setEditTextInputType(view: EditText, type: Int) {
    when (type) {
        0 -> {
            view.inputType = InputType.TYPE_CLASS_TEXT
        }
        else -> {
            view.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    view.requestLayout()
}

@BindingAdapter("android:src")
fun setSrc(view: ImageView, url: String?) {
    Log.d("url ", url)
    if (url == null)
        Glide.with(view.context).load(R.mipmap.ic_launcher).into(view)
    else
        Glide.with(view.context).load(url).into(view)
}
@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    if(isVisible)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}