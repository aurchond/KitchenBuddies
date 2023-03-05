import 'meal_session_steps.dart';

class TokenAndStepsCommunication {
  List<String>? _tokens;
  MealSessionSteps? _mealSessionSteps;

  TokenAndStepsCommunication(
      {List<String>? tokens, MealSessionSteps? mealSessionSteps}) {
    if (tokens != null) {
      this._tokens = tokens;
    }
    if (mealSessionSteps != null) {
      this._mealSessionSteps = mealSessionSteps;
    }
  }

  List<String>? get tokens => _tokens;
  set tokens(List<String>? tokens) => _tokens = tokens;
  MealSessionSteps? get mealSessionSteps => _mealSessionSteps;
  set mealSessionSteps(MealSessionSteps? mealSessionSteps) =>
      _mealSessionSteps = mealSessionSteps;

  TokenAndStepsCommunication.fromJson(Map<String, dynamic> json) {
    _tokens = json['tokens'].cast<String>();
    _mealSessionSteps = json['mealSessionSteps'] != null
        ? new MealSessionSteps.fromJson(json['mealSessionSteps'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['tokens'] = this._tokens;
    if (this._mealSessionSteps != null) {
      data['mealSessionSteps'] = this._mealSessionSteps!.toJson();
    }
    return data;
  }
}
