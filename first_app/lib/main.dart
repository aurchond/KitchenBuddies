import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:first_app/backend_processing/data_model.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/provider/notification_provider.dart';
import 'package:first_app/screens/email_pass_screen.dart';
import 'package:first_app/screens/home_screen.dart';
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
            primarySwatch: Colors.green,
          ),
          home: ProviderDemoScreen()
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

class ProviderDemoScreen extends StatefulWidget {
  const ProviderDemoScreen({Key? key}) : super(key: key);

  @override
  _ProviderDemoScreenState createState() => _ProviderDemoScreenState();
}

class _ProviderDemoScreenState extends State<ProviderDemoScreen> {
  @override
  void initState() {
    super.initState();
    final postModel = Provider.of<DataClass>(context, listen: false);
    postModel.getPostData();
  }

  @override
  Widget build(BuildContext context) {
    final postModel = Provider.of<DataClass>(context);

    return Scaffold(
      appBar: AppBar(
        title: Text("Provider Demo"),
      ),
      body: Container(
        padding: EdgeInsets.all(20),
        child: postModel.loading?Center(
          child: Container(
            child: SpinKitThreeBounce(
              itemBuilder: (BuildContext context, int index) {
                return DecoratedBox(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    color: index.isEven ? Colors.red : Colors.green,
                  ),
                );
              },
            ),
          ),
        ):Center(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                margin: EdgeInsets.only(top: 40, bottom: 20),
                child: Text(
                  postModel.post?.userEmail ?? "",
                  style:
                  TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                ),
              ),
              Container(
                child: Text((postModel.post?.recipeStep)?[0].instructions ?? ""),
              )
            ],
          ),
        ),
      ),
    );
  }
}

