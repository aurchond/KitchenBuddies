import 'package:first_app/screens/home_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../data_models/meal_session_steps.dart';
import '../../provider/notification_provider.dart';
import '../../widgets/grouped_button_text.dart';

class ReceivedInstructionScreen extends StatelessWidget {
  final RemoteMessage message;
  const ReceivedInstructionScreen({Key? key, required this.message})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    final fcmProvider = Provider.of<NotificationProvider>(context);

    // Map<String, dynamic> messageMap = message.notification!.toMap();
    // MealSessionSteps? thisUserSteps = MealSessionSteps.fromJson(messageMap);
    Map<String, dynamic> dataMap = message.data!;
    List<String> tokens = [""];
    return Scaffold(
      appBar: AppBar(title: const Text("Received Instructions")),
      body:
    Container(
    padding: EdgeInsets.all(20), child: Center (
            child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
          new Expanded(
            //https://www.allrecipes.com/recipe/187342/banh-mi/
              child: new ListView.builder(
                  itemCount: 5, //hardcoded bc string is not a list
                  itemBuilder: (BuildContext context, int index) {
                    // first argument to the function has it's step number
                    // appended to the instruction appended to the
                    // complete list of ingredients for that step
                    return groupedButtonText(
                        (index + 1).toString() +
                            ". " + dataMap["body"], //remove null checks for now
                        "I'm blocked on this step!",
                        tokens,
                        fcmProvider);
                  }))]),
      )),
    );
  }
}
