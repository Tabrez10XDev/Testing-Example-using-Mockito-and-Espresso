package com.example.unittestingexample_junit4.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unittestingexample_junit4.R
import com.example.unittestingexample_junit4.adapters.ShoppingItemAdapter
import com.example.unittestingexample_junit4.databinding.FragmentShoppingBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_shopping.*
import javax.inject.Inject

class ShoppingFragment @Inject constructor(
    val shoppingItemAdapter: ShoppingItemAdapter,
    var viewModel: ShoppingViewModel? = null
) : Fragment(R.layout.fragment_shopping) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        setupRecyclerView()
        subscribeToSubscribers()
        fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(R.id.action_shoppingFragment_to_addShoppingItemFragment)
        }
    }

    private val itemTouchCallBack = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel?.deleteShoppingItem(item)
            Snackbar.make(
                requireView(),
                "Successfully deleted item",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction("Undo"){
                    viewModel?.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }
    }

    private fun subscribeToSubscribers(){
        viewModel?.shoppingItems?.observe(viewLifecycleOwner, Observer {
            shoppingItemAdapter.shoppingItems = it
        })
        viewModel?.totalPrice?.observe(viewLifecycleOwner, Observer {
            val price = it ?: 0f
            val priceText = "Total Price: $price Ru"
            tvShoppingItemPrice.text = priceText
        })
    }

    private fun setupRecyclerView(){
        rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(this)
        }
    }
}