import 'meal_session_steps.dart';
class Data {
  String? _title;
  TokenAndStepsCommunication? _tokenAndStepsCommunication;

  Data({String? title, TokenAndStepsCommunication? tokenAndStepsCommunication}) {
    if (title != null) {
      this._title = title;
    }
    if (tokenAndStepsCommunication != null) {
      this._tokenAndStepsCommunication = tokenAndStepsCommunication;
    }
  }

  String? get title => _title;
  set title(String? title) => _title = title;
  TokenAndStepsCommunication? get tokenAndStepsCommunication => _tokenAndStepsCommunication;
  set body(TokenAndStepsCommunication? tokenAndStepsCommunication) => _tokenAndStepsCommunication = tokenAndStepsCommunication;

  Data.fromJson(Map<String, dynamic> json) {
    _title = json['title'];
    _tokenAndStepsCommunication = json['body'] != null ? new TokenAndStepsCommunication.fromJson(json['body']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['title'] = this._title;
    if (this._tokenAndStepsCommunication!= null) {
      data['body'] = this._tokenAndStepsCommunication!.toJson();
    }
    return data;
  }
}

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
