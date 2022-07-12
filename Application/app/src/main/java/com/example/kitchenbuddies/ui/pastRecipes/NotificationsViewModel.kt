package com.example.kitchenbuddies.ui.pastRecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PastRecipesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Past Recipes Fragment"
    }
    val text: LiveData<String> = _text
}