import 'dart:convert';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:first_app/backend_processing/data_class.dart';
import 'package:first_app/data_models/meal_session_steps_request.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:provider/provider.dart';

import '../../data_models/token_and_steps_communication.dart';
import '../../helpers/globals.dart';
import '../../provider/auth_provider.dart';
import '../../provider/notification_provider.dart';
import '../../widgets/grouped_button_text.dart';

class InstructionsScreen extends StatefulWidget {
  const InstructionsScreen({Key? key, required List<String> this.tokens, required List<int> this.selectedRecipes, required List<String> this.selectedFriends}) : super(key: key);
  final List<String> tokens;
  final List<int> selectedRecipes;
  final List<String> selectedFriends;

  @override
  _InstructionsScreenState createState() => _InstructionsScreenState();
}

class _InstructionsScreenState extends State<InstructionsScreen> {
  @override
  void initState() {
    super.initState();
    final postModel = Provider.of<DataClass>(context, listen: false);

    postModel.kitchenConstraints?.userEmail = myEmail;
    postModel.mealSessionStepsRequest = MealSessionStepsRequest(kitchenConstraints: postModel.kitchenConstraints, recipeIDs: widget.selectedRecipes, includedFriends: widget.selectedFriends);
    postModel.loadMealSessionSteps(myEmail);



    //send tokens to everyone so they can also use blocked buttons
  }

  @override
  Widget build(BuildContext context) {
    final postModel = Provider.of<DataClass>(context);
    final fcmProvider = Provider.of<NotificationProvider>(context);

    //send tokens to everyone so they can also use blocked buttons
    TokenAndStepsCommunication sendTokenAndSteps = new TokenAndStepsCommunication();
    sendTokenAndSteps.tokens = widget.tokens;
    sendTokenAndSteps.mealSessionSteps = postModel.mealSessionSteps;

    // zip it up
    final body = jsonEncode(sendTokenAndSteps.toJson());

    //send meal session steps to other friends in session
    for (int i = 0; i < widget.tokens.length; i++) {
      fcmProvider.sendNotification(
          token: widget.tokens[i],
          title: "Meal Session Steps",
          body: body //unzip him -> add a sweather underneath (instructions + tokens) -> zip him up and send in notification
      );
    }

    return Consumer<AuthProvider>(builder: (context, model, _) {
      return Scaffold(
        appBar: AppBar(
          title: Text("Instructions"),
        ),
        body: Container(
          padding: EdgeInsets.all(20),
          child: postModel.loading
              ? Center(
                  // while the postmodel is loading, we go into this first widget
                  child: Container(
                    child: SpinKitThreeBounce(
                      itemBuilder: (BuildContext context, int index) {
                        // this is the formatting of the meal instructions
                        return DecoratedBox(
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(15),
                            color: index.isEven
                                ? Colors.red
                                : Colors.deepOrange.shade300,
                          ),
                        );
                      },
                    ),
                  ),
                )
              : Center(
                  // after the postmodel is done loading, we go into this widget
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [

                      // display the user's email at the top
                      Container(
                        margin: EdgeInsets.all(20),
                        child: Text(
                          postModel.mealSessionSteps?.userEmail ?? "",
                          style: TextStyle(
                              fontWeight: FontWeight.bold, fontSize: 18),
                        ),
                      ),

                      // todo: display all ingredients necessary here

                      // display the user's instructions below
                      new Expanded(
                          child: new ListView.builder(
                              itemCount: postModel.mealSessionSteps?.recipeSteps?.length,
                              itemBuilder: (BuildContext context, int index) {

                                // first argument to the function has it's step number
                                // appended to the instruction appended to the
                                // complete list of ingredients for that step
                                return groupedButtonText(
                                    (index + 1).toString() +
                                        ". " +
                                        (postModel.mealSessionSteps?.recipeSteps?[index]
                                                .instructions ??
                                            "") +
                                        " (" +
                                        (postModel.mealSessionSteps?.recipeSteps?[index]
                                                .ingredientsCompleteList
                                                ?.join(', ') ??
                                            "") +
                                        ")",
                                    "I'm blocked on this step!",
                                    widget.tokens,
                                    fcmProvider);
                              })),
                    ],
                  ),
                ),
        ),
      );
    });
  }
}
