import 'package:first_app/backend_processing/service_class.dart';
import 'package:flutter/cupertino.dart';

import '../data_models/meal_session_steps.dart';

class DataClass extends ChangeNotifier {
  MealSessionSteps? post;
  bool loading = false;

  // called from instructions screen to get specific instructions for user
  getPostData(String emailToFind) async {

    // actually show that the screen is loading while the info is fetched
    loading = true;
    post = (await getSinglePostData(emailToFind));
    loading = false;

    notifyListeners();
  }
}
