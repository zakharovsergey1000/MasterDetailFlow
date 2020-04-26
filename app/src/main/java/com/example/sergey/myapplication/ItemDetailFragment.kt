package com.example.sergey.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.coroutines.launch

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {
    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var binding: ViewDataBinding? = null

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: User? = null
    // Use the 'by activityViewModels()' Kotlin property delegate
    // from the fragment-ktx artifact
    private val model: SharedViewModel by activityViewModels()
    private val itemDetailFragmentViewModel: ItemDetailFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = null
                activity?.toolbar_layout?.title = item?.content
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_detail, container, false
        )


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.selected.observe(viewLifecycleOwner, Observer<Int> { id ->
            // Update the UI
            itemDetailFragmentViewModel.getUser(id).observe(
                viewLifecycleOwner,
                Observer { user -> binding?.setVariable(BR.user, user) })
        })
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        binding = null
        super.onDestroyView()
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}

class ItemDetailFragmentViewModel : ViewModel() {
    private var _user: MutableLiveData<User> = MutableLiveData()
    fun getUser(id: Int): LiveData<User> {
        if (_user.value == null || _user.value!!.id != id) {
            loadUser(id)
        }
        return _user
    }

    private val repository: Repository = Repository()
    fun loadUser(id: Int) {
        viewModelScope.launch {
            val retrivedTodo = repository.getUser(id)

            _user.value = retrivedTodo
        }
    }
}