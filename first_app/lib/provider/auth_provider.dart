import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

import '../helpers/custom_text_field.dart';
import '../helpers/keys.dart';

class AuthProvider extends ChangeNotifier {
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController usernameController = TextEditingController();

  AuthType _authType = AuthType.signIn;

  AuthType get authType => _authType;

  // auth has a separate db (just for credentials) from firestore
  FirebaseAuth firebaseAuth = FirebaseAuth.instance;
  FirebaseFirestore firebaseFirestore = FirebaseFirestore.instance;

  setAuthType() {
    _authType =
        _authType == AuthType.signIn ? AuthType.signUp : AuthType.signIn;
    notifyListeners();
  }

  // get this device's token to save in the Firebase Auth later
  Future<String> getDeviceToken() async {
    final FirebaseMessaging firebaseMessaging = FirebaseMessaging.instance;

    final String? token =
        await firebaseMessaging.getToken(); // get the device's token
    return token!;
  }

  // saving emails/passwords within firebase
  authenticate() async {
    UserCredential userCredential;

    try {
      // when successfully signs up or signs in we pass the user onto the home page
      if (_authType == AuthType.signUp) {
        userCredential = await firebaseAuth.createUserWithEmailAndPassword(
            email: emailController.text, password: passwordController.text);

        String token = await getDeviceToken();

        await firebaseFirestore
            .collection("users")
            .doc(userCredential.user!.uid)
            .set({
          "email": userCredential.user!.email,
          "uid": userCredential.user!.uid,
          "user_name": usernameController.text,
          "token": token,
        });
      }
      if (_authType == AuthType.signIn) {
        userCredential = await firebaseAuth.signInWithEmailAndPassword(
            email: emailController.text, password: passwordController.text);

        String token = await getDeviceToken();

        // update the device token if the user signs in on another device so that the most recent one is saved
        await firebaseFirestore
            .collection("users")
            .doc(userCredential.user!.uid);
      }
    } on FirebaseAuthException catch (error) {
      Keys.scaffoldMessengerKey.currentState!.showSnackBar(
          SnackBar(content: Text(error.code), backgroundColor: Colors.red));
    } catch (error) {
      Keys.scaffoldMessengerKey.currentState!.showSnackBar(SnackBar(
          content: Text(error.toString()), backgroundColor: Colors.red));
    }
  }

  TextEditingController resetEmailController = TextEditingController();
  resetPassword(BuildContext context) {
    showDialog(
        context: context,
        builder: (_) {
          return AlertDialog(
            title: const Text("Enter your email"),
            content: CustomTextField(
                iconData: Icons.email,
                hintText: "Enter email",
                controller: resetEmailController),
            actions: [
              TextButton(
                  onPressed: () async {
                    final navigator = Navigator.of(context).pop();
                    try {
                      await firebaseAuth.sendPasswordResetEmail(
                          email: resetEmailController.text);
                      Keys.scaffoldMessengerKey.currentState!
                          .showSnackBar(const SnackBar(
                        content: Text("Email sent successfully"),
                        backgroundColor: Colors.green,
                      ));
                      navigator;
                    } catch (e) {
                      Keys.scaffoldMessengerKey.currentState!
                          .showSnackBar(SnackBar(
                        content: Text(e.toString()),
                        backgroundColor: Colors.red,
                      ));
                      navigator;
                    }
                  },
                  child: const Text("Submit"))
            ],
          );
        });
  }

  logOut() async {
    try {
      await firebaseAuth.signOut();
    } catch (error) {
      Keys.scaffoldMessengerKey.currentState!.showSnackBar(SnackBar(
          content: Text(error.toString()), backgroundColor: Colors.red));
    }
  }
}

enum AuthType {
  signUp,
  signIn,
}
