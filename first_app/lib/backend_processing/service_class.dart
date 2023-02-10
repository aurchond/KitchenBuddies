import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
import 'data_class.dart';
import 'data_model.dart';

Future<UserRecipeDetails?> getSinglePostData() async {
  UserRecipeDetails? result;
  try {
    final response = await http.get(
      Uri.parse("https://mocki.io/v1/cf23d25e-1a8f-4c34-94c0-363815d7a3fc"),
      headers: {
        HttpHeaders.contentTypeHeader: "application/json",
      },);

    // status code is fine
    if (response.statusCode == 200) {
      final item = json.decode(response.body);
      print(item);
      result = UserRecipeDetails.fromJson(item[0]); //iterate thru list
      //print(response.body);
    } else {
      print("error");
    }
  } catch (e) {
    log(e.toString());
  }
  return result;
}