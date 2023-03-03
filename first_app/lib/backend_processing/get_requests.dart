import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:first_app/data_models/kitchen_constraints.dart';
import 'package:first_app/screens/meal_session_screens/new_meal_session_screen.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:provider/provider.dart';
import '../data_models/friends_list.dart';
import 'data_class.dart';
import '../data_models/meal_session_steps.dart';

// called from data class to get actual data from the URL
Future<String> getUserSkill() async {
    //sendGetRequest("/GetUserSkill");

  final response = await http.get(
    Uri.parse("https://mocki.io/v1/5a499647-319b-4da8-8e2e-9471d3c12113"),
    headers: {
      HttpHeaders.contentTypeHeader: "application/json",
    },
  );

  final item = json.decode(response.body);
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
  sendGetRequest("/GetKitchenConstraints");
}

Future<FriendsList?> getFriendsList() async {
  final response = await http.get(
    Uri.parse("https://mocki.io/v1/f1ac5265-155e-42d4-b588-9650dbf593c0"),
    headers: {
      HttpHeaders.contentTypeHeader: "application/json",
    },
  );

  final item = json.decode(response.body);

  FriendsList? friendsList = FriendsList.fromJson(item);
  //print(item['skillLevel']);

 return friendsList;
}

Future<Response?> sendGetRequest(String route) async{

  try {
    final queryParameters = {
      "userEmail": "aurchon@gmail.com",
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
