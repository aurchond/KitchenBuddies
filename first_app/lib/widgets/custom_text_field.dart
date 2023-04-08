import 'package:flutter/src/foundation/key.dart';
import 'package:flutter/src/widgets/container.dart';
import 'package:flutter/src/widgets/framework.dart';

import 'package:flutter/material.dart';

class CustomTextField extends StatelessWidget {
  final TextEditingController controller;
  final IconData iconData;
  final String hintText;
  const CustomTextField(
      {Key? key,
      required this.controller,
      required this.iconData,
      required this.hintText})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.only(top: 10),
        child: Container(
            height: 40,
            decoration: BoxDecoration(
                border: Border.all(color: Colors.deepOrange),
                borderRadius: BorderRadius.circular(20)),
            child: TextField(
              controller: controller,
              decoration: InputDecoration(
                  border: InputBorder.none,
                  prefixIcon: Icon(iconData),
                  hintText: hintText),
            )));
  }
}
