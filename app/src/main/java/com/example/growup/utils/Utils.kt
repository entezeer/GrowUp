import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.growup.R


object Utils {
    fun progressShow(progress: ProgressDialog?){
        progress?.setMessage("Loading")
        progress?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress?.isIndeterminate = true
        progress?.progress = 0
        progress?.show()
    }

    fun showInternetAlert(context: Context){

        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.network_alert_dialog,null)
        val gifImageView = view.findViewById<ImageView>(R.id.gif_image_view)


        Glide.with(context).load(R.drawable.nointernet).into(gifImageView)

        val dialog = AlertDialog.Builder(context)
        dialog.setPositiveButton("Try again",null)
        dialog.setView(view)
        dialog.show()
    }
}