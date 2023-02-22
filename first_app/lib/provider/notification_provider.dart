import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;

class NotificationProvider extends ChangeNotifier {

  sendNotification ({
    required String token, required String title, required String body}) async {
      const postUrl = 'https://fcm.googleapis.com/fcm/send';

      Map<String, dynamic> data;

      data = {
        "registration_ids": [token],
        "collapse_key": "type_a",
        "notification": {
          "title": title, // todo: use a different title for the two types of notifs (send instructions and notify done step)
          "body": body, // currently sending as string
        },
        'data': {
          "title": title,
          "body": body,
          "_id": "this is the payload",
        },
      };

      final response =
          await http.post(Uri.parse(postUrl), body:json.encode(data), headers: {
            'content-type': 'application/json',
            'Authorization': "Key=AAAAw0fvp20:APA91bFyb50GCTtGwizQk90vfZyT7a0bsM5pXgfPj7iJpdqOB-velNK-w3CrDU6JJffTbvgNdiBbEuCKNus8qzYubRpWB4CZowF3Usqf6CreXxGj4B3zkP0d0l9UlGCPJzHlTv_uze2J"
          });
      print(response.body);
  }
}