package com.example.unittestingexample_junit4.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unittestingexample_junit4.data.local.ShoppingItem
import com.example.unittestingexample_junit4.data.remote.responses.ImageResponse
import com.example.unittestingexample_junit4.repositories.ShoppingRepository
import com.example.unittestingexample_junit4.util.Constants
import com.example.unittestingexample_junit4.util.Event
import com.example.unittestingexample_junit4.util.Resource
import kotlinx.coroutines.launch

class ShoppingViewModel @ViewModelInject constructor(
        private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images : LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currImageUrl = MutableLiveData<String>()
    val currImageUrl : LiveData<String> = _currImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus : LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurrImageUrl(url : String){
        _currImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name : String, amountString : String, priceString : String){
        if(name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()){
            _insertShoppingItemStatus.postValue(Event(Resource.error("The Fields must not be empty", null)))
            return
        }
        if(name.length > Constants.MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error("The name of the item +" +
                    "not exceed ${Constants.MAX_NAME_LENGTH} characters", null)))
            return
        }
        if(priceString.length > Constants.MAX_PRICE_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error("The price of the item +" +
                    "not exceed ${Constants.MAX_PRICE_LENGTH} characters", null)))
            return
        }

        val amount = try{
            amountString.toInt()
        }catch (e : Exception){
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter a valid amount", null)))
            return
        }

        val shoppingItem = ShoppingItem(name, amount, priceString.toFloat(), _currImageUrl.value ?: "")
        insertShoppingItemIntoDb(shoppingItem)
        setCurrImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery : String){
        if(imageQuery.isEmpty()){
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }

}