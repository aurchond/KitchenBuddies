import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:first_app/dashboard.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/provider/notification_provider.dart';
import 'package:first_app/screens/email_pass_screen.dart';
import 'package:first_app/master.dart';
import 'package:first_app/widgets/material_auto_router.gr.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'keys.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  final _appRouter = AppRouter();
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerDelegate: _appRouter.delegate(),
      routeInformationParser: _appRouter.defaultRouteParser(),
    );

    /*return MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => AuthProvider()),
          ChangeNotifierProvider(create: (_) => NotificationProvider()),
        ],
        child: MaterialApp(
          scaffoldMessengerKey: Keys.scaffoldMessengerKey,
          title: 'FlutterChat',
          debugShowCheckedModeBanner: false,
          theme: ThemeData(
            primarySwatch: Colors.deepOrange, //TODO: fix deep orange colour
          ),
          home: StreamBuilder(
            stream: FirebaseAuth.instance.authStateChanges(),
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                //return const Master();
                return DashboardPage();
              }
              else {
                return const EmailPassScreen();
              }
            }
          ),
        ));*/
  }
}
