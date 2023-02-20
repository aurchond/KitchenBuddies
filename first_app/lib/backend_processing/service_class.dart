import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
import 'data_class.dart';
import '../data_models/meal_session_steps.dart';

Future<MealSessionSteps?> getSinglePostData(int jsonIndex) async {
  MealSessionSteps? result;
  try {
    final response = await http.get(
      Uri.parse("https://mocki.io/v1/d4f1a7b6-52d2-49a1-86ef-eba719006078"),
      headers: {
        HttpHeaders.contentTypeHeader: "application/json",
      },
    );

    // status code is fine
    if (response.statusCode == 200) {
      final item = json.decode(response.body);
      print(item);
      result = MealSessionSteps.fromJson(item[jsonIndex]); //iterate thru list
      //print(response.body);
    } else {
      print("error");
    }
  } catch (e) {
    log(e.toString());
  }
  return result;
}
