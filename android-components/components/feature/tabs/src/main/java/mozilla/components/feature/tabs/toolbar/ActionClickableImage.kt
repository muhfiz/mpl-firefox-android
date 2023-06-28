package mozilla.components.feature.tabs.toolbar

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import mozilla.components.concept.toolbar.Toolbar
import mozilla.components.support.base.android.Padding
import mozilla.components.support.ktx.android.view.setPadding

/**
 * An action that just shows a static, non-clickable image.
 *
 * @param imageDrawable The drawable to be shown.
 * @param contentDescription Optional content description to be used. If no content description
 *                           is provided then this view will be treated as not important for
 *                           accessibility.
 * @param padding A optional custom padding.
 * @param onClick A optional onClick callback
 */
open class ActionClickableImage(
    private val imageDrawable: Drawable,
    private val contentDescription: String? = null,
    private val padding: Padding? = null,
    private val onClick: (() -> Unit)? = null
) : Toolbar.Action {

    override fun createView(parent: ViewGroup): View = AppCompatImageView(parent.context).also { image ->
        image.minimumWidth = imageDrawable.intrinsicWidth
        image.setImageDrawable(imageDrawable)

        image.contentDescription = contentDescription
        image.importantForAccessibility = if (contentDescription.isNullOrEmpty()) {
            View.IMPORTANT_FOR_ACCESSIBILITY_NO
        } else {
            View.IMPORTANT_FOR_ACCESSIBILITY_AUTO
        }
        if(onClick != null){
            image.setOnClickListener { onClick.invoke() }
        }
        padding?.let { pd -> image.setPadding(pd) }
    }

    override fun bind(view: View) = Unit
}
