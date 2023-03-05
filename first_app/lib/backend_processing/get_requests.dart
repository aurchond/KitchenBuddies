import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:first_app/data_models/kitchen_constraints_model.dart';
import 'package:first_app/screens/meal_session_screens/new_meal_session_screen.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:provider/provider.dart';
import '../data_models/friends_list.dart';
import '../data_models/recipe_info.dart';
import '../helpers/globals.dart';
import 'data_class.dart';
import '../data_models/meal_session_steps.dart';

// called from data class to get actual data from the URL
Future<String> getUserSkill() async {
    final response = await sendGetRequest("/GetUserSkill");

  // final response = await http.get(
  //   Uri.parse("https://mocki.io/v1/5a499647-319b-4da8-8e2e-9471d3c12113"),
  //   headers: {
  //     HttpHeaders.contentTypeHeader: "application/json",
  //   },
  // );

  final item = json.decode(response!.body);
  //print(item['skillLevel']);

  if (item['skillLevel'] == 1) {
    return "Beginner";
  } else if (item['skillLevel'] == 3) {
    return "Advanced";
  } else {
    return "Intermediate";
  }
}


Future<KitchenConstraints?> getKitchenConstraints() async {
  final response = await sendGetRequest("/GetKitchenConstraints");
  final item = json.decode(response!.body);

  KitchenConstraints? kitchenConstraints = KitchenConstraints.fromJson(item);

  return kitchenConstraints;
}

Future<FriendsList?> getFriendsList() async {
  final response = await sendGetRequest("/GetFriendsList");
  final item = json.decode(response!.body);

  FriendsList? friendsList = FriendsList.fromJson(item);

 return friendsList;
}


Future<List<RecipeInfo?>> getPastRecipes() async {
  final response = await sendGetRequest("/GetPastRecipes");
  final item = json.decode(response!.body);
  List<RecipeInfo?> pastRecipes = <RecipeInfo?>[];

  if (item.length > 0) {
    for (int i=0; i<item.length; i++) {
      print(item[i]);
      pastRecipes.add(RecipeInfo.fromJson(item[i]));
    }
  }

  return pastRecipes;
}

Future<Response?> sendGetRequest(String route) async{

  try {
    final queryParameters = {
      "userEmail": myEmail,
    };

    final uri = Uri.http("178.128.227.93:8080",route, queryParameters);
    final headers = {HttpHeaders.contentTypeHeader: 'application/json'};
    final response = await http.get(uri, headers: headers);
    //return response;


    // status code is fine
    if (response.statusCode == 200) {
      return response;
    } else {
      print("error");
    }
  } catch (e) {
    log(e.toString());
  }
}

