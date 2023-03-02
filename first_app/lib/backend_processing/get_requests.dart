import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:first_app/screens/meal_session_screens/new_meal_session_screen.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
import 'data_class.dart';
import '../data_models/meal_session_steps.dart';

// called from data class to get actual data from the URL
Future<String> GetUserSkill() async {
  final queryParameters = {
    "userEmail": "aurchon@gmail.com",
  };

  try {
    // final uri = Uri.http("178.128.227.93:8080","/GetUserSkill", queryParameters);
    // final headers = {HttpHeaders.contentTypeHeader: 'application/json'};
    // final response = await http.get(uri, headers: headers);

    final response = await http.get(
      Uri.parse("https://mocki.io/v1/5a499647-319b-4da8-8e2e-9471d3c12113"),
      headers: {
        HttpHeaders.contentTypeHeader: "application/json",
      },
    );

    // status code is fine
    if (response.statusCode == 200) {
      final item = json.decode(response.body);
      print(item['skillLevel']);

      if (item['skillLevel'] == 1) {
        return "Beginner";
      } else if (item['skillLevel'] == 3) {
        return "Advanced";
      } else {
        return "Intermediate";
      }

      return (item['skillLevel'] >= 0) ? item['skillLevel'] : -1;
    } else {
      print("error");
    }
  } catch (e) {
    log(e.toString());
  }
  return "Intermediate"; //error check?
}
