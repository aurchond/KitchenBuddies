import 'package:first_app/backend_processing/service_class.dart';
import 'package:flutter/cupertino.dart';

import '../data_models/meal_session_steps.dart';
import 'get_requests.dart';

class DataClass extends ChangeNotifier {
  MealSessionSteps? post;
  String? skillLevel;
  bool loading = false;

  // called from instructions screen to get specific instructions for user
  getPostData(String emailToFind) async {

    // actually show that the screen is loading while the info is fetched
    loading = true;
    post = (await getSinglePostData(emailToFind));
    loading = false;

    notifyListeners();
  }

  loadHomePage() async {

    // actually show that the screen is loading while the info is fetched
    loading = true;
    skillLevel = (await GetUserSkill());
    loading = false;

    print(skillLevel);

    notifyListeners();
  }
}
