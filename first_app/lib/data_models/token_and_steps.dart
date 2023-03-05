import 'meal_session_steps.dart';

class TokenAndSteps {
  List<String>? _tokens;
  List<MealSessionSteps>? _mealSessionSteps;

  TokenAndSteps(
      {List<String>? tokens, List<MealSessionSteps>? mealSessionSteps}) {
    if (tokens != null) {
      this._tokens = tokens;
    }
    if (mealSessionSteps != null) {
      this._mealSessionSteps = mealSessionSteps;
    }
  }

  List<String>? get tokens => _tokens;
  set tokens(List<String>? tokens) => _tokens = tokens;
  List<MealSessionSteps>? get mealSessionSteps => _mealSessionSteps;
  set mealSessionSteps(List<MealSessionSteps>? mealSessionSteps) =>
      _mealSessionSteps = mealSessionSteps;

  TokenAndSteps.fromJson(Map<String, dynamic> json) {
    _tokens = json['tokens'].cast<String>();
    if (json['mealSessionSteps'] != null) {
      _mealSessionSteps = <MealSessionSteps>[];
      json['mealSessionSteps'].forEach((v) {
        _mealSessionSteps!.add(new MealSessionSteps.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['tokens'] = this._tokens;
    if (this._mealSessionSteps != null) {
      data['mealSessionSteps'] =
          this._mealSessionSteps!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}