import 'dart:convert';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:first_app/backend_processing/data_class.dart';
import 'package:first_app/data_models/meal_session_steps_request_model.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:provider/provider.dart';

import '../../data_models/data_communication_wrapper_model.dart';
import '../../data_models/meal_session_steps_model.dart';
import '../../helpers/friend_tokens.dart';
import '../../helpers/globals.dart';
import '../../provider/auth_provider.dart';
import '../../provider/notification_provider.dart';
import '../../widgets/grouped_button_text.dart';

class InstructionsScreen extends StatefulWidget {
  const InstructionsScreen(
      {Key? key,
      required Map<String, String> this.tokenMap,
      required List<int> this.selectedRecipes,
      required List<String> this.selectedFriends})
      : super(key: key);
  final Map<String, String> tokenMap;
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
    final fcmProvider =
        Provider.of<NotificationProvider>(context, listen: false);

    postModel.kitchenConstraints?.userEmail = myEmail;
    postModel.mealSessionStepsRequest = MealSessionStepsRequest(
        kitchenConstraints: postModel.kitchenConstraints,
        recipeIDs: widget.selectedRecipes,
        includedFriends: widget.selectedFriends);
    print("attempt to load meal session steps");
    postModel.loadMealSessionSteps();
    print("returned from loading steps");
  }

  @override
  Widget build(BuildContext context) {
    final postModel = Provider.of<DataClass>(context);
    final fcmProvider = Provider.of<NotificationProvider>(context);

    Map<String,String> friendsTokenMap = removeMyTokenFromMap(widget.tokenMap);

    print(postModel.allMealSessionSteps?.length.toString());

    // this is the body with ALL tokens (including hosts') and the chosen friends' meal steps
    Body sendTokenAndSteps = new Body();
    widget.tokenMap.entries
        .forEach((e) => sendTokenAndSteps.tokens?.add(e.value));

    // the total number of meal session friends and the host
    // send meal session steps to other friends in session
    for (int i = 0; i < widget.selectedFriends.length; i++) {
      //send tokens to everyone so they can also use blocked buttons

      // helper variables
      String chosenFriend = widget.selectedFriends[i];
      String? receiversToken = widget.tokenMap[chosenFriend];

      // go through the session steps and set the
      for (int i = 0; i < (postModel.allMealSessionSteps?.length ?? 0) ; i++) {
        if (postModel.allMealSessionSteps?[i]?.userEmail == chosenFriend) {
          // set the steps of this person's meal steps to the current one
          sendTokenAndSteps.mealSessionSteps =
          postModel.allMealSessionSteps?[i];

          sendTokenAndSteps.receiversToken = receiversToken;

        } else {
          print("we got my steps");
          postModel.mySteps = postModel.allMealSessionSteps?[i];
        }
      }

      // encode the body
      final body = jsonEncode(sendTokenAndSteps.toJson());

      // send the notification to this specific friend
      fcmProvider.sendNotification(
          token: receiversToken.toString(),
          title: "Meal Session Steps",
          body: body
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
                          myEmail,
                          style: TextStyle(
                              fontWeight: FontWeight.bold, fontSize: 18),
                        ),
                      ),

                      // todo: display all ingredients necessary here

                      // display the user's instructions below
                      new Expanded(
                          child: new ListView.builder(
                              itemCount: postModel.mySteps?.recipeSteps?.length ??
                                  0,
                              itemBuilder: (BuildContext context, int index) {
                                // first argument to the function has it's step number
                                // appended to the instruction appended to the
                                // complete list of ingredients for that step
                                return groupedButtonText(
                                    (index + 1).toString() +
                                        ". " +
                                        (postModel
                                                .mySteps
                                                ?.recipeSteps?[index]
                                                .instructions ??
                                            "") +
                                        " (" +
                                        (postModel
                                                .mySteps
                                                ?.recipeSteps?[index]
                                                .ingredientsCompleteList
                                                ?.join(', ') ??
                                            "") +
                                        ")",
                                    "I'm blocked on this step!",
                                    friendsTokenMap,
                                    null, // is not sending a friendsTokenList
                                    true, // is indeed the host
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
