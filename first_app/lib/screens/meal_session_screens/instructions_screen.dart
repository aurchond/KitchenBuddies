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

    // this is the body with ALL tokens (including hosts') and the chosen friends' meal steps
    Body sendTokenAndSteps = new Body();
    sendTokenAndSteps.tokens = <String>[];
    widget.tokenMap.entries
        .forEach((e) => sendTokenAndSteps.tokens?.add(e.value));

    // the total number of meal session friends and the host
    // send meal session steps to other friends in session
    for (int i = 0; i < widget.selectedFriends.length; i++) {
      //send tokens to everyone so they can also use blocked buttons

      // helper variables
      String chosenFriend = widget.selectedFriends[i];
      String? receiversToken = widget.tokenMap[chosenFriend];
      sendTokenAndSteps.receiversToken = receiversToken;

      // go through the session steps and set the
      for (int i = 0; i < (postModel.allMealSessionSteps?.length ?? 0); i++) {
        if (postModel.allMealSessionSteps?[i]?.userEmail == chosenFriend) {
          // set the steps of this person's meal steps to the current one
          sendTokenAndSteps.mealSessionSteps =
              postModel.allMealSessionSteps?[i];
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
          body: body,
          isBlocked: false);
    }

    Map<String, String> friendsTokenMap = removeMyTokenFromMap(widget.tokenMap);

    Map fillColourIds = Map<int, Color>();
    for (int i = 0; i < widget.selectedRecipes.length; i++) {
      fillColourIds[widget.selectedRecipes[i]] = fillColours[i];
    }

    Map accentColourIds = Map<int, Color>();
    for (int i = 0; i < widget.selectedRecipes.length; i++) {
      accentColourIds[widget.selectedRecipes[i]] = accentColours[i];
    }

    Map buttonColourIds = Map<int, Color>();
    for (int i = 0; i < widget.selectedRecipes.length; i++) {
      buttonColourIds[widget.selectedRecipes[i]] = buttonColours[i];
    }

    final List<bool> selected =
        List.generate(postModel.mySteps?.recipeSteps?.length ?? 0, (i) => true);

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
                      StatefulBuilder(builder:
                          (BuildContext context, StateSetter setState) {
                        return Expanded(
                            child: Column(children: [
                          Expanded(
                              child: new ListView.builder(
                                  itemCount:
                                      postModel.mySteps?.recipeSteps?.length ??
                                          0,
                                  itemBuilder:
                                      (BuildContext context, int index) {
                                    return Padding(
                                      padding:
                                          const EdgeInsets.only(bottom: 15),
                                      child: Row(
                                        children: [
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
                                                      BorderRadius.circular(15),
                                                  color: selected[index]
                                                      ? fillColourIds[
                                                          double.parse(postModel
                                                                      .mySteps
                                                                      ?.recipeSteps?[
                                                                          index]
                                                                      .number ??
                                                                  "")
                                                              .floor()]
                                                      // ? Colors
                                                      //     .deepOrange.shade200
                                                      : Colors.grey.shade300,
                                                  border: Border.all(
                                                      width: 3,
                                                      color: selected[index]
                                                          ? accentColourIds[double.parse(postModel
                                                                      .mySteps
                                                                      ?.recipeSteps?[index]
                                                                      .number ??
                                                                  "")
                                                              .floor()]
                                                          : Colors.grey.shade400)),
                                              padding: const EdgeInsets.all(15),
                                              child: Wrap(
                                                  alignment: WrapAlignment
                                                      .spaceBetween,
                                                  runAlignment: WrapAlignment
                                                      .spaceBetween,
                                                  children: [
                                                    Text(
                                                        (postModel
                                                                    .mySteps
                                                                    ?.recipeSteps?[
                                                                        index]
                                                                    .number
                                                                    .toString() ??
                                                                "") +
                                                            ". " +
                                                            (postModel
                                                                    .mySteps
                                                                    ?.recipeSteps?[
                                                                        index]
                                                                    .instructions ??
                                                                "") +
                                                            " (" +
                                                            (postModel
                                                                    .mySteps
                                                                    ?.recipeSteps?[
                                                                        index]
                                                                    .ingredientsCompleteList
                                                                    ?.join(
                                                                        ', ') ??
                                                                "") +
                                                            ")",
                                                        style: TextStyle(
                                                          fontSize: 18,
                                                          fontWeight:
                                                              FontWeight.w400,
                                                        )),
                                                    Padding(
                                                      padding:
                                                          const EdgeInsets.only(
                                                              top: 10),
                                                      child: Center(
                                                        child: ElevatedButton(
                                                          style: ElevatedButton.styleFrom(
                                                              textStyle: const TextStyle(
                                                                  fontSize: 18),
                                                              padding:
                                                                  const EdgeInsets
                                                                      .all(10),
                                                              backgroundColor: selected[
                                                                      index]
                                                                  ? buttonColourIds[
                                                                      double.parse(postModel.mySteps?.recipeSteps?[index].number ??
                                                                              "")
                                                                          .floor()]
                                                                  : Colors
                                                                      .grey),
                                                          onPressed:
                                                              (!selected[index])
                                                                  ? null
                                                                  : () {
                                                                      // if it's the host they use a map from the regular instructions screen
                                                                      //if you have moved past this step, don't send a notification
                                                                      for (var email
                                                                          in friendsTokenMap
                                                                              .keys) {
                                                                        fcmProvider.sendNotification(
                                                                            token: friendsTokenMap[
                                                                                email]!,
                                                                            title:
                                                                                "Step Blocked",
                                                                            body: "I'm blocked on step " +
                                                                                (postModel?.mySteps?.recipeSteps?[index].number.toString() ?? "") +
                                                                                "!",
                                                                            isBlocked: true);
                                                                      }
                                                                    },
                                                          child: Text(
                                                            "I'm blocked on step " +
                                                                (postModel
                                                                        .mySteps
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
                    ],
                  ),
                ),
        ),
      );
    });
  }
}
