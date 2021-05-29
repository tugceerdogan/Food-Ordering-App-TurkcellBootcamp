package com.example.yemeksiparisuygulamasi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {

    private lateinit var yemeklerListe: ArrayList<Yemekler>
    private lateinit var adapter: YemeklerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarMainActivity.setTitle("\n Yemekler(14 ürün)")
        setSupportActionBar(toolbarMainActivity)

        rV_yemek.setHasFixedSize(true)
        rV_yemek.layoutManager = LinearLayoutManager(this)

        yemeklerListe=ArrayList()
        adapter = YemeklerAdapter(this,yemeklerListe)
        rV_yemek.adapter = adapter

        tumYemekler()

        Sepet.setOnClickListener {
            startActivity(Intent(this@MainActivity, SepetActivity::class.java))

            overridePendingTransition(R.anim.sag_iceri,R.anim.sol_disari)
        }
    }

    override fun onBackPressed() {
        val yeniIntent = Intent(Intent.ACTION_MAIN)
        yeniIntent.addCategory(Intent.CATEGORY_HOME)
        yeniIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(yeniIntent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_arama, menu)

        val item = menu.findItem(R.id.action_ara)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onQueryTextSubmit(query: String): Boolean {
      Log.e("Gönderilen Arama Sonucu", query)
        yemekAra(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
      Log.e("Harf Girdikçe Sonuc", newText)
        yemekAra(newText)
        return true
    }

    fun tumYemekler() {

        val url = "http://kasimadalan.pe.hu/yemekler/tum_yemekler.php"

        val request = StringRequest(Request.Method.GET, url, Response.Listener { cevap ->

            try {
                yemeklerListe.clear()

                val jsonObject = JSONObject(cevap)
                val yemekler = jsonObject.getJSONArray("yemekler")

                for (i in 0 until yemekler.length()) {
                    val y = yemekler.getJSONObject(i)

                    val yemek = Yemekler(y.getInt("yemek_id")
                                        ,y.getString("yemek_adi")
                                        ,y.getString("yemek_resim_adi")
                                        ,y.getInt("yemek_fiyat"))

                    yemeklerListe.add(yemek)
                }
                adapter.notifyDataSetChanged()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, Response.ErrorListener {})


        Volley.newRequestQueue(this@MainActivity).add(request)
    }


    fun yemekAra(aramaKelime: String) {

        val url = "http://kasimadalan.pe.hu/yemekler/tum_yemekler_arama.php"

        val request = object : StringRequest(Method.POST, url, Response.Listener { cevap ->

            try{
                yemeklerListe.clear()

                val jsonObject = JSONObject(cevap)
                val yemekler = jsonObject.getJSONArray("yemekler")

                for(i in 0 until yemekler.length()){
                    val y = yemekler.getJSONObject(i)

                    val yemek = Yemekler(y.getInt("yemek_id")
                                        ,y.getString("yemek_adi")
                                        ,y.getString("yemek_resim_adi")
                                        ,y.getInt("yemek_fiyat"))

                    yemeklerListe.add(yemek)
                }

                adapter.notifyDataSetChanged()

            } catch(e : Exception){
                e.printStackTrace()
            }

        },  Response.ErrorListener {  }){
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String,String>()
                params["yemek_adi"] = aramaKelime
                return params
            }
        }

        Volley.newRequestQueue(this@MainActivity).add(request)
    }

}































































