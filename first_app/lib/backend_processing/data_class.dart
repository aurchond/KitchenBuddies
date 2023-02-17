import 'package:first_app/backend_processing/service_class.dart';
import 'package:flutter/cupertino.dart';

import '../data_models/meal_session_steps.dart';

class DataClass extends ChangeNotifier {
  MealSessionSteps? post;
  bool loading = false;

  getPostData(int jsonIndex) async {
    loading = true;
    post = (await getSinglePostData(jsonIndex));
    loading = false;

    notifyListeners();
  }
}