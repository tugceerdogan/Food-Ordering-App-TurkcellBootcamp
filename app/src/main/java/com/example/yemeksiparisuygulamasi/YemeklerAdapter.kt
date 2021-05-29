package com.example.yemeksiparisuygulamasi

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class YemeklerAdapter(private var mContext: Context, private var yemeklerListe:ArrayList<Yemekler>)
: RecyclerView.Adapter<YemeklerAdapter.CardTasarimTutucu>() {



    inner class CardTasarimTutucu(tasarim: View) : RecyclerView.ViewHolder(tasarim) {
        var satir_card: CardView
        var satir_yemek_adi: TextView
        var satir_yemek_fiyat: TextView
        var satir_resim: ImageButton
        var sepeteEkle :ImageView

        init {
            satir_card = tasarim.findViewById(R.id.satir_card)
            satir_yemek_adi = tasarim.findViewById(R.id.satir_yemek_adi)
            satir_yemek_fiyat = tasarim.findViewById(R.id.satir_yemek_fiyat)
            satir_resim = tasarim.findViewById(R.id.satir_resim)
            sepeteEkle = tasarim.findViewById(R.id.sepeteEkle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.card_tasarim_yemek, parent, false)
        return CardTasarimTutucu(tasarim)
    }

    override fun getItemCount(): Int {
        return yemeklerListe.size
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {

        val yemek = yemeklerListe.get(position)


        holder.satir_yemek_adi.text = "${yemek.yemek_adi} "
        holder.satir_yemek_fiyat.text = " ${yemek.yemek_fiyat} \u20BA"

        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"

        Picasso.get().load(url).into(holder.satir_resim)

        holder.satir_resim.setOnClickListener{
            resimBuyutAlert(yemek)
        }

        holder.sepeteEkle.setOnClickListener {
            alertGoster(yemek)
        }

    }
    fun sepeteEkle(yemek_id : Int, yemek_adi:String, yemek_resim_adi : String, yemek_fiyat:Int, yemek_siparis_adet:Int) {

        val url = " http://kasimadalan.pe.hu/yemekler/insert_sepet_yemek.php"

        val request = object : StringRequest(Request.Method.POST, url, Response.Listener { cevap ->


        }, Response.ErrorListener {}) {

            override fun getParams(): MutableMap<String, String> {

                val params = HashMap<String, String>()

                params["yemek_id"] = yemek_id.toString()
                params["yemek_adi"] = yemek_adi
                params["yemek_resim_adi"] = yemek_resim_adi
                params["yemek_fiyat"] = yemek_fiyat.toString()
                params["yemek_siparis_adet"] = yemek_siparis_adet.toString()
                return params
            }
        }
        Volley.newRequestQueue(mContext).add(request)
    }

        fun alertGoster(yemek : Yemekler) {

            val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alert_tasarimi, null)
            val girilenSayi  = tasarim.findViewById(R.id.alert_siparisAdet) as EditText


            val ad = AlertDialog.Builder(mContext)
            ad.setTitle("Sipariş Sayısını giriniz : ")
            ad.setView(tasarim)
            ad.setPositiveButton("Ekle") { dialogInterface, i ->

                val yemek_siparis_adet = girilenSayi.text.toString().trim()

                if (TextUtils.isEmpty(yemek_siparis_adet)) {
                    Toast.makeText(mContext, "Lütfen sipariş adet sayısını giriniz.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if ( yemek_siparis_adet > 0.toString() ) {

                    sepeteEkle(yemek.yemek_id, yemek.yemek_adi, yemek.yemek_resim_adi, yemek.yemek_fiyat, yemek_siparis_adet.toInt())

                    Toast.makeText(mContext, "${yemek_siparis_adet} adet ${yemek.yemek_adi} sepetine eklendi!", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(mContext, "Lütfen geçerli bir sipariş adet sayısı giriniz.", Toast.LENGTH_SHORT).show()
                }
            }

          //  ad.setNegativeButton(" ") { dialogInterface, i ->
        //    }

            ad.create().show()
        }

        fun resimBuyutAlert(yemek : Yemekler) {

           val tasarim = LayoutInflater.from(mContext).inflate(R.layout.resim_alert, null)
           val resimBuyu  = tasarim.findViewById(R.id.resimBuyu) as ImageView
           val alert_Fiyat = tasarim.findViewById(R.id.alert_Fiyat) as TextView
           val alert_Ad = tasarim.findViewById(R.id.alert_Ad) as TextView


            val url = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"

           Picasso.get().load(url).into(resimBuyu)

            alert_Fiyat.text=" ${yemek.yemek_fiyat}₺"
            alert_Ad.text = " ${yemek.yemek_adi}"

           val ad = AlertDialog.Builder(mContext)

           ad.setView(tasarim)

           ad.create().show()
        }
}

















