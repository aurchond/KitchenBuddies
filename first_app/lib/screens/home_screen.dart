import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:first_app/widgets/kitchen_constraints.dart';
import 'package:first_app/local_notification_service.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/screens/meal_session_screens/received_instructions_screen.dart';
import 'package:first_app/widgets/input_text_button.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'package:first_app/provider/notification_provider.dart';

import 'package:http/http.dart' as http;

import '../backend_processing/data_class.dart';
import '../backend_processing/get_requests.dart';
import '../backend_processing/post_requests.dart';
import '../helpers/globals.dart';
import '../widgets/tile_decorated.dart';
import '../data_models/kitchen_constraints.dart';

const List<String> skillList = <String>['Beginner', 'Intermediate', 'Advanced'];
String savedSkillValue = "Intermediate";

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  //initialize controllers and nodes for input text buttons
  final textController = TextEditingController();
  late FocusNode myFocusNode = FocusNode();

  final List<FocusNode> focusNodes =
      List<FocusNode>.generate(5, (int index) => FocusNode());
  final List<TextEditingController> controllers =
      List<TextEditingController>.generate(
          5, (int index) => TextEditingController());

  @override
  void initState() {
    final dataModel = Provider.of<DataClass>(context, listen: false);
    dataModel.loadHomePage(); //todo: save string value

    //in terminated state
    FirebaseMessaging.instance.getInitialMessage().then((value) {
/*     if (value != null) {
        Navigator.of(context).push(new MaterialPageRoute(
            builder: (context) => ReceivedInstructionScreen(message: value)));
      }*/
    });

    //in foreground listener
    // if you have app open
    FirebaseMessaging.onMessage.listen((event) {
      LocalNotificationService.init();
      LocalNotificationService.displayNotification(event);

      //getting problem about widget being unmounted
      if (mounted) {
        final splitMessage = (event.data.toString().split('title: '))[1]
            .split('}'); //TODO: do this in regex
        //we will categorize our notifications based on title
        //notifications to send to other users about completing a step
        //notification that redirects user to instruction page
        if (splitMessage[0] != "Step Completed") {
          Navigator.of(context).push(MaterialPageRoute(
              //if this starts bugging out again, use pushReplacement
              builder: (context) => ReceivedInstructionScreen(message: event)));
        }
      }
    });

    //in background state but not terminated
    FirebaseMessaging.onMessageOpenedApp.listen((event) {
/*      Navigator.of(context).push(new MaterialPageRoute(
          builder: (context) => ReceivedInstructionScreen(message: event)));*/
    });

    // TODO: implement initstate
    super.initState();
  }

  //input text button callback
  onPressedCallback(FocusNode _focusNode, TextEditingController _controller) {
    print(_controller.text);
    addFriend(_controller.text);
    _focusNode.unfocus();
  }

  @override
  void dispose() {
    // dispose of resources for input text buttons
    textController.dispose();
    myFocusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final fcmProvider = Provider.of<NotificationProvider>(context);
    final dataModel = Provider.of<DataClass>(context);

    String? dropdownValue = dataModel.skillLevel;
    List<String>? myFriends = dataModel.friendsList?.friends;
    KitchenConstraints? kitchenConstraints = dataModel?.kitchenConstraints;

    List<bool> isTextFieldShown = List.filled(5, false, growable: true);

    return Consumer<AuthProvider>(builder: (context, model, _) {
      return Scaffold(
          appBar: AppBar(
            actions: [
              IconButton(
                onPressed: () {
                  model.logOut();
                },
                icon: const Icon(Icons.logout),
              )
            ],
          ),
          body: SingleChildScrollView(
              physics: ClampingScrollPhysics(),
              child: ConstrainedBox(
                  constraints: BoxConstraints(
                    minWidth: MediaQuery.of(context).size.width,
                    minHeight: MediaQuery.of(context).size.height,
                  ),
                  child: IntrinsicHeight(
                      child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      mainAxisSize: MainAxisSize.max,
                      children: [
                        /// add and get friends ///
                        Flexible(
                            fit: FlexFit.loose,
                            child: Container(
                                margin: EdgeInsets.only(top: 20, bottom: 15),
                                child: Text(
                                  "Hello " + myEmail + "! Your friends:",
                                  style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 18),
                                ))),
                        StatefulBuilder(builder:
                            (BuildContext context, StateSetter setState) {
                          return Column(children: [
                            SizedBox(
                                height: 205,
                                child: Container(
                                    padding: const EdgeInsets.all(10),
                                    color: Colors.deepOrange.shade100,
                                    child: GridView.builder(
                                        gridDelegate:
                                            const SliverGridDelegateWithFixedCrossAxisCount(
                                                crossAxisCount: 2,
                                                childAspectRatio: 4 / 2,
                                                crossAxisSpacing: 10,
                                                mainAxisSpacing: 10),
                                        itemCount: myFriends?.length,
                                        itemBuilder:
                                            (BuildContext context, index) {
                                          return TileDecorated(
                                              Colors.deepOrange.shade200,
                                              Icon(
                                                Icons.person,
                                                color:
                                                    Colors.deepOrange.shade700,
                                              ), index,
                                              Text(myFriends?[index] ?? "",
                                                  style: TextStyle(
                                                      fontSize: 16,
                                                      fontWeight:
                                                          FontWeight.w400)),
                                              Text(""), Text(""),
                                              false);
                                        }))),
                            Container(
                                padding: const EdgeInsets.only(bottom: 7),
                                color: Colors.deepOrange.shade100,
                                child: Padding(
                                    padding: const EdgeInsets.all(10.0),
                                    child: Container(
                                        decoration: BoxDecoration(
                                          color: Colors.deepOrange.shade200,
                                          border: Border.all(
                                              width: 3,
                                              color:
                                                  Colors.deepOrange.shade300),
                                        ),
                                        child: Row(
                                          children: [
                                            Expanded(
                                                child: TextField(
                                              focusNode: myFocusNode,
                                              controller: textController,
                                              decoration: InputDecoration(
                                                border: OutlineInputBorder(),
                                                hintText:
                                                    "Enter your friend's email",
                                                suffixIcon: IconButton(
                                                  onPressed:
                                                      textController.clear,
                                                  icon: Icon(Icons.clear),
                                                ),
                                              ),
                                            )),
                                            SizedBox(
                                              width: 120,
                                              height: 60,
                                              child: ElevatedButton(
                                                  onPressed: () {
                                                    addFriend(
                                                        textController.text);
                                                    myFocusNode.unfocus();
                                                    setState(() {
                                                      dataModel.loadHomePage();
                                                      print(myFriends);
                                                    });
                                                  },
                                                  child: Text("Add friend!")),
                                            )
                                          ],
                                        ))))
                          ]);
                        }),

                        /// old push notif stuff ///
                        // Expanded(
                        //     child: Padding(
                        //   padding: const EdgeInsets.all(10),
                        //   child: Container(
                        //     alignment: Alignment.center,
                        //     decoration: BoxDecoration(
                        //       color: Colors.deepOrange.shade100,
                        //     ),
                        //     child: StreamBuilder<QuerySnapshot>(
                        //         builder: (context, snapshot) {
                        //           if (snapshot.hasData) {
                        //             return ListView.builder(
                        //                 itemBuilder: (context, index) {
                        //                   return Card(
                        //                       child: ListTile(
                        //                           onTap: () {
                        //                             fcmProvider.sendNotification(
                        //                                 token: snapshot.data!.docs[index]
                        //                                     ["token"],
                        //                                 title: snapshot.data!.docs[index]
                        //                                     ["user_name"],
                        //                                 body: "Notification Test");
                        //                           },
                        //                           // indexing user from the db
                        //                           title: Text(snapshot.data!.docs[index]
                        //                               ["user_name"])));
                        //                 },
                        //                 itemCount: snapshot.data!.docs.length);
                        //           } else {
                        //             return const Center(child: CircularProgressIndicator());
                        //           }
                        //         },
                        //         stream: FirebaseFirestore.instance
                        //             .collection("users")
                        //             .snapshots()),
                        //   ),
                        // )),
                        /// set and get skill level ///
                        StatefulBuilder(builder:
                            (BuildContext context, StateSetter setState) {
                          return Row(
                            children: [
                              Container(
                                  margin: EdgeInsets.all(20),
                                  child: Text(
                                    "Change your skill level:",
                                    style: TextStyle(
                                        fontWeight: FontWeight.bold,
                                        fontSize: 18),
                                  )),
                              DropdownButton<String>(
                                value: dropdownValue,
                                icon: const Icon(Icons.arrow_downward),
                                elevation: 16,
                                style:
                                    const TextStyle(color: Colors.deepOrange),
                                underline: Container(
                                  height: 2,
                                  color: Colors.deepOrangeAccent,
                                ),
                                onChanged: (String? value) {
                                  // This is called when the user selects an item.
                                  setState(() {
                                    dropdownValue = value!;
                                    addSkillLevel(value);
                                  });
                                },
                                items: skillList.map<DropdownMenuItem<String>>(
                                    (String value) {
                                  return DropdownMenuItem<String>(
                                    value: value,
                                    child: Text(value,
                                        style: TextStyle(fontSize: 18)),
                                  );
                                }).toList(),
                              )
                            ],
                          );
                        }),

                        /// set and get kitchen constraints
                        StatefulBuilder(builder:
                            (BuildContext context, StateSetter setState) {
                          return Column(children: [
                            Container(
                                margin: EdgeInsets.all(10),
                                child: Text(
                                  "Customize your kitchen:",
                                  style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 18),
                                )),
                            SizedBox(
                                height: 160,
                                child: GridView.count(
                                    padding: const EdgeInsets.all(10),
                                    crossAxisSpacing: 10,
                                    mainAxisSpacing: 10,
                                    crossAxisCount: 5,
                                    children: <Widget>[
                                      //text descriptions of constraints
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: const Text('Ovens',
                                            textAlign: TextAlign.center,
                                            style: TextStyle(
                                                color: Colors.white,
                                                fontWeight: FontWeight.bold)),
                                        color: Colors.deepOrange[300],
                                      ),
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: const Text('Pots',
                                            textAlign: TextAlign.center,
                                            style: TextStyle(
                                                color: Colors.white,
                                                fontWeight: FontWeight.bold)),
                                        color: Colors.deepOrange[300],
                                      ),
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: const Text('Pans',
                                            textAlign: TextAlign.center,
                                            style: TextStyle(
                                                color: Colors.white,
                                                fontWeight: FontWeight.bold)),
                                        color: Colors.deepOrange[300],
                                      ),
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: const Text(
                                          'Bowls',
                                          textAlign: TextAlign.center,
                                          style: TextStyle(
                                              color: Colors.white,
                                              fontWeight: FontWeight.bold),
                                        ),
                                        color: Colors.deepOrange[300],
                                      ),
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: const Text('Cutting Boards',
                                            textAlign: TextAlign.center,
                                            style: TextStyle(
                                                color: Colors.white,
                                                fontWeight: FontWeight.bold)),
                                        color: Colors.deepOrange[300],
                                      ),
                                      /// number input of each of the constraints ///
                                      // number of ovens
                                      Container(
                                          padding: const EdgeInsets.all(8),
                                          color: Colors.deepOrange[200],
                                          child: AnimatedSwitcher(
                                            duration: Duration(
                                              milliseconds: 300,
                                            ),
                                            child: isTextFieldShown[0]
                                                ? TextField(
                                                    focusNode: focusNodes[0],
                                                    controller: controllers[0],
                                                    keyboardType:
                                                        TextInputType.number,
                                                    decoration: InputDecoration(
                                                      border:
                                                          OutlineInputBorder(),
                                                    ),
                                                  )
                                                : SizedBox(
                                                    height: 50,
                                                    child: ElevatedButton(
                                                        onPressed: () {
                                                          setState(() {
                                                            isTextFieldShown[
                                                                0] = true;
                                                          });
                                                        },
                                                        child: Text((kitchenConstraints?.oven ?? 0).toString()))),
                                          )),

                                      // number of pots
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: isTextFieldShown[1]
                                            ? TextField(
                                                focusNode: focusNodes[1],
                                                controller: controllers[1],
                                                keyboardType:
                                                    TextInputType.number,
                                                decoration: InputDecoration(
                                                  border: OutlineInputBorder(),
                                                ),
                                              )
                                            : SizedBox(
                                                height: 50,
                                                child: ElevatedButton(
                                                    onPressed: () {
                                                      setState(() {
                                                        isTextFieldShown[1] =
                                                            true;
                                                      });
                                                    },
                                                    child: Text((kitchenConstraints?.pot ?? 0).toString()))),
                                        color: Colors.deepOrange[200],
                                      ),

                                      // number of pans
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: isTextFieldShown[2]
                                            ? TextField(
                                                focusNode: focusNodes[2],
                                                controller: controllers[2],
                                                keyboardType:
                                                    TextInputType.number,
                                                decoration: InputDecoration(
                                                  border: OutlineInputBorder(),
                                                ),
                                              )
                                            : SizedBox(
                                                height: 50,
                                                child: ElevatedButton(
                                                    onPressed: () {
                                                      setState(() {
                                                        isTextFieldShown[2] =
                                                            true;
                                                      });
                                                    },
                                                    child: Text((kitchenConstraints?.pan ?? 0).toString()))),
                                        color: Colors.deepOrange[200],
                                      ),


                                      // number of bowls
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: isTextFieldShown[3]
                                            ? TextField(
                                                focusNode: focusNodes[3],
                                                controller: controllers[3],
                                                keyboardType:
                                                    TextInputType.number,
                                                decoration: InputDecoration(
                                                  border: OutlineInputBorder(),
                                                ),
                                              )
                                            : SizedBox(
                                                height: 50,
                                                child: ElevatedButton(
                                                    onPressed: () {
                                                      setState(() {
                                                        isTextFieldShown[3] =
                                                            true;
                                                      });
                                                    },
                                                    child: Text((kitchenConstraints?.bowl ?? 0).toString()))),
                                        color: Colors.deepOrange[200],
                                      ),

                                      // number of cutting boards
                                      Container(
                                        padding: const EdgeInsets.all(8),
                                        child: isTextFieldShown[4]
                                            ? TextField(
                                                focusNode: focusNodes[4],
                                                controller: controllers[4],
                                                keyboardType:
                                                    TextInputType.number,
                                                decoration: InputDecoration(
                                                  border: OutlineInputBorder(),
                                                ),
                                              )
                                            : SizedBox(
                                                height: 50,
                                                child: ElevatedButton(
                                                    onPressed: () {
                                                      setState(() {
                                                        isTextFieldShown[4] =
                                                            true;
                                                      });
                                                    },
                                                    child: Text((kitchenConstraints?.cuttingBoard ?? 0).toString()))),
                                        color: Colors.deepOrange[200],
                                      ),
                                    ])),
                            SizedBox(
                                width: 360,
                                height: 60,
                                child: ElevatedButton(
                                    onPressed: () {
                                      KitchenConstraints kitchenConstraints =
                                          KitchenConstraints(
                                              userEmail: myEmail,
                                              oven: int.tryParse(
                                                      controllers[0].text) ??
                                                  0,
                                              pot: int.tryParse(
                                                      controllers[1].text) ??
                                                  0,
                                              pan: int.tryParse(
                                                      controllers[2].text) ??
                                                  0,
                                              bowl: int.tryParse(
                                                      controllers[3].text) ??
                                                  0,
                                              cuttingBoard: int.tryParse(
                                                      controllers[4].text) ??
                                                  0);

                                      addKitchenConstriants(kitchenConstraints);
                                      setState(() {
                                        dataModel.loadHomePage();
                                        for (int i = 0;
                                            i < isTextFieldShown.length;
                                            i++) {
                                          isTextFieldShown[i] = false;
                                        }
                                      });
                                    },
                                    child:
                                        Text("Save your kitchen constraints!")))
                          ]);
                        })
                      ],
                    ),
                  )))));
    });
  }
}
