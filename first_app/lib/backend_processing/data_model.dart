class UserRecipeDetails {
  String? _userEmail;
  List<RecipeStep>? _recipeStep;

  UserRecipeDetails({String? userEmail, List<RecipeStep>? recipeStep}) {
    if (userEmail != null) {
      this._userEmail = userEmail;
    }
    if (recipeStep != null) {
      this._recipeStep = recipeStep;
    }
  }

  String? get userEmail => _userEmail;
  set userEmail(String? userEmail) => _userEmail = userEmail;
  List<RecipeStep>? get recipeStep => _recipeStep;
  set recipeStep(List<RecipeStep>? recipeStep) => _recipeStep = recipeStep;

  UserRecipeDetails.fromJson(Map<String, dynamic> json) {
    _userEmail = json['userEmail'];
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
    if (this._recipeStep != null) {
      data['recipeStep'] = this._recipeStep!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class RecipeStep {
  String? _number;
  String? _instructions;
  List<String>? _ingredientList;
  List<int>? _ingredientQuantity;
  String? _dependencyItem;
  String? _nextUserEmail;

  RecipeStep(
      {String? number,
        String? instructions,
        List<String>? ingredientList,
        List<int>? ingredientQuantity,
        String? dependencyItem,
        String? nextUserEmail}) {
    if (number != null) {
      this._number = number;
    }
    if (instructions != null) {
      this._instructions = instructions;
    }
    if (ingredientList != null) {
      this._ingredientList = ingredientList;
    }
    if (ingredientQuantity != null) {
      this._ingredientQuantity = ingredientQuantity;
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
  List<String>? get ingredientList => _ingredientList;
  set ingredientList(List<String>? ingredientList) =>
      _ingredientList = ingredientList;
  List<int>? get ingredientQuantity => _ingredientQuantity;
  set ingredientQuantity(List<int>? ingredientQuantity) =>
      _ingredientQuantity = ingredientQuantity;
  String? get dependencyItem => _dependencyItem;
  set dependencyItem(String? dependencyItem) =>
      _dependencyItem = dependencyItem;
  String? get nextUserEmail => _nextUserEmail;
  set nextUserEmail(String? nextUserEmail) => _nextUserEmail = nextUserEmail;

  RecipeStep.fromJson(Map<String, dynamic> json) {
    _number = json['number'];
    _instructions = json['instructions'];
    _ingredientList = json['ingredientList'].cast<String>();
    _ingredientQuantity = json['ingredientQuantity'].cast<int>();
    _dependencyItem = json['dependencyItem'];
    _nextUserEmail = json['nextUserEmail'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['number'] = this._number;
    data['instructions'] = this._instructions;
    data['ingredientList'] = this._ingredientList;
    data['ingredientQuantity'] = this._ingredientQuantity;
    data['dependencyItem'] = this._dependencyItem;
    data['nextUserEmail'] = this._nextUserEmail;
    return data;
  }
}