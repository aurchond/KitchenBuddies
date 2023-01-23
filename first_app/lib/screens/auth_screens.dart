import 'package:first_app/screens/email_pass_screen.dart';
import 'package:first_app/widgets/auth_button.dart';
import 'package:flutter/material.dart';

class AuthScreen extends StatelessWidget {
  const AuthScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        // create UI with buttons for user to get to login
        body: Center(
            child: Padding(
                padding: const EdgeInsets.all(20),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text(
                      "Select Auth Provider",
                      style:
                          TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
                    ),
                    AuthButton(
                        iconData: Icons.email,
                        title: "Email/Password",
                        onTap: () {
                          Navigator.of(context).push(MaterialPageRoute(
                              builder: (_) => const EmailPassScreen()));
                        }),
                  ],
                ))));
  }
}
