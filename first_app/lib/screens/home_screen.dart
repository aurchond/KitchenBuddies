import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:first_app/local_notification_service.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/screens/meal_session_screens/received_instructions_screen.dart';
import 'package:first_app/helpers/input_text_button.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'package:first_app/provider/notification_provider.dart';

import 'package:http/http.dart' as http;

import '../backend_processing/data_class.dart';
import '../backend_processing/get_requests.dart';
import '../helpers/tile_decorated.dart';

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

  final burnerController = TextEditingController();
  late FocusNode burnerFocusNode = FocusNode();

  final potController = TextEditingController();
  late FocusNode potFocusNode = FocusNode();

  final panController = TextEditingController();
  late FocusNode panFocusNode = FocusNode();

  final knifeController = TextEditingController();
  late FocusNode knifeFocusNode = FocusNode();

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

  @override
  void dispose() {
    // dispose of resources for input text buttons
    textController.dispose();
    myFocusNode.dispose();

    burnerController.dispose();
    burnerFocusNode.dispose();

    potController.dispose();
    potFocusNode.dispose();

    panController.dispose();
    panFocusNode.dispose();

    knifeController.dispose();
    knifeFocusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final fcmProvider = Provider.of<NotificationProvider>(context);
    final dataModel = Provider.of<DataClass>(context);

    /// dummy data ///
    // todo AD: use getKitchenConstraints APIs

    //final List<Map> myFriends =
    //     List.generate(6, (index) => {"id": index, "name": "Friend $index"})
    //         .toList();
    String? dropdownValue = dataModel.skillLevel;
    List<String>? myFriends = dataModel.friendsList?.friends;


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
        body: Column(
          children: [
            /// friend setup ///
            //todo AD: use AddFriend API
            Container(
                margin: EdgeInsets.only(top: 20, bottom: 15),
                child: Text(
                  "Your friends:",
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                )),
            Expanded(
                child: Container(
              padding: const EdgeInsets.all(10),
              child: GridView.builder(
                  gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
                      maxCrossAxisExtent: 300,
                      childAspectRatio: 5 / 2,
                      crossAxisSpacing: 10,
                      mainAxisSpacing: 10),
                  itemCount: myFriends?.length,
                  itemBuilder: (BuildContext context, index) {
                    return TileDecorated(
                        Colors.deepOrange.shade200,
                        Icon(
                          Icons.person,
                          color: Colors.deepOrange.shade700,
                        ),
                        Text(myFriends?[index] ?? "",
                            style: TextStyle(
                                fontSize: 16, fontWeight: FontWeight.w400)),
                        Text(""),
                        false);
                  }),
            )),

            inputTextButton(textController, "Enter your friend's email",
                "Add friend!", myFocusNode),

            /// old push notif stuff ///
            Expanded(
                child: Padding(
              padding: const EdgeInsets.all(10),
              child: Container(
                alignment: Alignment.center,
                decoration: BoxDecoration(
                  color: Colors.deepOrange.shade100,
                ),
                child: StreamBuilder<QuerySnapshot>(
                    builder: (context, snapshot) {
                      if (snapshot.hasData) {
                        return ListView.builder(
                            itemBuilder: (context, index) {
                              return Card(
                                  child: ListTile(
                                      onTap: () {
                                        fcmProvider.sendNotification(
                                            token: snapshot.data!.docs[index]
                                                ["token"],
                                            title: snapshot.data!.docs[index]
                                                ["user_name"],
                                            body: "Notification Test");
                                      },
                                      // indexing user from the db
                                      title: Text(snapshot.data!.docs[index]
                                          ["user_name"])));
                            },
                            itemCount: snapshot.data!.docs.length);
                      } else {
                        return const Center(child: CircularProgressIndicator());
                      }
                    },
                    stream: FirebaseFirestore.instance
                        .collection("users")
                        .snapshots()),
              ),
            )),

            /// set skill level ///
            //todo AD: use AddSkillLevel API
            StatefulBuilder(
                builder: (BuildContext context, StateSetter setState) {
              return Row(
                children: [
                  Container(
                      margin: EdgeInsets.all(25),
                      child: Text(
                        "Change your skill level:",
                        style: TextStyle(
                            fontWeight: FontWeight.bold, fontSize: 15),
                      )),
                  DropdownButton<String>(
                    value: dropdownValue,
                    icon: const Icon(Icons.arrow_downward),
                    elevation: 16,
                    style: const TextStyle(color: Colors.deepOrange),
                    underline: Container(
                      height: 2,
                      color: Colors.deepOrangeAccent,
                    ),
                    onChanged: (String? value) {
                      // This is called when the user selects an item.
                      setState(() {
                        dropdownValue = value!;
                        //TODO: handle API call
                      });
                    },
                    items: skillList.map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value, style: TextStyle(fontSize: 18)),
                      );
                    }).toList(),
                  )
                ],
              );
            }),

            /// set kitchen constraints
            //todo AD: use AddKitchenConstraints API
            Container(
                margin: EdgeInsets.only(top: 5, bottom: 20),
                child: Text(
                  "Customize your kitchen:",
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                )),
            Flexible(
                child: ListView(
                  children: [
                    inputTextButton(burnerController, "Change # of burners",
                        "Update # of burners", burnerFocusNode),
                    inputTextButton(potController, "Change # of pots",
                        "Update # of pots", potFocusNode),
                    inputTextButton(panController, "Change # of pans",
                        "Update # of pans", panFocusNode),
                    inputTextButton(knifeController, "Change # of knives",
                        "Update # of knives", knifeFocusNode),
                  ],
            ))
          ],
        ),
      );
    });
  }
}
