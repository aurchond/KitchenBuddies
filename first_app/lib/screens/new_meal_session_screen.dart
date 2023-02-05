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
          onPressed: () => Navigator.pushNamed(context, 'CurrentMealSession'),
          child: Text('Welcome to New Session'),
        ),
      ),
    );
  }
}
