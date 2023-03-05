import 'package:first_app/data_models/token_and_steps_communication.dart';
import 'package:first_app/screens/home_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../data_models/meal_session_steps.dart';
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
    TokenAndStepsCommunication? receivedTokenAndSteps = TokenAndStepsCommunication.fromJson(dataMap["body"]);
    //MealSessionSteps? sessionList = tokenAndSteps?.mealSessionSteps;
    MealSessionSteps? thisUserSteps = receivedTokenAndSteps.mealSessionSteps;

    // for (int i = 0; i < (sessionList?.length ?? 0); i++) {
    //   // emailToFind is the user's own email
    //   if (sessionList?[i].userEmail == myEmail) {
    //     thisUserSteps = sessionList![i];
    //   }
    // }

    return Scaffold(
      appBar: AppBar(title: const Text("Received Instructions")),
      body:
    Container(
    padding: EdgeInsets.all(20), child: Center (
            child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
          new Expanded(
              child: new ListView.builder(
                  itemCount: thisUserSteps?.recipeSteps?.length ?? 0,
                  itemBuilder: (BuildContext context, int index) {
                    // first argument to the function has it's step number
                    // appended to the instruction appended to the
                    // complete list of ingredients for that step
                    return groupedButtonText(
                        // (index + 1).toString() +
                        //     ". " + dataMap["body"], //remove null checks for now
                        (index + 1).toString() +
                            ". " +
                            (thisUserSteps?.recipeSteps?[index]
                                .instructions ??
                                "") +
                            " (" +
                            (thisUserSteps?.recipeSteps?[index]
                                .ingredientsCompleteList
                                ?.join(', ') ??
                                "") +
                            ")",
                        "I'm blocked on this step!",
                        receivedTokenAndSteps?.tokens ?? [""],
                        fcmProvider);
                  }))]),
      )),
    );
  }
}
