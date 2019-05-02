package com.example.growup.ui.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.growup.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.example.growup.GrowUpApplication
import com.example.growup.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SearchActivity : AppCompatActivity() {
    var adapter: RecyclerViewSearchAdapter? = null
    var list: ArrayList<String> = ArrayList()
    var userArray: ArrayList<User> = ArrayList()
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
        initList()

    }

    fun init() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        adapter = RecyclerViewSearchAdapter(this@SearchActivity, list)
        adapter!!.notifyDataSetChanged()
        recyclerView?.adapter = adapter
    }

    private fun initList() {
        GrowUpApplication.mUserRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SearchActivity, databaseError.message, Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               if(dataSnapshot.exists()){
                   dataSnapshot.children.forEach {
                       list.add(it.getValue(User::class.java)!!.name+" "+it.getValue(User::class.java)!!.lastName)
                   }
               }
            }
        })
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
                if (query!!.isEmpty()) {
                    recyclerView?.visibility = View.GONE
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")) {
                    this.onQueryTextSubmit("")
                    recyclerView?.visibility = View.GONE
                } else {
                    adapter?.filter?.filter(newText)
                    recyclerView?.visibility = View.VISIBLE
                }
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}