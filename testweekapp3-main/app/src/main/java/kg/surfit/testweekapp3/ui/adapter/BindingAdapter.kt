package kg.surfit.testweekapp3.ui.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImageUrl(imageView: ImageView, url: String?) {
    Picasso.get().load(url).into(imageView)
}
