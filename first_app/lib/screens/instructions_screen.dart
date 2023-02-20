import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:first_app/backend_processing/data_class.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:provider/provider.dart';

import '../provider/auth_provider.dart';
import '../provider/notification_provider.dart';

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

  stitchIngredientList(final postModel) {
    //stitch ingredient quantity and list together
    List<List<String>> ingredients =
        new List.generate(postModel.post?.recipeStep?.length ?? 0, (i) => []);
    if (postModel.post?.recipeStep?.length != null &&
        postModel.post?.recipeStep?[0].ingredientsCompleteList?.length !=
            null) {
      for (int i = 0; i < (postModel.post?.recipeStep?.length ?? 0); i++) {
        //should include error case for missing length
        for (int j = 0;
            j <
                (postModel
                        .post?.recipeStep?[i].ingredientsCompleteList?.length ??
                    0);
            j++) {
          ingredients[i].add((postModel
                      .post?.recipeStep?[i].ingredientsCompleteList?[j]
                      .toString() ??
                  "")
              // + " " + (postModel.post?.recipeStep?[i].ingredientList?[j] ?? "")
              );
        }
      }
    }

    print(ingredients);
  }

  @override
  Widget build(BuildContext context) {
    final postModel = Provider.of<DataClass>(context);
    final fcmProvider = Provider.of<NotificationProvider>(context);

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
                    children: [
                      Container(
                        margin: EdgeInsets.only(top: 40, bottom: 20),
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
                                return new Text(
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
                                    style: TextStyle(
                                        fontSize: 14,
                                        color: Colors.deepOrange.shade300));
                              })),
                      new Expanded( /// BORROWED THIS FROM HOME_SCREEN ///
                        child: StreamBuilder<QuerySnapshot>(
                            builder: (context, snapshot) {
                              if (snapshot.hasData) {
                                return ElevatedButton(
                                    onPressed: () {
                                      fcmProvider.sendNotification(
                                          token: snapshot.data!.docs[0]["token"], //TODO: use token belonging to blocked person
                                          title: "Step Completed",
                                          body: "I'm finished with my step!"); //TODO: write down resource being passed off
                                    },
                                    child: Text(
                                        "Tell " + snapshot.data!.docs[0]["user_name"] + " you're done with your step!"));
                              }
                              else {
                                return const Center(child: CircularProgressIndicator());
                              }
                            },
                            stream: FirebaseFirestore.instance
                                .collection("users")
                                .snapshots()),
                      )
                    ],
                  ),
                ),
        ),
      );
    });
  }
}
