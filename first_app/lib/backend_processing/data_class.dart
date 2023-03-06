import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:first_app/backend_processing/post_requests.dart';
import 'package:first_app/data_models/friends_list_model.dart';
import 'package:first_app/data_models/kitchen_constraints_model.dart';
import 'package:flutter/cupertino.dart';

import '../data_models/meal_session_steps_model.dart';
import '../data_models/meal_session_steps_request_model.dart';
import '../data_models/recipe_info_model.dart';
import 'get_requests.dart';
import '../provider/auth_provider.dart';

class DataClass extends ChangeNotifier {

  bool loading = false;

  // for general GET and POST requests
  String? skillLevel;
  KitchenConstraints? kitchenConstraints;
  FriendsList? friendsList;
  List<RecipeInfo?>? pastRecipes;

  // for creating a new meal session
  MealSessionStepsRequest? mealSessionStepsRequest;
  List<MealSessionSteps?>? allMealSessionSteps;
  MealSessionSteps? mySteps;



  loadHomePage() async {

    // actually show that the screen is loading while the info is fetched
    loading = true;
    skillLevel = await getUserSkill();
    kitchenConstraints = await getKitchenConstraints();
    friendsList = await getFriendsList();
    loading = false;

    notifyListeners();
  }

  // called from instructions screen to get specific instructions for user
  loadMealSessionSteps() async {

    // actually show that the screen is loading while the info is fetched
    loading = true;
    // todo: change back to getMealSessionSteps
    allMealSessionSteps = (await getMealSessionSteps(mealSessionStepsRequest));
    //print(mealSessionSteps);
    loading = false;

    notifyListeners();
  }

  loadAllRecipesPage() async {
    loading = true;
    pastRecipes = await getPastRecipes(); //todo: null check for past recipes
    loading = false;

    print(skillLevel);

    notifyListeners();
  }
}
