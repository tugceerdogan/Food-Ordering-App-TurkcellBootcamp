package com.example.yemeksiparisuygulamasi



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.Volley.newRequestQueue
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.json.JSONObject

class SepettekilerAdapter( private var mContext: Context,private var sepetListe:ArrayList<Sepet>)
    : RecyclerView.Adapter<SepettekilerAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(tasarim: View) : RecyclerView.ViewHolder(tasarim) {
        var satir_card: CardView
        var satir_resim: ImageButton
        var satir_yemek_adi: TextView
        var satir_yemek_fiyat: TextView
        var silme_resim: ImageView
        var siparisAdeti: TextView

        init {
            satir_card = tasarim.findViewById(R.id.satir_card)
            satir_yemek_adi = tasarim.findViewById(R.id.satir_yemek_adi)
            satir_yemek_fiyat = tasarim.findViewById(R.id.satir_yemek_fiyat)
            satir_resim = tasarim.findViewById(R.id.satir_resim)
            silme_resim = tasarim.findViewById(R.id.silme_resim)
            siparisAdeti = tasarim.findViewById(R.id.siparisAdeti)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.card_tasarim_sepet, parent, false)

        return CardTasarimTutucu(tasarim)
    }

    override fun getItemCount(): Int {
        return sepetListe.size
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {

        val sepet = sepetListe.get(position)

        holder.satir_yemek_adi.text = "${sepet.yemek_adi} "
        holder.satir_yemek_fiyat.text = "${sepet.yemek_siparis_adet*sepet.yemek_fiyat} \u20BA"
        holder.siparisAdeti.text = "Sipariş adeti : ${sepet.yemek_siparis_adet}"

        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${sepet.yemek_resim_adi}"

        Picasso.get().load(url).into(holder.satir_resim)

        holder.silme_resim.setOnClickListener {

            Snackbar.make(holder.silme_resim,"Sepetindeki ${sepet.yemek_adi} silinsin mi ?", Snackbar.LENGTH_LONG).setAction("EVET") {
                Toast.makeText(mContext, "${sepet.yemek_adi} sepetinden silindi", Toast.LENGTH_SHORT).show()


            val url = "http://kasimadalan.pe.hu/yemekler/delete_sepet_yemek.php"

            val request = object : StringRequest(Request.Method.POST, url, Response.Listener { cevap ->

           sepettekiYemekler()

            }, Response.ErrorListener { }) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["yemek_id"] = sepet.yemek_id.toString()

                    return params
                }
            }

            newRequestQueue(mContext).add(request)

            }.show()

        }

    }

    fun sepettekiYemekler() {

        val url = "http://kasimadalan.pe.hu/yemekler/tum_sepet_yemekler.php"

        val request = object : StringRequest(Request.Method.GET, url, Response.Listener { cevap ->

            val tempListe = ArrayList<Sepet>()
            sepetListe = tempListe

            notifyDataSetChanged()

            try {

                val jsonObject = JSONObject(cevap)
                val sepet_yemekler = jsonObject.getJSONArray("sepet_yemekler")

                for (i in 0 until sepet_yemekler.length()) {
                    val y = sepet_yemekler.getJSONObject(i)

                    val sepet = Sepet(y.getInt("yemek_id")
                            , y.getString("yemek_adi")
                            , y.getString("yemek_resim_adi")
                            , y.getInt("yemek_fiyat")
                            , y.getInt("yemek_siparis_adet"))

                    tempListe.add(sepet)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, Response.ErrorListener { }) {
        }


        newRequestQueue(mContext).add(request)
    }
}




