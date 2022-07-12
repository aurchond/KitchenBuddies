package layout

class Recipe (
    val name: String,
    val cook_time: String,
    val difficulty: String,
    val previous_meal_session: String,
    var expandable: Boolean = true)