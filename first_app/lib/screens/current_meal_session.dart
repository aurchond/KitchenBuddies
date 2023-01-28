import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../provider/notification_provider.dart';

class CurrentMealSession extends StatefulWidget {
  const CurrentMealSession({Key? key}) : super(key: key);

  @override
  State<CurrentMealSession> createState() => _CurrentMealSessionState();
}

class _CurrentMealSessionState extends State<CurrentMealSession> {
  @override
  Widget build(BuildContext context) {
    final fcmProvider = Provider.of<NotificationProvider>(context);

    return Scaffold(
        body: StreamBuilder<QuerySnapshot>(
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
  }
}
