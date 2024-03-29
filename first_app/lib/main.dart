import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/provider/notification_provider.dart';
import 'package:first_app/screens/email_pass_screen.dart';
import 'package:first_app/screens/home_screen.dart';
import 'package:first_app/screens/meal_session_screens/instructions_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/foundation/key.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:provider/provider.dart';
import 'package:first_app/local_notification_service.dart';
import 'package:first_app/widgets/bottom_navigation.dart';

import 'dart:convert';

import 'package:http/http.dart' as http;

import 'backend_processing/data_class.dart';
import 'data_models/recipe_info_model.dart';
import 'helpers/globals.dart';
import 'helpers/keys.dart';

Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  print("Handling a background message: ${message.messageId}");
}

void main() async {
  //logged in
/*  String url = "https://mocki.io/v1/041c7ac2-1d75-453a-82ec-be5637f4cb16";
  final response = await http.get(Uri.parse(url));
  print(response.body);*/

  //UserRecipeDetails  userDetails = UserRecipeDetails.fromJson(jsonDecode(response.body));
  //print(userDetails.users?.user)

  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  await LocalNotificationService.init();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => AuthProvider()),
          ChangeNotifierProvider(create: (_) => NotificationProvider()),
          ChangeNotifierProvider(create: (_) => DataClass()),
        ],
        child: MaterialApp(
            scaffoldMessengerKey: Keys.scaffoldMessengerKey,
            title: 'KitchenBuddies',
            debugShowCheckedModeBanner: false,
            theme: ThemeData(
              primarySwatch: Colors.deepOrange,
            ),
            home: StreamBuilder<User?>(
              stream: FirebaseAuth.instance.authStateChanges(),
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  User? user = snapshot.data;
                  myEmail = user?.email ?? "";

                  FirebaseFirestore.instance
                      .collection("users")
                      .doc(user?.uid)
                      .get()
                      .then((value) {
                    myUsername = value.data()?["user_name"];
                  });
                  return BottomNavigation();
                }
                else {
                  return const EmailPassScreen();
                }
              }
            ),
            ));
  }
}

