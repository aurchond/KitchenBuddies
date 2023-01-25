import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../provider/auth_provider.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<AuthProvider>(builder: (context, model, _) {
      return Scaffold(
        appBar: AppBar(
          actions: [
            IconButton(
              onPressed: () {
                model.logOut();
              },
              icon: const Icon(Icons.logout),
            )
          ],
        ),
        body: const Center(
          child: Text("Kitchen Buddies"),
        ),
      );
    });
  }
}
