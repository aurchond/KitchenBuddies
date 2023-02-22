import 'package:flutter/src/foundation/key.dart';
import 'package:flutter/src/widgets/container.dart';
import 'package:flutter/src/widgets/framework.dart';

import 'package:flutter/material.dart';

class AuthButton extends StatelessWidget {
  final IconData iconData;
  final String title;
  final Function()? onTap;
  const AuthButton(
      {Key? key, required this.iconData, required this.title, this.onTap})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.only(top: 10),
        child: InkWell(
            onTap: onTap,
            child: Container(
                height: 40,
                decoration: BoxDecoration(
                    border: Border.all(color: Colors.green),
                    borderRadius: BorderRadius.circular(20)),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(iconData),
                    Text(title),
                  ],
                ))));
  }
}
