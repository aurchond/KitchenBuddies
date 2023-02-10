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
      Uri.parse("https://mocki.io/v1/041c7ac2-1d75-453a-82ec-be5637f4cb16"),
      headers: {
        HttpHeaders.contentTypeHeader: "application/json",
      },);

    // status code is fine
    if (response.statusCode == 200) {
      final item = json.decode(response.body);
      result = UserRecipeDetails.fromJson(item);
      print(response.body);
    } else {
      print("error");
    }
  } catch (e) {
    log(e.toString());
  }
  return result;
}