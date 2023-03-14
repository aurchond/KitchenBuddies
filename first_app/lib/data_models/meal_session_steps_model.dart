class MealSessionSteps {
  String? _userEmail;
  List<String>? _notes;
  List<RecipeStep>? _recipeSteps;
  MealSessionSteps(
      {String? userEmail, List<String>? notes, List<RecipeStep>? recipeSteps}) {
    if (userEmail != null) {
      this._userEmail = userEmail;
    }
    if (notes != null) {
      this._notes = notes;
    }
    if (recipeSteps != null) {
      this._recipeSteps = recipeSteps;
    }
  }
  String? get userEmail => _userEmail;
  set userEmail(String? userEmail) => _userEmail = userEmail;
  List<String>? get notes => _notes;
  set notes(List<String>? notes) => _notes = notes;
  List<RecipeStep>? get recipeSteps => _recipeSteps;
  set recipeSteps(List<RecipeStep>? recipeSteps) => _recipeSteps = recipeSteps;
  MealSessionSteps.fromJson(Map<String, dynamic> json) {
    _userEmail = json['userEmail'];
    _notes = json['notes'].cast<String>();
    if (json['recipeSteps'] != null) {
      _recipeSteps = <RecipeStep>[];
      json['recipeSteps'].forEach((v) {
        _recipeSteps!.add(new RecipeStep.fromJson(v));
      });
    }
  }
  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userEmail'] = this._userEmail;
    data['notes'] = this._notes;
    if (this._recipeSteps != null) {
      data['recipeSteps'] = this._recipeSteps!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class RecipeStep {
  String? _number;
  String? _instructions;
  List<String>? _ingredientsCompleteList;

  RecipeStep(
      {String? number,
        String? instructions,
        List<String>? ingredientsCompleteList,}){
    if (number != null) {
      this._number = number;
    }
    if (instructions != null) {
      this._instructions = instructions;
    }
    if (ingredientsCompleteList != null) {
      this._ingredientsCompleteList = ingredientsCompleteList;
    }
  }

  String? get number => _number;
  set number(String? number) => _number = number;
  String? get instructions => _instructions;
  set instructions(String? instructions) => _instructions = instructions;
  List<String>? get ingredientsCompleteList => _ingredientsCompleteList;
  set ingredientsCompleteList(List<String>? ingredientsCompleteList) =>
      _ingredientsCompleteList = ingredientsCompleteList;

  RecipeStep.fromJson(Map<String, dynamic> json) {
    _number = json['number'];
    _instructions = json['instructions'];
    var ingredientsListLength = json['ingredientList'].length;

    // manually concat quantity and list of ingredients
    _ingredientsCompleteList = List<String>.filled(ingredientsListLength, '');
    for (int i = 0; i < ingredientsListLength; i++) {
      //_ingredientsCompleteList?[i] = json['ingredientQuantity'][i].toString() + " " + json['ingredientList'][i];
      _ingredientsCompleteList?[i] = json['ingredientList'][i];
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['number'] = this._number;
    data['instructions'] = this._instructions;
    data['ingredientList'] = this._ingredientsCompleteList;
    //data['ingredientQuantity'] = [1];
    return data;
  }
}