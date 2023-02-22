import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

// Widget to group together texts and buttons, such that after clicking the button
// a notification can be sent
Widget groupedButtonText(
    String text, String buttonText, String token, final fcmProvider) {
  return Padding(
    padding: const EdgeInsets.only(bottom: 15),
    child: Row(
      children: [
        Flexible(
          child: Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(15),
                color: Colors.deepOrange.shade200,
                border: Border.all(width: 3, color: Colors.deepOrange.shade300),
              ),
              padding: const EdgeInsets.all(15),
              child: Column(children: [
                Wrap(
                    alignment: WrapAlignment.spaceBetween,
                    runAlignment: WrapAlignment.spaceBetween,
                    children: [
                      Text(
                        text,
                        style: TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.w400,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top: 10),
                        child: Center(
                          child: ElevatedButton(
                            style: ElevatedButton.styleFrom(
                                textStyle: const TextStyle(fontSize: 18),
                                padding: const EdgeInsets.all(10),
                                backgroundColor: Colors.deepOrange),
                            onPressed: () {
                              fcmProvider.sendNotification(
                                  token: token,
                                  title: "Step Completed",
                                  body: "I'm finished with my step!");
                            },
                            child: Text(buttonText),
                          ),
                        ),
                      ),
                    ]),
              ])),
        ),
      ],
    ),
  );
}
