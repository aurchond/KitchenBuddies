class MealSessionSteps {
  String? _userEmail;
  String? _notes;
  List<RecipeStep>? _recipeStep;

  MealSessionSteps(
      {String? userEmail, String? notes, List<RecipeStep>? recipeStep}) {
    if (userEmail != null) {
      this._userEmail = userEmail;
    }
    if (notes != null) {
      this._notes = notes;
    }
    if (recipeStep != null) {
      this._recipeStep = recipeStep;
    }
  }

  String? get userEmail => _userEmail;
  set userEmail(String? userEmail) => _userEmail = userEmail;
  String? get notes => _notes;
  set notes(String? notes) => _notes = notes;
  List<RecipeStep>? get recipeStep => _recipeStep;
  set recipeStep(List<RecipeStep>? recipeStep) => _recipeStep = recipeStep;

  MealSessionSteps.fromJson(Map<String, dynamic> json) {
    _userEmail = json['userEmail'];
    _notes = json['notes'];
    if (json['recipeStep'] != null) {
      _recipeStep = <RecipeStep>[];
      json['recipeStep'].forEach((v) {
        _recipeStep!.add(new RecipeStep.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userEmail'] = this._userEmail;
    data['notes'] = this._notes;
    if (this._recipeStep != null) {
      data['recipeStep'] = this._recipeStep!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class RecipeStep {
  String? _number;
  String? _instructions;
  List<String>? _ingredientsCompleteList;
  String? _dependencyItem;
  String? _nextUserEmail;

  RecipeStep(
      {String? number,
        String? instructions,
        List<String>? ingredientsCompleteList,
        String? dependencyItem,
        String? nextUserEmail}) {
    if (number != null) {
      this._number = number;
    }
    if (instructions != null) {
      this._instructions = instructions;
    }
    if (ingredientsCompleteList != null) {
      this._ingredientsCompleteList = ingredientsCompleteList;
    }
    if (dependencyItem != null) {
      this._dependencyItem = dependencyItem;
    }
    if (nextUserEmail != null) {
      this._nextUserEmail = nextUserEmail;
    }
  }

  String? get number => _number;
  set number(String? number) => _number = number;
  String? get instructions => _instructions;
  set instructions(String? instructions) => _instructions = instructions;
  List<String>? get ingredientsCompleteList => _ingredientsCompleteList;
  set ingredientsCompleteList(List<String>? ingredientsCompleteList) =>
      _ingredientsCompleteList = ingredientsCompleteList;
  String? get dependencyItem => _dependencyItem;
  set dependencyItem(String? dependencyItem) =>
      _dependencyItem = dependencyItem;
  String? get nextUserEmail => _nextUserEmail;
  set nextUserEmail(String? nextUserEmail) => _nextUserEmail = nextUserEmail;

  RecipeStep.fromJson(Map<String, dynamic> json) {
    _number = json['number'];
    _instructions = json['instructions'];
    var ingredientsListLength = json['ingredientQuantity'].length;

    // manually concat quantity and list of ingredients
    _ingredientsCompleteList = List<String>.filled(ingredientsListLength, '');
    for (int i = 0; i < ingredientsListLength; i++) {
      _ingredientsCompleteList?[i] = json['ingredientQuantity'][i].toString() + " " + json['ingredientList'][i];
    }
    _nextUserEmail = json['nextUserEmail'];
  }

  // todo: need to fix ingredientsList so that it can get ingredient list only from ingredientCompleteList
  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['number'] = this._number;
    data['instructions'] = this._instructions;
    data['ingredientsList'] = this._ingredientsCompleteList;
    data['ingredientsQuantity'] = 1;
    data['dependencyItem'] = this._dependencyItem;
    data['nextUserEmail'] = this._nextUserEmail;
    return data;
  }
}