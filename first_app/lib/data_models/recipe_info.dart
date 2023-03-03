class RecipeInfo {
  String? _recipeName;
  List<String>? _ingredientList;
  double? _totalTime;
  String? _lastDateMade;

  RecipeInfo(
      {String? recipeName,
        List<String>? ingredientList,
        double? totalTime,
        String? lastDateMade}) {
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
  }

  String? get recipeName => _recipeName;
  set recipeName(String? recipeName) => _recipeName = recipeName;
  List<String>? get ingredientList => _ingredientList;
  set ingredientList(List<String>? ingredientList) =>
      _ingredientList = ingredientList;
  double? get completionTime => _totalTime;
  set completionTime(double? completionTime) =>
      _totalTime = completionTime;
  String? get lastTimeMade => _lastDateMade;
  set lastTimeMade(String? lastTimeMade) => _lastDateMade = lastTimeMade;

  RecipeInfo.fromJson(Map<String, dynamic> json) {



    _recipeName = json['recipeName'];
    _ingredientList = json['ingredientList'].cast<String>();
    _totalTime = json['completionTime'];
    _lastDateMade = json['lastTimeMade'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['recipeName'] = this._recipeName;
    data['ingredientList'] = this._ingredientList;
    data['completionTime'] = this._totalTime;
    data['lastTimeMade'] = this._lastDateMade;
    return data;
  }
}