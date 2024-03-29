import 'dart:convert';

import 'package:first_app/data_models/meal_session_steps_model.dart';

class DataCommunicationWrapper {
  String? _title;
  Body? _body;

  DataCommunicationWrapper({String? title, Body? body}) {
    if (title != null) {
      this._title = title;
    }
    if (body != null) {
      this._body = body;
    }
  }

  String? get title => _title;
  set title(String? title) => _title = title;
  Body? get body => _body;
  set body(Body? body) => _body = body;

  DataCommunicationWrapper.fromJson(Map<String, dynamic> json) {
    _title = json['title'];
    _body = json['body'] != null ? new Body.fromJson(jsonDecode(json["body"]) as Map<String, dynamic>) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['title'] = this._title;
    if (this._body != null) {
      data['body'] = this._body!.toJson();
    }
    return data;
  }
}

class Body {
  String? _receiversToken;
  List<String>? _tokens;
  MealSessionSteps? _mealSessionSteps;

  Body(
      {String? receiversToken,
        List<String>? tokens,
        MealSessionSteps? mealSessionSteps}) {
    if (receiversToken != null) {
      this._receiversToken = receiversToken;
    }
    if (tokens != null) {
      this._tokens = tokens;
    }
    if (mealSessionSteps != null) {
      this._mealSessionSteps = mealSessionSteps;
    }
  }

  String? get receiversToken => _receiversToken;
  set receiversToken(String? receiversToken) =>
      _receiversToken = receiversToken;
  List<String>? get tokens => _tokens;
  set tokens(List<String>? tokens) => _tokens = tokens;
  MealSessionSteps? get mealSessionSteps => _mealSessionSteps;
  set mealSessionSteps(MealSessionSteps? mealSessionSteps) =>
      _mealSessionSteps = mealSessionSteps;

  Body.fromJson(Map<String, dynamic> json) {
    _receiversToken = json['receiversToken'];
    _tokens = json['tokens'].cast<String>();
    _mealSessionSteps = json['mealSessionSteps'] != null
        ? new MealSessionSteps.fromJson(json['mealSessionSteps'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['receiversToken'] = this._receiversToken;
    data['tokens'] = this._tokens;
    if (this._mealSessionSteps != null) {
      data['mealSessionSteps'] = this._mealSessionSteps!.toJson();
    }
    return data;
  }
}