import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:first_app/local_notification_service.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/screens/new_meal_session_screen.dart';
import 'package:first_app/screens/past_recipes_screen.dart';
import 'package:flutter/material.dart';
import 'package:google_nav_bar/google_nav_bar.dart';
import 'package:provider/provider.dart';

import 'package:first_app/provider/notification_provider.dart';

import 'current_meal_session.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _selectedIndex = 0;
  static const TextStyle optionStyle =
  TextStyle(fontSize: 30, fontWeight: FontWeight.w600);
  static const List<Widget> _widgetOptions = <Widget>[ //TODO: is this safe?
    CurrentMealSession(),
    NewMealSession(),
    PastRecipes(),
  ];

  // @override
  // void didChangeDependencies() {
  //   final authProvider = Provider.of<AuthProvider>(context,listen: false);
  //   if (mounted) authProvider.updateEmailVerificationState();
  //   super.didChangeDependencies();
  // }

  @override
  void initState() {
    //in terminated state
    FirebaseMessaging.instance.getInitialMessage().then((value) {});

    //in foreground listener
    FirebaseMessaging.onMessage.listen((event) {
      LocalNotificationService.init();
      LocalNotificationService.displayNotification(event);
    });

    //in background state but not terminated
    FirebaseMessaging.onMessageOpenedApp.listen((event) {});

    // TODO: implement initstate

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    // final fcmProvider = Provider.of<NotificationProvider>(context);

    return Consumer<AuthProvider>(builder: (context, model, _) {
      return Scaffold(
          bottomNavigationBar: Container(
            color: Colors.deepOrange.shade300,
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 20),
              child: GNav(
                gap: 10,
                backgroundColor: Colors.deepOrange.shade300,
                color: Colors.white,
                activeColor: Colors.white,
                tabBackgroundColor: Colors.deepOrange.shade200,
                padding: EdgeInsets.all(16),
                tabs: const [
                  GButton(icon: Icons.home, text: "Home"),
                  GButton(
                      icon: Icons.favorite_outline_rounded,
                      text: "Meal Session"),
                  GButton(icon: Icons.search, text: "Past Recipes"),
                ],
                selectedIndex: _selectedIndex,
                onTabChange: (index) {
                  setState(() {
                    _selectedIndex = index;
                  });
                },
              ),
            ),
          ),
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
          body: Center ( // child should navigate to selected screen
            child: _widgetOptions.elementAt(_selectedIndex),
          )

          /// THIS IS FROM PUSH NOTIFICATION TESTING! ///
          /*body: StreamBuilder<QuerySnapshot>(
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
                  FirebaseFirestore.instance.collection("users").snapshots())*/
          );
    });
  }
}
