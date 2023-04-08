import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget inputTextButton(TextEditingController _controller, String hint, String buttonText, FocusNode _focusNode, Function(FocusNode _focusNode, TextEditingController _controller) _onPressed) {
  return Padding(
      padding: const EdgeInsets.all(10.0),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.deepOrange.shade200,
          border: Border.all(
              width: 3, color: Colors.deepOrange.shade300),
        ),
      child: Row(
        children: [
            Expanded(child: TextField(
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
            )),
            SizedBox(
              width: 120, height: 60, child:
            ElevatedButton(
                onPressed: () {
                  _onPressed(_focusNode, _controller);
                },
                child: Text(buttonText)),
            )],
        )));
}
