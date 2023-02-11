import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:first_app/backend_processing/data_model.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/provider/notification_provider.dart';
import 'package:first_app/screens/email_pass_screen.dart';
import 'package:first_app/screens/home_screen.dart';
import 'package:first_app/screens/instructions_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/foundation/key.dart';
import 'package:first_app/screens/chat_screens.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:provider/provider.dart';

import 'dart:convert';

import 'package:http/http.dart' as http;

import 'backend_processing/data_class.dart';
import 'backend_processing/data_model.dart';
import 'keys.dart';

void main() async {
  //logged in
/*  String url = "https://mocki.io/v1/041c7ac2-1d75-453a-82ec-be5637f4cb16";
  final response = await http.get(Uri.parse(url));
  print(response.body);*/

  //UserRecipeDetails  userDetails = UserRecipeDetails.fromJson(jsonDecode(response.body));
  //print(userDetails.users?.user)

  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
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
            title: 'FlutterChat',
            debugShowCheckedModeBanner: false,
            theme: ThemeData(
              primarySwatch: Colors.deepOrange,
            ),
            home: InstructionsScreen()
            // StreamBuilder(
            //   stream: FirebaseAuth.instance.authStateChanges(),
            //   builder: (context, snapshot) {
            //     if (snapshot.hasData) {
            //       return const HomeScreen();
            //     }
            //     else {
            //       return const EmailPassScreen();
            //     }
            //   }
            // ),
            ));
  }
}

