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

class HomePageScreen extends StatefulWidget {
  const HomePageScreen({Key? key}) : super(key: key);

  @override
  State<HomePageScreen> createState() => _HomePageScreenState();
}

class _HomePageScreenState extends State<HomePageScreen> {

    // final fcmProvider = Provider.of<NotificationProvider>(context);
    @override
    Widget build(BuildContext context) {
      return Scaffold(
        body: const Center(
          child: Text("Home Page"),
        ),
      );

  }
}
