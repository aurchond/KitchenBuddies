import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

// Widget to group together texts and buttons, such that after clicking the button
// a notification can be sent
Widget groupedButtonText(bool _selected,
    String text, String buttonText, Map<String, String>? friendsTokenMap, List<String>? friendsTokenList, bool isHost, final fcmProvider, setState) {
  return Padding(
    padding: const EdgeInsets.only(bottom: 15),
    child: Row(
      children: [
        Flexible(
          child: Container(
            decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(15),
                color: _selected ? Colors.deepOrange.shade200 : Colors.grey.shade300,
                border: Border.all(width: 3, color: _selected ? Colors.deepOrange.shade300 : Colors.grey.shade400)
            ),
            padding: const EdgeInsets.all(15),
            child:
            Wrap(
                alignment: WrapAlignment.spaceBetween,
                runAlignment: WrapAlignment.spaceBetween,
                children: [ ListTile(title:
                Text(
                    text,
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.w400,
                    )),
                  onTap: () {
                    setState(() {
                      _selected = !_selected;
                    });
                  },
                ),
                  Padding(
                    padding: const EdgeInsets.only(top: 10),
                    child: Center(
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                            textStyle: const TextStyle(fontSize: 18),
                            padding: const EdgeInsets.all(10),
                            backgroundColor: _selected ? Colors.deepOrange: Colors.grey),
                        onPressed: () {

                          // if it's the host they use a map from the regular instructions screen
                          if (isHost && friendsTokenMap != null) {
                            for (var email in friendsTokenMap.keys) {
                              fcmProvider.sendNotification(
                                  token: friendsTokenMap[email],
                                  title: "Step Blocked",
                                  body: "I'm blocked on my step!");}
                          }

                          // if it's the non-hosts/friends then use a list from received instructinos
                          else {
                            for (int i = 0; i < (friendsTokenList?.length ?? 0); i++) {
                              fcmProvider.sendNotification(
                                  token: friendsTokenList?[i],
                                  title: "Step Blocked",
                                  body: "I'm blocked on my step!");}
                          }


                        },
                        child: Text(buttonText),
                      ),
                    ),
                  ),
                ]),
          ),
        ),
      ],
    ),
  );
}