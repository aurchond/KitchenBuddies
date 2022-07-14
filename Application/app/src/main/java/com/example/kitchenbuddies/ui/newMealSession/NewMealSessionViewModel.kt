package com.example.kitchenbuddies.ui.newMealSession

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import layout.Recipe

class NewMealSessionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }


//    private var _recipeList = MutableLiveData<ArrayList<Recipe>()>().apply {
//        Recipe(
//            "Norvasc",
//            "40 mg",
//            "Refill required---------- DO OTHIS and don't look back on it yuh",
//            "Directions: Take daily at 9:00am (on empty stomach)."
//        )
//    }


    val text: LiveData<String> = _text
}