import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/container.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

class ChatScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: ListView.builder(
      itemCount: 10,
      itemBuilder: (ctx, index) => Container(
        padding: EdgeInsets.all(8),
        child: Text("This works!"),
      ),
    )
        // floatingActionButton: FloatingActionButton(
        //   child: Icon(Icons.add),
        //   onPressed: () {
        //     Firestore.instance
        //         .collection(
        //             'chats/jNdVUnN5twS6YBmalzzC/messages/') // collections / document / collection
        //         .snapshots()
        //         .listen((data) {
        //       print(data);
        //     }); //returns a screen -> new values whenever data changes
        //   },

        );
  }
}
