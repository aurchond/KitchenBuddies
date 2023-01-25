import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/screens/home_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/foundation/key.dart';
import 'package:first_app/screens/chat_screens.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:first_app/screens/auth_screens.dart';
import 'package:provider/provider.dart';

import 'keys.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
        providers: [ChangeNotifierProvider(create: (_) => AuthProvider())],
        child: MaterialApp(
          scaffoldMessengerKey: Keys.scaffoldMessengerKey,
          title: 'FlutterChat',
          debugShowCheckedModeBanner: false,
          theme: ThemeData(
            primarySwatch: Colors.blue,
          ),
          //home: ChatScreen(),
          home: StreamBuilder(
            stream: FirebaseAuth.instance.authStateChanges(),
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                return const HomeScreen();
              }
              else {
                return const AuthScreen();
              }
            }
          ),
        ));
  }
}
