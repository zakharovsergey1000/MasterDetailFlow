package com.example.sergey.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.sergey.myapplication.databinding.ActivityItemListBindingImpl
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityItemListBindingImpl = DataBindingUtil.setContentView(
            this, R.layout.activity_item_list)

//        binding.user = User("Test", "User")
        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        val model: MyViewModel by viewModels()
        swiperefreshlayout.setOnRefreshListener {
            model.loadUsers()
        }
val editText:EditText = EditText(this)
        setupRecyclerView(item_list, model.getUsers())
        val handler = Handler()
        model.usersResource.observeOnce(this, "ItemListActivity", Observer { resource ->
            handler.post(Runnable {
                swiperefreshlayout.isRefreshing = false
            })

            if (resource.status == Status.ERROR){
                Utils.makeToast(this, resource.throwable!!.localizedMessage, Toast.LENGTH_LONG).show();
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("swiperefreshlayout.isRefreshing", swiperefreshlayout.isRefreshing)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        swiperefreshlayout.isRefreshing = savedInstanceState.getBoolean("swiperefreshlayout.isRefreshing", false)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }
    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        users: LiveData<List<User>>
    ) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, users, twoPane)
    }
    class MyViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        private val binding: ViewDataBinding
        fun bind(obj: Any?) {
            binding.setVariable(BR.obj, obj)
            binding.executePendingBindings()
            binding.root.tag = obj
        }

        init {
            this.binding = binding
        }
    }
    abstract class MyBaseAdapter : RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: ViewDataBinding = DataBindingUtil.inflate(
                layoutInflater, viewType, parent, false
            )
            return MyViewHolder(binding)
        }

        override fun onBindViewHolder(
            holder: MyViewHolder,
            position: Int
        ) {
            val obj = getObjForPosition(position)
            holder.bind(obj)
        }

        override fun getItemViewType(position: Int): Int {
            return getLayoutIdForPosition(position)
        }

        protected abstract fun getObjForPosition(position: Int): Any
        protected abstract fun getLayoutIdForPosition(position: Int): Int
    }
    abstract class SingleLayoutAdapter(private val layoutId: Int) : MyBaseAdapter() {
        override fun getLayoutIdForPosition(position: Int): Int {
            return layoutId
        }

    }
    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val users: LiveData<List<User>>,
        private val twoPane: Boolean
    ) :
        SingleLayoutAdapter(R.layout.item_list_content) {
        private var values: List<User> = emptyList()
        private val onClickListener: View.OnClickListener

        init {
            users.observe(parentActivity, Observer<List<User>>{ users ->
                // update UI
                values = users
                notifyDataSetChanged()
            })

            onClickListener = View.OnClickListener { v ->
                val item = (v.tag as Item)
                parentActivity.sharedViewModel.select(item.dummyItem.id)
                if (twoPane) {
                    val fragment = ItemDetailFragment()
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.dummyItem.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun getObjForPosition(position: Int): Any {
            return Item(values[position], onClickListener)
        }

//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_list_content, parent, false)
//            return ViewHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            val item = values[position]
//            holder.idView.text = item.id
//            holder.contentView.text = item.content
//
//            with(holder.itemView) {
//                tag = item
//                setOnClickListener(onClickListener)
//            }
//        }

        override fun getItemCount() = values.size

//        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val idView: TextView = view.id_text
//            val contentView: TextView = view.content
//        }
    }
}

class Item(val dummyItem: User, val onClickListener: View.OnClickListener) {

}
class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Int>()

    fun select(item: Int) {
        selected.value = item
    }
}