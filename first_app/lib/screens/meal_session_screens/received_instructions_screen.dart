import 'package:first_app/data_models/data_communication_wrapper_model.dart';
import 'package:first_app/helpers/friend_tokens.dart';
import 'package:first_app/helpers/string_extension.dart';
import 'package:flutter/cupertino.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../data_models/meal_session_steps_model.dart';
import '../../helpers/globals.dart';
import '../../provider/notification_provider.dart';

List<Color> accentColours = [
  Colors.red.shade300,
  Colors.deepOrange.shade300,
  Colors.yellow.shade200,
  Colors.green.shade300,
  Colors.lightBlue.shade300,
  Colors.deepPurple.shade300
];
List<Color> fillColours = [
  Colors.red.shade200,
  Colors.deepOrange.shade200,
  Colors.yellow.shade400,
  Colors.green.shade200,
  Colors.lightBlue.shade200,
  Colors.deepPurple.shade200
];
List<Color> buttonColours = [
  Colors.red.shade400,
  Colors.deepOrange.shade400,
  Colors.yellow.shade600,
  Colors.green.shade400,
  Colors.lightBlue.shade400,
  Colors.deepPurple.shade400
];

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

    //print("map " + dataMap["body"]);

    List<String> friendTokenList = removeMyTokenFromList(
        receivedTokenAndSteps?.tokens ?? [],
        receivedTokenAndSteps?.receiversToken ?? "");
    Map<int, String> recipeIdToName = new Map<int, String>();
    for (int i = 0; i < (thisUserSteps?.notes?.length ?? 0); i++) {
      String strToParse = thisUserSteps?.notes?[i] ?? "";
      int dashIndex = strToParse.indexOf("-");
      String recipeName = strToParse.substring(dashIndex + 1);
      int recipeId = int.parse(strToParse.substring(0, dashIndex));
      recipeIdToName[recipeId] = recipeName;
    }

    Map fillColourIds = Map<int, Color>();
    Map accentColourIds = Map<int, Color>();
    Map buttonColourIds = Map<int, Color>();
    List<int> recipeIdList = new List<int>.from(recipeIdToName.keys);
    print(recipeIdList);

    for (int i = 0; i < recipeIdList.length; i++) {
      fillColourIds[recipeIdList[i]] = fillColours[i];
      accentColourIds[recipeIdList[i]] = accentColours[i];
      buttonColourIds[recipeIdList[i]] = buttonColours[i];
    }

    final List<bool> selected =
        List.generate(thisUserSteps?.recipeSteps?.length ?? 0, (i) => true);

    return Scaffold(
        appBar: AppBar(
            title: Text(myUsername.capitalize() + "'s Received Instructions")),
        body: Container(
            padding: EdgeInsets.all(20),
            child: Center(
              child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Container(
                        margin: EdgeInsets.only(top: 5, bottom: 15),
                        child: Text(
                          "Recipes Legend",
                          textAlign: TextAlign.left,
                          style: TextStyle(
                              fontWeight: FontWeight.bold, fontSize: 18),
                        )),
                    SizedBox(
                        height: 180,
                        child: new ListView.builder(
                            itemCount: recipeIdList.length,
                            itemBuilder: (BuildContext context, int index) {
                              return Container(
                                  decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(15),
                                      color: fillColourIds[recipeIdList[index]],
                                      border: Border.all(
                                          width: 3,
                                          color: accentColourIds[
                                              recipeIdList[index]])),
                                  padding: const EdgeInsets.all(15),
                                  child: Text(
                                    "Recipe " +
                                        recipeIdList[index].toString() +
                                        ": " +
                                        recipeIdToName[recipeIdList[index]]!,
                                  ));
                            })),
                    Container(
                        margin: EdgeInsets.only(top: 20, bottom: 15),
                        child: Text(
                          "Order of Steps",
                          textAlign: TextAlign.left,
                          style: TextStyle(
                              fontWeight: FontWeight.bold, fontSize: 18),
                        )),
                    StatefulBuilder(
                        builder: (BuildContext context, StateSetter setState) {
                      return Expanded(
                        child: Column(
                          children: [
                            Expanded(
                                child: new ListView.builder(
                                    itemCount:
                                        thisUserSteps?.recipeSteps?.length ?? 0,
                                    itemBuilder:
                                        (BuildContext context, int index) {
                                      String instructionNumber = (thisUserSteps
                                              ?.recipeSteps?[index].number ??
                                          "");
                                      String id = int.parse(
                                              instructionNumber.split(".")[0])
                                          .toString();
                                      String number = int.parse(
                                              instructionNumber.split(".")[1])
                                          .toString();

                                      return Padding(
                                        padding:
                                            const EdgeInsets.only(bottom: 15),
                                        child: Row(children: [
                                          Flexible(
                                            child: InkWell(
                                              onTap: () {
                                                setState(() {
                                                  selected[index] =
                                                      !selected[index];
                                                });
                                              },
                                              child: Container(
                                                decoration: BoxDecoration(
                                                    borderRadius:
                                                        BorderRadius.circular(
                                                            15),
                                                    color: selected[index]
                                                        ? fillColourIds[
                                                            double.parse(thisUserSteps?.recipeSteps?[index].number ?? "")
                                                                .floor()]
                                                        : Colors.grey.shade300,
                                                    border: Border.all(
                                                        width: 3,
                                                        color: selected[index]
                                                            ? accentColourIds[
                                                                double.parse(
                                                                        thisUserSteps?.recipeSteps?[index].number ??
                                                                            "")
                                                                    .floor()]
                                                            : Colors.grey.shade400)),
                                                padding:
                                                    const EdgeInsets.all(15),
                                                child: Wrap(
                                                    alignment: WrapAlignment
                                                        .spaceBetween,
                                                    runAlignment: WrapAlignment
                                                        .spaceBetween,
                                                    children: [
                                                      Text(
                                                          "Recipe " +
                                                              id +
                                                              " - Step " +
                                                              number,
                                                          style: TextStyle(
                                                            fontSize: 18,
                                                            fontWeight:
                                                                FontWeight.bold,
                                                          )),
                                                      Text(
                                                          thisUserSteps
                                                                  ?.recipeSteps?[
                                                                      index]
                                                                  .instructions ??
                                                              "",
                                                          style: TextStyle(
                                                            fontSize: 18,
                                                            fontWeight:
                                                                FontWeight.w400,
                                                          )),
                                                      Text(
                                                          "Ingredients: " +
                                                              (thisUserSteps
                                                                      ?.recipeSteps?[
                                                                          index]
                                                                      .ingredientsCompleteList
                                                                      ?.join(
                                                                          ', ') ??
                                                                  ""),
                                                          style: TextStyle(
                                                              fontSize: 18,
                                                              fontStyle:
                                                                  FontStyle
                                                                      .italic)),
                                                      Padding(
                                                        padding:
                                                            const EdgeInsets
                                                                .only(top: 10),
                                                        child: Center(
                                                          child: ElevatedButton(
                                                            style: ElevatedButton.styleFrom(
                                                                textStyle:
                                                                    const TextStyle(
                                                                        fontSize:
                                                                            18),
                                                                padding:
                                                                    const EdgeInsets
                                                                            .all(
                                                                        10),
                                                                backgroundColor: selected[
                                                                        index]
                                                                    ? buttonColourIds[double.parse(
                                                                            thisUserSteps?.recipeSteps?[index].number ??
                                                                                "")
                                                                        .floor()]
                                                                    : Colors
                                                                        .grey),
                                                            onPressed:
                                                                (!selected[
                                                                        index])
                                                                    ? null
                                                                    : () {
                                                                        // if it's the host they use a map from the regular instructions screen
                                                                        //if you have moved past this step, don't send a notification
                                                                        for (int i =
                                                                                0;
                                                                            i < (friendTokenList.length ?? 0);
                                                                            i++) {
                                                                          fcmProvider.sendNotification(
                                                                              token: friendTokenList[i],
                                                                              title: "Step Blocked",
                                                                              body: "I'm blocked on Recipe " + id + " - Step " + number + "!",
                                                                              isBlocked: true);
                                                                        }
                                                                      },
                                                            child: Text(
                                                              "I'm blocked on Recipe " +
                                                                  id +
                                                                  " - Step " +
                                                                  number +
                                                                  "!",
                                                            ),
                                                          ),
                                                        ),
                                                      ),
                                                    ]),
                                              ),
                                            ),
                                          ),
                                        ]),
                                      );
                                    })),
                          ],
                        ),
                      );
                    })
                  ]),
            )));
  }
}
