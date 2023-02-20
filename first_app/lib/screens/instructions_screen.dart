import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:first_app/backend_processing/data_class.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:provider/provider.dart';

import '../provider/auth_provider.dart';
import '../provider/notification_provider.dart';
import '../widgets/grouped_button_text.dart';

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
    postModel.getPostData(0); //call with index
  }

  @override
  Widget build(BuildContext context) {
    final postModel = Provider.of<DataClass>(context);
    final fcmProvider = Provider.of<NotificationProvider>(context);
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
                  child: Container(
                    child: SpinKitThreeBounce(
                      itemBuilder: (BuildContext context, int index) {
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
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    //scrollDirection: Axis.horizontal,
                    children: [
                      Container(
                        margin: EdgeInsets.all(20),
                        child: Text(
                          postModel.post?.userEmail ?? "",
                          style: TextStyle(
                              fontWeight: FontWeight.bold, fontSize: 18),
                        ),
                      ),
                      new Expanded(
                          child: new ListView.builder(
                              itemCount: postModel.post?.recipeStep?.length,
                              itemBuilder: (BuildContext context, int index) {
                                return groupedButtonText(
                                    (index + 1).toString() +
                                        ". " +
                                        (postModel.post?.recipeStep?[index]
                                                .instructions ??
                                            "") +
                                        " (" +
                                        (postModel.post?.recipeStep?[index]
                                                .ingredientsCompleteList
                                                ?.join(', ') ??
                                            "") +
                                        ")",
                                    "Tell your friend you're done your step!",
                                    token,
                                    fcmProvider);
                              })),
                      //   return new Text(
                      //       (index + 1).toString() +
                      //           ". " +
                      //           (postModel.post?.recipeStep?[index]
                      //                   .instructions ??
                      //               "") +
                      //           " (" +
                      //           (postModel.post?.recipeStep?[index]
                      //                   .ingredientsCompleteList
                      //                   ?.join(', ') ??
                      //               "") +
                      //           ")",
                      //       style: TextStyle(
                      //           fontSize: 14,
                      //           color: Colors.deepOrange.shade300));
                      // })),
                      // new Expanded(
                      //   /// BORROWED THIS FROM HOME_SCREEN ///
                      //   child: StreamBuilder<QuerySnapshot>(
                      //       builder: (context, snapshot) {
                      //         if (snapshot.hasData) {
                      //           return ElevatedButton(
                      //               onPressed: () {
                      //                 fcmProvider.sendNotification(
                      //                     token: snapshot.data!.docs[0][
                      //                         "token"], //TODO: use token belonging to blocked person
                      //                     title: "Step Completed",
                      //                     body:
                      //                         "I'm finished with my step!"); //TODO: write down resource being passed off
                      //               },
                      //               child: Text("Tell " +
                      //                   snapshot.data!.docs[0]["user_name"] +
                      //                   " you're done with your step!"));
                      //         } else {
                      //           return const Center(
                      //               child: CircularProgressIndicator());
                      //         }
                      //       },
                      //       stream: FirebaseFirestore.instance
                      //           .collection("users")
                      //           .snapshots()),
                      // )
                    ],
                  ),
                ),
        ),
      );
    });
  }
}
