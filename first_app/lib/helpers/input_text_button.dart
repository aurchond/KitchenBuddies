import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget inputTextButton(TextEditingController _controller, String hint, String buttonText, FocusNode _focusNode) {
  return Padding(
      padding: const EdgeInsets.all(10.0),
      child: Row(children: [
        Flexible(
            child: Container(
                decoration: BoxDecoration(
                  color: Colors.deepOrange.shade200,
                  border: Border.all(
                      width: 3, color: Colors.deepOrange.shade300),
                ),
                child: Column(
          children: [
            TextField(
              focusNode: _focusNode,
              controller: _controller,
              decoration: InputDecoration(
                border: OutlineInputBorder(),
                hintText: hint,
                suffixIcon: IconButton(
                  onPressed:
                    _controller.clear,
                    icon: Icon(Icons.
                  clear),
                ),
              ),
            ),
            ElevatedButton(
                onPressed: () {
                  print(_controller.text);
                  _focusNode.unfocus();
                  //TODO: API callback function
                },
                child: Text(buttonText)),
          ],
        )))
      ]));
}
