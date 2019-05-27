package com.growup.growup.ui.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.growup.growup.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.growup.growup.GrowUpApplication
import com.growup.growup.data.user.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import android.widget.Toast


class SearchActivity : AppCompatActivity() , RecyclerViewSearchAdapter.Listener {
    private var adapter: RecyclerViewSearchAdapter? = null
    private var list: ArrayList<User> = ArrayList()
    private var listWithUserKeys: ArrayList<String> = ArrayList() // this list needs for getting image from firebase storage
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        getUsersData()
        init()
    }

    private fun init() {
        supportActionBar?.title = "Поиск"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_28_white)
        recyclerView = findViewById(R.id.recyclerViewSearch)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL , false)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun getUsersData(){
        GrowUpApplication.mUserRef.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@SearchActivity,databaseError.message,Toast.LENGTH_LONG).show()
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        list.add(it.getValue(User::class.java)!!)
                        listWithUserKeys.add(it.key.toString())
                    }

                    adapter = this@SearchActivity.let {
                        RecyclerViewSearchAdapter(list, listWithUserKeys, this@SearchActivity, it)
                    }
                    adapter?.notifyDataSetChanged()
                    recyclerView?.adapter = adapter

                }
        })
    }

    override fun onItemSelectedAt(position: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView = searchItem?.actionView as SearchView

        searchItem.setOnMenuItemClickListener {
            searchView.setIconifiedByDefault(true)
            searchView.isFocusable = true
            searchView.isIconified = false
            searchView.requestFocusFromTouch()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter(newText)
                if (newText.trim().isEmpty()){
                    recyclerView?.visibility = View.GONE
                    Toast.makeText(this@SearchActivity, "1",  Toast.LENGTH_SHORT).show()
                }else{
                    recyclerView?.visibility = View.VISIBLE
                    Toast.makeText(this@SearchActivity, "2",  Toast.LENGTH_SHORT).show()
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


}