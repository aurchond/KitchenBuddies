import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:first_app/helpers/globals.dart';

Future<void> alertDialog(
    BuildContext context,
    Function() buttonCallback,
    Function() parseCallback,
    Function(String value) callback,
    TextEditingController _controller,
    String title,
    String hint) async {
  return showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.all(Radius.circular(10.0))),
          insetPadding: EdgeInsets.all(10),
          title: Text(title),
          content: Center(
              child: SizedBox(
                  width: MediaQuery.of(context).size.width,
                  child: TextField(
                    minLines: 20,
                    maxLines: null,
                    keyboardType: TextInputType.multiline,
                    onChanged: (value) {
                      callback(value);
                    },
                    controller: _controller,
                    decoration: InputDecoration(
                        hintText: hint,
                        suffixIcon: IconButton(
                            onPressed: _controller.clear,
                            icon: Icon(Icons.clear))),
                  ))),
          actions: <Widget>[
            ElevatedButton(
              child: Text('CANCEL'),
              onPressed: buttonCallback,
            ),
            ElevatedButton(
              child: Text('OK'),
              onPressed: parseCallback,
            ),
          ],
        );
      });
}
