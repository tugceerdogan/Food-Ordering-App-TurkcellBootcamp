package com.example.yemeksiparisuygulamasi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_sepet.*
import org.json.JSONObject


class SepetActivity : AppCompatActivity() {

    private lateinit var sepetListe: ArrayList<Sepet>
    private lateinit var sepetAdapter: SepettekilerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sepet)

        toolbarSepetActivity.setTitle("\n Sepetim")
        setSupportActionBar(toolbarSepetActivity)

        rV_sepet.setHasFixedSize(true)
        rV_sepet.layoutManager = LinearLayoutManager(this)

        sepettekiYemekler()


        button.setOnClickListener {
            Toast.makeText(this, "AFİYET OLSUN!", Toast.LENGTH_SHORT).show()
        }

        }
        override fun finish() {
            super.finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        }
      fun sepettekiYemekler(){

         val url = "http://kasimadalan.pe.hu/yemekler/tum_sepet_yemekler.php"

         val request = StringRequest(Request.Method.GET,url, Response.Listener { cevap ->

            try{

              sepetListe = ArrayList()

               val jsonObject = JSONObject(cevap)
               val sepet_yemekler = jsonObject.getJSONArray("sepet_yemekler")


               for(i in 0 until sepet_yemekler.length()){
                  val s = sepet_yemekler.getJSONObject(i)

                  val sepet = Sepet(s.getInt("yemek_id")
                          ,s.getString("yemek_adi")
                          ,s.getString("yemek_resim_adi")
                          ,s.getInt("yemek_fiyat")
                          ,s.getInt("yemek_siparis_adet"))

                 sepetListe.add(sepet)

               }

               sepetAdapter = SepettekilerAdapter(this,sepetListe)
               rV_sepet.adapter = sepetAdapter

            }catch (e:Exception){
               e.printStackTrace()
            }

         }, Response.ErrorListener {  })

         Volley.newRequestQueue(this@SepetActivity).add(request)
      }
    }







