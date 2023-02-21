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
              padding: const EdgeInsets.all(15),
              child: Column(mainAxisSize: MainAxisSize.min, children: [
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
                        padding: const EdgeInsets.only(top:10),
                        child: ElevatedButton(
                          style: ElevatedButton.styleFrom(
                              textStyle: const TextStyle(fontSize: 18),
                              padding: const EdgeInsets.all(10),
                              backgroundColor: Color.fromARGB(255, 221, 86, 62)),
                          onPressed: () {
                            fcmProvider.sendNotification(
                                token: token,
                                title: "Step Completed",
                                body: "I'm finished with my step!");
                          },
                          child: Text(buttonText),
                        ),
                      ),
                    ]),
              ])),
        ),
      ],
    ),
  );
}
