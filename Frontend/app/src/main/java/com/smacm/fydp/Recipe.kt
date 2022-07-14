package com.smacm.fydp

data class Recipe (
    var name: String,
    var cook_time: String,
    var difficulty: String,
    var previous_meal_session: String,
    var expandable: Boolean = true
)