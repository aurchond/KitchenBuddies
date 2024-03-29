import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:first_app/data_models/kitchen_constraints_model.dart';
import 'package:first_app/data_models/recipe_info_model.dart';
import 'package:first_app/helpers/globals.dart';
import 'package:first_app/screens/meal_session_screens/new_meal_session_screen.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:provider/provider.dart';
import '../data_models/friends_list_model.dart';
import '../data_models/meal_session_steps_request_model.dart';
import 'data_class.dart';
import '../data_models/meal_session_steps_model.dart';

// called from data class to get actual data from the URL
Future<List<MealSessionSteps?>> requestMealSessionSteps(MealSessionStepsRequest? mealSessionStepsRequest) async {

  final body = jsonEncode(mealSessionStepsRequest!.toJson());
  Response? response = await sendPostRequest("RequestMealSessionSteps", body);
  print(response);

  final item = json.decode(response!.body);
  //print("item: " + item);

  List<MealSessionSteps?> allMealSessionSteps = <MealSessionSteps>[];
  for (int i = 0; i < item.length; i++) {
    allMealSessionSteps.add(MealSessionSteps.fromJson(item[i]));
  }

  return allMealSessionSteps; //error check?
}


Future<List<MealSessionSteps?>> getMealSessionSteps(MealSessionStepsRequest? mealSessionStepsRequest) async {
  List<MealSessionSteps?> allMealSessionSteps = <MealSessionSteps>[];
  try {
    final uri = Uri.parse("https://mocki.io/v1/798b07f6-05d4-4c7e-b0a1-b034eb2eb2d9");
    final headers = {HttpHeaders.contentTypeHeader: 'application/json'};
    final response = await http.get(uri, headers: headers);


    // status code is fine
    if (response.statusCode == 200) {
      final item = json.decode(response.body);
      for (int i = 0; i < item.length; i++) {
        allMealSessionSteps.add(MealSessionSteps.fromJson(item[i]));
      }
    } else {
      print("error: " + response.body.toString());
    }
  } catch (e) {
    log(e.toString());
  }
  return allMealSessionSteps; //error check?
}


Future<void> addFriend(String newFriend) async {

  final body = jsonEncode(<String, String>{
    "userEmail": myEmail,
    "newFriend": newFriend
  });

  Response? response = await sendPostRequest("AddFriend", body);
}

Future<void> addSkillLevel(String newSkillLevel) async {

  int intSkill = 2;
  if (newSkillLevel == "Beginner") {
    intSkill = 1;
  } else if (newSkillLevel == "Advanced") {
    intSkill = 3;
  }

  final body = jsonEncode(<String, String>{
    "userEmail": myEmail,
    "skillLevel": intSkill.toString()
  });

  Response? response = await sendPostRequest("AddSkillLevel", body);
}

Future<void> addKitchenConstriants(KitchenConstraints kitchenConstraints) async {

  final body = jsonEncode(kitchenConstraints.toJson());

  Response? response = await sendPostRequest("AddKitchenConstraints", body);
}

Future<void> addUser(String email, String username) async {

  final body = jsonEncode(<String, String>{
    "userEmail": email,
    "skillLevel": "2",
    "userName": username
  });
  Response? response = await sendPostRequest("AddUser", body);
}

Future<RecipeInfo?> requestRecipeByURL(String recipeURL) async {

  final body = jsonEncode(<String, String>{
    "userEmail": myEmail,
    "recipeUrl": recipeURL
  });
  Response? response = await sendPostRequest("RequestRecipeByUrl", body);
  final item = json.decode(response!.body);

  RecipeInfo recipeFromURL = RecipeInfo.fromJson(item);
  return recipeFromURL;
}

Future<RecipeInfo?> requestRecipeByInput(RecipeInfo recipeByText) async {

  final body = jsonEncode(recipeByText.toJson());
  Response? response = await sendPostRequest("RequestRecipeByInput", body);
  final item = json.decode(response!.body);

  RecipeInfo recipeFromURL = RecipeInfo.fromJson(item);
  return recipeFromURL;
}


Future<Response?> sendPostRequest(String route, String body) async{

  try {
    final uri = Uri.http("178.128.227.93:8080",route);
    final headers = {HttpHeaders.contentTypeHeader: 'application/json'};
    final response = await http.post(uri, headers: headers, body: body);
    //return response;  https://www.allrecipes.com/recipe/8489220/chimichurri-chicken/

    // status code is fine
    if (response.statusCode == 200) {
      return response;
    } else {
      print("error: " + response.toString());
    }
  } catch (e) {
    log(e.toString());
  }
}
