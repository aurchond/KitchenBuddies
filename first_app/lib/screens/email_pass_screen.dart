import 'package:firebase_auth/firebase_auth.dart';
import 'package:first_app/provider/auth_provider.dart';
import 'package:first_app/screens/home_screen.dart';
import 'package:first_app/widgets/custom_text_field.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class EmailPassScreen extends StatelessWidget {
  const EmailPassScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<AuthProvider>(builder: (context, model, _) {
      return StreamBuilder(
          stream: FirebaseAuth.instance.authStateChanges(),
          builder: (context, snapshot) {
            if (!snapshot.hasData) {
              return Scaffold(
                body: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 20),
                  child: Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        CustomTextField(
                          controller: model.emailController,
                          hintText: "Email",
                          iconData: Icons.email,
                        ),
                        if (model.authType == AuthType.signUp)
                          CustomTextField(
                            controller: model.usernameController,
                            hintText: "User name",
                            iconData: Icons.person,
                          ),
                        CustomTextField(
                          controller: model.passwordController,
                          hintText: "Password",
                          iconData: Icons.password,
                        ),
                        TextButton(
                            onPressed: () {
                              model.authenticate();
                            },
                            child: model.authType == AuthType.signUp
                                ? const Text("Sign Up")
                                : const Text("Sign In")),
                        TextButton(
                            onPressed: () {
                              model.setAuthType();
                            },
                            child: model.authType == AuthType.signUp
                                ? const Text("Already have an account")
                                : const Text("Create an account")),
                        if (model.authType == AuthType.signIn)
                          TextButton(
                              onPressed: () {
                                //model.resetPassword(context);
                              },
                              child: const Text("Reset password")),
                      ],
                    ),
                  ),
                ),
              );
            } else {
              return const HomeScreen();
            }
          });
    });
  }
}
