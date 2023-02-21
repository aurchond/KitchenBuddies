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
    postModel.getPostData("shadiz@gmail.com"); //call with index
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
