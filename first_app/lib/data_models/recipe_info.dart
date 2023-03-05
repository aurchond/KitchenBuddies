import 'package:first_app/helpers/globals.dart';

class RecipeInfo {
  int? _recipeID;
  String? _recipeName;
  List<String>? _ingredientList;
  double? _totalTime;
  String? _lastDateMade;
  List<String>? _instructionList;

  RecipeInfo(
      {int? recipeID,
        String? recipeName,
        List<String>? ingredientList,
        double? totalTime,
        String? lastDateMade,
        List<String>? instructionList}) {
    if (recipeID != null) {
      this._recipeID = recipeID;
    }
    if (recipeName != null) {
      this._recipeName = recipeName;
    }
    if (ingredientList != null) {
      this._ingredientList = ingredientList;
    }
    if (totalTime != null) {
      this._totalTime = totalTime;
    }
    if (lastDateMade != null) {
      this._lastDateMade = lastDateMade;
    }
    if (instructionList != null) {
      this._instructionList = instructionList;
    }
  }

  int? get recipeID => _recipeID;
  set recipeID(int? recipeID) => _recipeID = recipeID;
  String? get recipeName => _recipeName;
  set recipeName(String? recipeName) => _recipeName = recipeName;
  List<String>? get ingredientList => _ingredientList;
  set ingredientList(List<String>? ingredientList) =>
      _ingredientList = ingredientList;
  double? get totalTime => _totalTime;
  set totalTime(double? totalTime) => _totalTime = totalTime;
  String? get lastDateMade => _lastDateMade;
  set lastDateMade(String? lastDateMade) => _lastDateMade = lastDateMade;
  List<String>? get instructionList => _instructionList;
  set instructionList(List<String>? instructionList) =>
      _instructionList = instructionList;

  RecipeInfo.fromJson(Map<String, dynamic> json) {
    _recipeID = json['recipeID'];
    _recipeName = json['recipeName'];
    _ingredientList = json['ingredientList'].cast<String>();
    _totalTime = json['totalTime'];
    _lastDateMade = json['lastDateMade'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userEmail'] = myEmail;
    data['recipeName'] = this._recipeName;
    data['ingredientList'] = this._ingredientList;
    data['totalTime'] = this._totalTime;
    data['lastDateMade'] = this._lastDateMade;
    data['instructionList'] = this._instructionList;
    return data;
  }
}