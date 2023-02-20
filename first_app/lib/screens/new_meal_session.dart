import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'instructions_screen.dart';

class NewMealSession extends StatefulWidget {
  const NewMealSession({Key? key}) : super(key: key);

  @override
  State<NewMealSession> createState() => _NewMealSessionState();
}

class _NewMealSessionState extends State<NewMealSession> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("New Meal Session")),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
          Padding (
            padding: const EdgeInsets.all(8.0),
            child: ElevatedButton(
                onPressed: () {
                  Navigator.of(context).push(MaterialPageRoute(
                      builder: (context) => InstructionsScreen()));
                },
                child: Text("Start new session!"))
          ),
        ]),
      ),
    );
  }
}
