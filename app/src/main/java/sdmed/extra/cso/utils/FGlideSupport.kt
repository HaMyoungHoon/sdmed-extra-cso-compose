package sdmed.extra.cso.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import sdmed.extra.cso.interfaces.command.IEventListener
import java.io.File

class FGlideSupport {
    companion object {
        fun imageLoad(context: Context, file: File): RequestBuilder<Drawable> {
            return Glide.with(context).load(file)
        }
        fun imageLoad(context: Context, file: File, imageView: ImageView) {
            Glide.with(context).load(file)
                .into(imageView)
        }
        fun imageLoad(file: File, imageView: ImageView) {
            Glide.with(imageView.context).load(file)
                .into(imageView)
        }
        fun imageLoad(@androidx.annotation.DrawableRes resourceId: Int, imageView: ImageView) {
            Glide.with(imageView.context).load(resourceId)
                .into(imageView)
        }
        fun imageLoad(url: String, imageView: ImageView, loadFailedListener: IEventListener? = null, resourceReadyListener: IEventListener? = null) {
            Glide.with(imageView.context).load(url)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                        loadFailedListener?.onEvent(arrayListOf(e, model, target, isFirstResource))
                        return false
                    }
                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable?>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        resourceReadyListener?.onEvent(arrayListOf(resource, model, target, dataSource, isFirstResource))
                        return false
                    }
                })
                .into(imageView)
        }
        fun imageLoad(uri: Uri, imageView: AppCompatImageView, loadFailedListener: IEventListener? = null, resourceReadyListener: IEventListener? = null) {
            Glide.with(imageView.context).load(uri)
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                        loadFailedListener?.onEvent(arrayListOf(e, model, target, isFirstResource))
                        return false
                    }
                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable?>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        resourceReadyListener?.onEvent(arrayListOf(resource, model, target, dataSource, isFirstResource))
                        return false
                    }
                })
                .into(imageView)
        }
        fun imageResizedLoad(url: String, imageView: ImageView, width: Int?, height: Int?) {
            Glide.with(imageView.context).load(url)
                .override(width ?: Target.SIZE_ORIGINAL, height ?: Target.SIZE_ORIGINAL)
                .into(imageView)
        }
        fun circleImageLoad(url: String, imageView: ImageView) {
            Glide.with(imageView.context).load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView)
        }
        fun radiusImageLoad(url: String, imageView: ImageView, radius: Int) {
            Glide.with(imageView.context).load(url)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(radius)))
                .into(imageView)
        }
        fun assetGifImageLoad(assetGifSrc: String, imageView: ImageView, loopCount: Int) {
            try {
                Glide.with(imageView.context).asGif().load(assetGifSrc)
                    .listener(object: RequestListener<GifDrawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable?>, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onResourceReady(resource: GifDrawable, model: Any, target: Target<GifDrawable?>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            resource.setLoopCount(loopCount)
                            return false
                        }
                    })
                    .into(imageView)
            } catch (_: Exception) {
            }
        }
        fun imageLoad(imageView: AppCompatImageView, @DrawableRes resId: Int, onResourceReady: (Drawable) -> Unit) {
            Glide.with(imageView.context).load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalCenterCrop()
                .priority(Priority.IMMEDIATE)
                .into(object: CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        onResourceReady(resource)
                    }
                })
        }
    }
}