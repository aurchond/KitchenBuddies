import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:first_app/screens/new_meal_session.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
import 'data_class.dart';
import '../data_models/meal_session_steps.dart';

Future<MealSessionSteps?> getSinglePostData(String emailToFind) async {
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
