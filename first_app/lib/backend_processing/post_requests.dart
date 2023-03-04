import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:first_app/data_models/kitchen_constraints.dart';
import 'package:first_app/data_models/recipe_info.dart';
import 'package:first_app/helpers/globals.dart';
import 'package:first_app/screens/meal_session_screens/new_meal_session_screen.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:provider/provider.dart';
import 'data_class.dart';
import '../data_models/meal_session_steps.dart';

// called from data class to get actual data from the URL
Future<MealSessionSteps?> getMealSessionSteps(String emailToFind) async {
  MealSessionSteps dummy = new MealSessionSteps();
  try {
    final response = await http.get(
      Uri.parse("https://mocki.io/v1/b05ef8e6-efa7-415c-a185-2971ec2f80d4"),
      headers: {
        HttpHeaders.contentTypeHeader: "application/json",
      },
    );

    // status code is fine
    if (response.statusCode == 200) {
      final item = json.decode(response.body);
      for (int i = 0; i < item.length; i++) {
        MealSessionSteps? thisUserSteps = MealSessionSteps.fromJson(item[i]);

        // emailToFind is the user's own email
        if (thisUserSteps.userEmail == emailToFind) {
          return thisUserSteps;
        }
      }
    } else {
      print("error");
    }
  } catch (e) {
    log(e.toString());
  }
  return dummy; //error check?
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
    "username": username
  });
  Response? response = await sendPostRequest("AddUser", body);
}


// todo: test this out
Future<RecipeInfo?> requestRecipeByURL(String recipeURL) async {

  // todo: take out mock data
  final response = await http.get(
    Uri.parse("https://mocki.io/v1/baaae979-218a-4def-8be5-ce56596f36ff"),
    headers: {
      HttpHeaders.contentTypeHeader: "application/json",
    },
  );

  // final body = jsonEncode(<String, String>{
  //   "userEmail": myEmail,
  //   "recipeUrl": recipeURL
  // });
  // Response? response = await sendPostRequest("RequestRecipeByURL", body);
  final item = json.decode(response!.body);


  RecipeInfo recipeFromURL = RecipeInfo.fromJson(item);
  return recipeFromURL;
}


Future<Response?> sendPostRequest(String route, String body) async{

  try {
    final uri = Uri.http("178.128.227.93:8080",route);
    final headers = {HttpHeaders.contentTypeHeader: 'application/json'};
    final response = await http.post(uri, headers: headers, body: body);
    //return response;

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
