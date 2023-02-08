import 'dart:convert';
import 'dart:developer';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
import 'data_class.dart';
import 'data_model.dart';

Future<UserDetails?> getSinglePostData() async {
  UserDetails? result;
  try {
    final response = await http.get(
      Uri.parse("https://mocki.io/v1/29261a25-09fb-4421-86f2-8cc623975f29"),
      headers: {
        HttpHeaders.contentTypeHeader: "application/json",
      },);

    // status code is fine
    if (response.statusCode == 200) {
      final item = json.decode(response.body);
      result = UserDetails.fromJson(item);
    } else {
      print("error");
    }
  } catch (e) {
    log(e.toString());
  }
  return result;
}