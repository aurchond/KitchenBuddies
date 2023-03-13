import 'package:first_app/data_models/data_communication_wrapper_model.dart';
import 'package:first_app/helpers/friend_tokens.dart';
import 'dart:convert';
import 'package:first_app/screens/home_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../data_models/meal_session_steps_model.dart';
import '../../helpers/globals.dart';
import '../../provider/notification_provider.dart';
import '../../widgets/grouped_button_text.dart';

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
     print("map " + dataMap["body"]);



    //TODO: set up fade later
    bool visible = true;
    return Scaffold(
      appBar: AppBar(title: const Text("Received Instructions")),
      body: Container(
          padding: EdgeInsets.all(20),
          child: Center(
            child:
                Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              // display the user's email at the top
              Container(
                margin: EdgeInsets.all(20),
                child: Text(
                  //email,
                  thisUserSteps?.userEmail ?? "",
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                ),
              ),
              StatefulBuilder(builder: (BuildContext context, StateSetter setState) {
                return Expanded(
                    child: new ListView.builder(
                    //itemCount: numSteps ?? 0,
                    itemCount
                : thisUserSteps?.recipeSteps?.length ?? 0,
                      itemBuilder: (BuildContext context, int index) {
                        // first argument to the function has it's step number
                        // appended to the instruction appended to the
                        // complete list of ingredients for that step
                        return groupedButtonText(visible,
                            (thisUserSteps?.recipeSteps?[index].number.toString() ?? "") + ". " +
                                (thisUserSteps
                                    ?.recipeSteps?[index]
                                    .instructions ??
                                    "") +
                                " (" +
                                (thisUserSteps
                                    ?.recipeSteps?[index]
                                    .ingredientsCompleteList
                                    ?.join(', ') ??
                                    "") +
                                ")",
                            "I'm blocked on step " + (thisUserSteps?.recipeSteps?[index].number.toString() ?? "")+  "!",
                            null, // is not sending in a map,
                            receivedTokenAndSteps?.tokens,
                            false, // is not the host
                            fcmProvider, setState);
                      }));})
            ]),
          )),
    );
  }
}
