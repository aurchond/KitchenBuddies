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
          final splitMessage = (event.data.toString().split('title: '))[1].split('}'); //TODO: do this in regex
          //we will categorize our notifications based on title
          //notifications to send to other users about completing a step
          //notification that redirects user to instruction page
          if (splitMessage[0] != "Step Completed") {
            Navigator.of(context).push(MaterialPageRoute( //if this starts bugging out again, use pushReplacement
                builder: (context) =>
                    ReceivedInstructionScreen(message: event)));
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
    super.dispose();
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
          );
    });
  }
}
