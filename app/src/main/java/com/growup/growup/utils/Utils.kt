import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import com.growup.core.firebase.FirebaseClient
import com.growup.growup.GrowUpApplication
import com.growup.growup.data.market.remote.MarketRemoteConstants
import com.growup.growup.data.market.model.Products


object Utils : FirebaseClient(){
    fun progressShow(progress: ProgressDialog?){
        progress?.setMessage("Loading")
        progress?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress?.isIndeterminate = true
        progress?.progress = 0
        progress?.setCancelable(false)
        progress?.show()
    }

    fun confirmationForSold (
        context: Context,
        mData: Products,
        keyOfProduct: String?
    ){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Вы уверены ? ")
        builder.setMessage("Вы действительно хотите отметить товар как проданное ?")
        builder.setPositiveButton("Да"){dialog, which ->
            GrowUpApplication.mMarketRef.child("onSale").child(keyOfProduct!!).removeValue()
            getRef(MarketRemoteConstants.MARKET_REF).child(MarketRemoteConstants.MARKET_SOLD).push().setValue(mData)

        }
        builder.setNegativeButton("Нет"){dialog, which ->

        }
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }




}