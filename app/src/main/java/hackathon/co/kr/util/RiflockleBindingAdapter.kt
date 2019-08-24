package hackathon.co.kr.util

import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.databinding.BindingAdapter

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