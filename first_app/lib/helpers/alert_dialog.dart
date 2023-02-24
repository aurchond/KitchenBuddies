import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Future<void> alertDialog(BuildContext context, Function() buttonCallback, Function(String value) callback, TextEditingController _controller, String title, String hint) async {
  return showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(title),
          content: TextField(
            minLines:20,
            maxLines: null,
            keyboardType: TextInputType.multiline,
            onChanged: (value) {
              callback(value);
              },
              controller: _controller,
              decoration: InputDecoration(hintText:
             hint),
          ),
          actions: <Widget>[
            ElevatedButton(
              child: Text('CANCEL'),
              onPressed: buttonCallback,
            ),
            ElevatedButton(
              child: Text('OK'),
              onPressed: buttonCallback,
            ),
          ],
        );
      });
}