import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import com.example.core.firebase.FirebaseClient
import com.example.growup.data.market.remote.MarketRemoteContains
import com.example.growup.data.market.model.Products
import com.example.growup.ui.main.MainActivity
import com.example.growup.ui.market.AddAnnouncementActivity


object Utils : FirebaseClient(){
    fun progressShow(progress: ProgressDialog?){
        progress?.setMessage("Loading")
        progress?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress?.isIndeterminate = true
        progress?.progress = 0
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
            getRef(MarketRemoteContains.MARKET_REF).child(MarketRemoteContains.MARKET_SOLD).push().setValue(mData)
            getRef(MarketRemoteContains.MARKET_REF).child(MarketRemoteContains.MARKER_SALE).child(keyOfProduct!!).removeValue()
            MainActivity.start(context,"Марткет")
            GrowUpApplication.mSoldRef.push().setValue(mData)
            GrowUpApplication.mMarketRef.child("onSale").child(keyOfProduct!!).removeValue()
            MainActivity.start(context,"market")
        }
        builder.setNegativeButton("Нет"){dialog, which ->

        }
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }




}