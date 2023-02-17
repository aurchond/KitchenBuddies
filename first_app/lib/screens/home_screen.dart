import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:first_app/local_notification_service.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/screens/received_instructions_screen.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'package:first_app/provider/notification_provider.dart';

import 'package:http/http.dart' as http;

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  // @override
  // void didChangeDependencies() {
  //   final authProvider = Provider.of<AuthProvider>(context,listen: false);
  //   if (mounted) authProvider.updateEmailVerificationState();
  //   super.didChangeDependencies();
  // }

  @override
  void initState () {
    //in terminated state
    FirebaseMessaging.instance.getInitialMessage().then((value) {
     if (value != null) {
        Navigator.of(context).push(new MaterialPageRoute(
            builder: (context) => ReceivedInstructionScreen(message: value)));
        print(value.toString());
      }
      });

    //in foreground listener
    // if you have app open?
    FirebaseMessaging.onMessage.listen((event) {
      LocalNotificationService.init();
      LocalNotificationService.displayNotification(event);

      Navigator.of(context).push(new MaterialPageRoute(
            builder: (context) => ReceivedInstructionScreen(message: event)));
        print(event.toString());
    });

    //in background state but not terminated
    FirebaseMessaging.onMessageOpenedApp.listen((event) {
    });

    // TODO: implement initstate

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final fcmProvider = Provider.of<NotificationProvider>(context);

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
          body: StreamBuilder<QuerySnapshot>(
              builder: (context, snapshot) {
                if (snapshot.hasData) {

                  return ListView.builder(
                      itemBuilder: (context, index) {
                        return Card(
                            child: ListTile(
                                onTap: () {
                                  fcmProvider.sendNotification(
                                      token: snapshot.data!
                                          .docs[index]["token"],
                                      title: snapshot.data!.docs[index]
                                      ["user_name"],
                                      body: "Notification Test");
                                },
                                // indexing user from the db
                                title: Text(
                                    snapshot.data!.docs[index]["user_name"])));
                      },
                      itemCount: snapshot.data!.docs.length);
                } else {
                  return const Center(child: CircularProgressIndicator());
                }
              },
              stream:
                  FirebaseFirestore.instance.collection("users").snapshots())
          // body: Center(
          //   child: model.emailVerified ?? false
          //       ? const Text("Email verified")
          //       : const Text("Email is not verified"),
          // ),
          );
    });
  }
}
