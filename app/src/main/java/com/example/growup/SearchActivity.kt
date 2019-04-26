package com.example.growup

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.example.growup.models.User
import kotlinx.android.synthetic.main.recycler_view_search_item.*


class SearchActivity : AppCompatActivity() {
    var adapter:RecyclerViewSearchAdapter? = null
    var list:ArrayList<String> = ArrayList()

    private var recyclerView:RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initList()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        adapter = RecyclerViewSearchAdapter(this@SearchActivity,list)
        adapter!!.notifyDataSetChanged()
        recyclerView?.adapter = adapter
    }

    private fun initList() {
        for (user: User in GrowUpApplication.mUserList){
            list.add(user.name +" "+user.lastName)
        }
    }


    inner class RecyclerViewSearchAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable{

        private var list: ArrayList<String>? = null
        private var listFull: ArrayList<String>? = null
        private var context: Context? = null

        private var recyclerFilter:RecyclerFilter? = null
        constructor(context: Context , list: ArrayList<String>): this(){
            this.listFull = list
            this.list = list
            this.context = context

        }
        override fun getFilter(): Filter {
            if(recyclerFilter == null){
                recyclerFilter = RecyclerFilter()
            }
            return recyclerFilter as RecyclerFilter
        }
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            var queryText: TextView? = null
            var userImage: ImageView? = null
            init{
                userImage = view.findViewById(R.id.user_image_list)
                queryText = view.findViewById(R.id.query_text)

            }

        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
           return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_search_item,p0,false))

        }

        override fun getItemCount(): Int {
            return list?.size as Int
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
          var holder: ViewHolder = p0 as ViewHolder
            holder.queryText?.text = list?.get(p1)

        }



        inner class RecyclerFilter: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var result: FilterResults = FilterResults()
                if(constraint != null && constraint.isNotEmpty()){
                    var localList: ArrayList<String> = ArrayList<String>()
                        for (i: Int in 0..listFull?.size?.minus(1) as Int){
                            if (listFull?.get(i)?.toLowerCase()?.contains(constraint.toString().toLowerCase()) as Boolean){
                                localList.add(listFull?.get(i) as String)
                            }
                        }
                    result.values = localList
                    result.count  = localList.size
                    }else{
                    result.values = listFull
                    result.count  = listFull?.size as Int

                }
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        var searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        var searchView: SearchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!!.isEmpty()){
                    recyclerView?.visibility = View.GONE
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.equals("")){
                    this.onQueryTextSubmit("")
                    recyclerView?.visibility = View.GONE
                }else{
                adapter?.filter?.filter(newText)
                recyclerView?.visibility = View.VISIBLE
                }
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }
}


