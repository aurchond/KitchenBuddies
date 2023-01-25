import 'package:first_app/provider/auth_provider.dart';
import 'package:provider/provider.dart';
import 'package:first_app/widgets/custom_text_field.dart';
import 'package:flutter/src/foundation/key.dart';
import 'package:flutter/src/widgets/container.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:first_app/widgets/custom_text_field.dart';

import 'package:flutter/material.dart';

class EmailPassScreen extends StatelessWidget {
  const EmailPassScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<AuthProvider>(builder: (context, model, _) {
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
                if (model.authType == AuthType.signUp) // only showing email if we are signing up
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
                      model.setAuthType(); // only setting the authtype for the second text field because this changes the screen layout
                    },
                    child: model.authType == AuthType.signUp
                        ? const Text("Already Have An Account")
                        : const Text("Create An Account")),
              ],
            ),
          ),
        ),
      );
    });
  }
}
