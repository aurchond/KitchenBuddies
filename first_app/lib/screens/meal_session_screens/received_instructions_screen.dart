import 'package:first_app/data_models/data_communication_wrapper_model.dart';
import 'package:first_app/helpers/friend_tokens.dart';
import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../data_models/meal_session_steps_model.dart';
import '../../helpers/globals.dart';
import '../../provider/notification_provider.dart';

class ReceivedInstructionScreen extends StatelessWidget {
  final RemoteMessage message;
  const ReceivedInstructionScreen({Key? key, required this.message})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    final fcmProvider = Provider.of<NotificationProvider>(context);

    Map<String, dynamic> dataMap = message.data;

    //throwing error about quantity length when it is not a list
    DataCommunicationWrapper? dataCommunicationWrapper =
        DataCommunicationWrapper.fromJson(dataMap);
    Body? receivedTokenAndSteps = dataCommunicationWrapper.body;
    MealSessionSteps? thisUserSteps = receivedTokenAndSteps?.mealSessionSteps;

    Map<String, dynamic> body = json.decode(dataMap["body"]);
    //print("map " + dataMap["body"]);

    List<String> friendTokenList = removeMyTokenFromList(
        receivedTokenAndSteps?.tokens ?? [],
        receivedTokenAndSteps?.receiversToken ?? "");
    print("friendTokenList: " + friendTokenList.toString());

    final List<bool> selected =
        List.generate(thisUserSteps?.recipeSteps?.length ?? 0, (i) => true);

    return Scaffold(
      appBar: AppBar(title: const Text("Received Instructions")),
      body: Container(
          padding: EdgeInsets.all(20),
          child: Center(
            child:
            Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              // display the user's email at the top
              // Container(
              //   margin: EdgeInsets.all(20),
              //   child: Text(
              //     //email,
              //     thisUserSteps?.userEmail ?? "",
              //     style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
              //   ),
              // ),
              StatefulBuilder(
                  builder: (BuildContext context, StateSetter setState) {
                return Expanded(
                    child: Column(children: [
                  Expanded(
                      child: new ListView.builder(
                          itemCount: thisUserSteps?.recipeSteps?.length ?? 0,
                          itemBuilder: (BuildContext context, int index) {
                            return Padding(
                              padding: const EdgeInsets.only(bottom: 15),
                              child: Row(
                                children: [
                                  Flexible(
                                      child: InkWell(
                                    onTap: () {
                                      setState(() {
                                        selected[index] = !selected[index];
                                      });
                                    },
                                    child: Container(
                                      decoration: BoxDecoration(
                                          borderRadius:
                                              BorderRadius.circular(15),
                                          color: selected[index]
                                              ? Colors.deepOrange.shade200
                                              : Colors.grey.shade300,
                                          border: Border.all(
                                              width: 3,
                                              color: selected[index]
                                                  ? Colors.deepOrange.shade300
                                                  : Colors.grey.shade400)),
                                      padding: const EdgeInsets.all(15),
                                      child: Wrap(
                                          alignment: WrapAlignment.spaceBetween,
                                          runAlignment:
                                              WrapAlignment.spaceBetween,
                                          children: [
                                            Text(
                                                (thisUserSteps
                                                            ?.recipeSteps?[
                                                                index]
                                                            .number
                                                            .toString() ??
                                                        "") +
                                                    ". " +
                                                    (thisUserSteps
                                                            ?.recipeSteps?[
                                                                index]
                                                            .instructions ??
                                                        "") +
                                                    " (" +
                                                    (thisUserSteps
                                                            ?.recipeSteps?[
                                                                index]
                                                            .ingredientsCompleteList
                                                            ?.join(', ') ??
                                                        "") +
                                                    ")",
                                                style: TextStyle(
                                                  fontSize: 18,
                                                  fontWeight: FontWeight.w400,
                                                )),
                                            Padding(
                                              padding: const EdgeInsets.only(
                                                  top: 10),
                                              child: Center(
                                                child: ElevatedButton(
                                                  style: ElevatedButton
                                                      .styleFrom(
                                                          textStyle:
                                                              const TextStyle(
                                                                  fontSize: 18),
                                                          padding:
                                                              const EdgeInsets
                                                                  .all(10),
                                                          backgroundColor:
                                                              selected[index]
                                                                  ? Colors
                                                                      .deepOrange
                                                                  : Colors
                                                                      .grey),
                                                  onPressed: (!selected[index])
                                                      ? null
                                                      : () {
                                                          // if it's the host they use a map from the regular instructions screen
                                                          //if you have moved past this step, don't send a notification
                                                          for (int i = 0;
                                                              i <
                                                                  (friendTokenList
                                                                          .length ??
                                                                      0);
                                                              i++) {
                                                            fcmProvider.sendNotification(
                                                                token:
                                                                    friendTokenList[
                                                                        i],
                                                                title:
                                                                    "Step Blocked",
                                                                body: "I'm blocked on step " +
                                                                    (thisUserSteps
                                                                            ?.recipeSteps?[
                                                                                index]
                                                                            .number
                                                                            .toString() ??
                                                                        "") +
                                                                    "!",
                                                                isBlocked:
                                                                    true);
                                                          }
                                                        },
                                                  child: Text(
                                                    "I'm blocked on step " +
                                                        (thisUserSteps
                                                                ?.recipeSteps?[
                                                                    index]
                                                                .number
                                                                .toString() ??
                                                            "") +
                                                        "!",
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ]),
                                    ),
                                  )),
                                ],
                              ),
                            );
                          })),
                ]));
              }),
            ]),
          )),
    );
  }
}
