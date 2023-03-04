import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:first_app/backend_processing/data_class.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:provider/provider.dart';

import '../../provider/auth_provider.dart';
import '../../provider/notification_provider.dart';
import '../../helpers/grouped_button_text.dart';

class InstructionsScreen extends StatefulWidget {
  const InstructionsScreen({Key? key}) : super(key: key);

  @override
  _InstructionsScreenState createState() => _InstructionsScreenState();
}

class _InstructionsScreenState extends State<InstructionsScreen> {
  @override
  void initState() {
    super.initState();
    final postModel = Provider.of<DataClass>(context, listen: false);
    postModel.loadMealSessionSteps("shadiz@gmail.com"); //todo: update this to the user's own email
  }

  @override
  Widget build(BuildContext context) {
    final postModel = Provider.of<DataClass>(context);
    final fcmProvider = Provider.of<NotificationProvider>(context);

    // this is the token for firebase auth
    String token =
        "eMvq6DPDRTCzCPGZAD5hC7:APA91bHNtgKTMGNOpcHdeGxACuv8gE3XrInhkViPFvevKLl-dvZ4Wi3YfkxNN3_1fO4LduElNBhl9B0BPJbI7yAE6unWiVwkg528lZO5rtVOQPbUzu5era9GQTxewY8vd-GROjjyTRW2";
    //final authProvider = Provider.of<AuthProvider>(context);

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
                                    "I'm done this step!",
                                    token,
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
