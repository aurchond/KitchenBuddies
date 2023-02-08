import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:first_app/local_notification_service.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/screens/new_meal_session_screen.dart';
import 'package:first_app/screens/past_recipes_screen.dart';
import 'package:flutter/material.dart';
import 'package:google_nav_bar/google_nav_bar.dart';
import 'package:provider/provider.dart';

import 'package:persistent_bottom_nav_bar/persistent_tab_view.dart';

import 'package:first_app/provider/notification_provider.dart';
import 'screens/current_meal_session.dart';
import 'screens/home_page_screen.dart';

// This class is the "master" class which controls everything after the user logs in
class Master extends StatefulWidget {
  const Master({Key? key}) : super(key: key);

  @override
  State<Master> createState() => _MasterState();
}

class _MasterState extends State<Master> {
  int _selectedIndex = 0;
  // static const List<Widget> _widgetOptions = <Widget>[ //TODO: is this safe?
  //   HomePageScreen(),
  //   //CurrentMealSession(),
  //   NewMealSession(),
  //   PastRecipes(),
  // ];

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
    return Consumer<AuthProvider>(builder: (context, model, _) {
      return Scaffold(
        body: Navigator(
          onGenerateRoute: (settings) {
            Widget page = HomePageScreen();

            if (_selectedIndex == 1) {
              if (settings.name == 'NewMealSession') {
                page = NewMealSession();
              } else
                page = CurrentMealSession();
            }

            if (_selectedIndex == 2) page = PastRecipes();

            print("_selectedIndex: " + _selectedIndex.toString());

            return MaterialPageRoute(builder: (_) => page);
          },
        ),
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
                    icon: Icons.favorite_outline_rounded, text: "Meal Session"),
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

        // selectedIndex: _selectedIndex,
        // onTabChange: (index) {
        //   setState(() {
        //     _selectedIndex = index;
        //   });
        // },
        //     ),
        //   ),
        // ),
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
        // body: Center(
        //   // child should navigate to selected screen
        //   child: _widgetOptions.elementAt(_selectedIndex),
        // )

      );
    });
  }
}
