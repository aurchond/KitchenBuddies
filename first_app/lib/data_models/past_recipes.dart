class PastRecipes {
  String? _recipeName;
  List<String>? _ingredientList;
  double? _completionTime;
  String? _lastTimeMade;

  PastRecipes(
      {String? recipeName,
        List<String>? ingredientList,
        double? completionTime,
        String? lastTimeMade}) {
    if (recipeName != null) {
      this._recipeName = recipeName;
    }
    if (ingredientList != null) {
      this._ingredientList = ingredientList;
    }
    if (completionTime != null) {
      this._completionTime = completionTime;
    }
    if (lastTimeMade != null) {
      this._lastTimeMade = lastTimeMade;
    }
  }

  String? get recipeName => _recipeName;
  set recipeName(String? recipeName) => _recipeName = recipeName;
  List<String>? get ingredientList => _ingredientList;
  set ingredientList(List<String>? ingredientList) =>
      _ingredientList = ingredientList;
  double? get completionTime => _completionTime;
  set completionTime(double? completionTime) =>
      _completionTime = completionTime;
  String? get lastTimeMade => _lastTimeMade;
  set lastTimeMade(String? lastTimeMade) => _lastTimeMade = lastTimeMade;

  PastRecipes.fromJson(Map<String, dynamic> json) {
    _recipeName = json['recipeName'];
    _ingredientList = json['ingredientList'].cast<String>();
    _completionTime = json['completionTime'];
    _lastTimeMade = json['lastTimeMade'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['recipeName'] = this._recipeName;
    data['ingredientList'] = this._ingredientList;
    data['completionTime'] = this._completionTime;
    data['lastTimeMade'] = this._lastTimeMade;
    return data;
  }
}