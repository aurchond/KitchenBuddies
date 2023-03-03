import 'package:first_app/backend_processing/post_requests.dart';
import 'package:first_app/data_models/friends_list.dart';
import 'package:first_app/data_models/kitchen_constraints.dart';
import 'package:flutter/cupertino.dart';

import '../data_models/meal_session_steps.dart';
import '../data_models/recipe_info.dart';
import 'get_requests.dart';

class DataClass extends ChangeNotifier {
  MealSessionSteps? mealSessionSteps;
  String? skillLevel;
  KitchenConstraints? kitchenConstraints;
  FriendsList? friendsList;
  List<RecipeInfo?>? pastRecipes;

  bool loading = false;

  // called from instructions screen to get specific instructions for user
  loadMealSessionSteps(String emailToFind) async {

    // actually show that the screen is loading while the info is fetched
    loading = true;
    mealSessionSteps = (await getMealSessionSteps(emailToFind));
    loading = false;

    notifyListeners();
  }

  loadHomePage() async {

    // actually show that the screen is loading while the info is fetched
    loading = true;
    skillLevel = await getUserSkill();
    kitchenConstraints = await getKitchenConstraints();
    friendsList = await getFriendsList();
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
