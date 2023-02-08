import 'package:auto_route/annotations.dart';
import 'package:auto_route/auto_route.dart';
import 'package:first_app/screens/home_page_screen.dart';
import 'package:first_app/widgets/material_auto_router.gr.dart';
import 'package:flutter/material.dart';

class NewMealSession extends StatefulWidget {
  const NewMealSession({Key? key}) : super(key: key);

  @override
  State<NewMealSession> createState() => _NewMealSessionState();
}

class _NewMealSessionState extends State<NewMealSession> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('New Meal Session')),
      body: Center(
        child: ElevatedButton(
          onPressed: () => context.router.push(CurrentMealSession()),
          child: Text('Welcome to New Session'),
        ),
      ),
    );
  }
}
