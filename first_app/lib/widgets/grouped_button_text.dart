import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget groupedButtonText(
    String text, String buttonText, String token, final fcmProvider) {
  return Padding(
    padding: const EdgeInsets.all(20.0),
    child: Row(
      children: [
        Flexible(
          child: Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(10),
                color: Color.fromARGB(255, 255, 173, 135),
                border: Border.all(
                    width: 5, color: Color.fromARGB(255, 228, 122, 112)),
              ),
              padding: const EdgeInsets.all(20),
              child: Column(mainAxisSize: MainAxisSize.min, children: [
                Wrap(children: [
                  Text(
                    text,
                    style: TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.w400,
                    ),
                  ),
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(
                        textStyle: const TextStyle(fontSize: 20),
                        padding: const EdgeInsets.all(10)),
                    onPressed: () {
                      fcmProvider.sendNotification(
                          token: token,
                          title: "Step Completed",
                          body: "I'm finished with my step!");
                    },
                    child: Text(buttonText),
                  ),
                ]),
              ])),
        ),
      ],
    ),
  );
}
