import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

import '../keys.dart';

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

  // saving emails/passwords within firebase
  // TODO: copy UserCredential to actual database later
  authenticate() async {
    UserCredential userCredential;

    try {
      // when successfully signs up or signs in we pass the user onto the home page
      if (_authType == AuthType.signUp) {
        userCredential = await firebaseAuth.createUserWithEmailAndPassword(
            email: emailController.text, password: passwordController.text);
        firebaseFirestore
            .collection("users")
            .doc(userCredential.user!.uid)
            .set({
          "email": userCredential.user!.email,
          "uid": userCredential.user!.uid,
          "user_name": usernameController.text,
        });
      }
      if (_authType == AuthType.signIn) {
        userCredential = await firebaseAuth.signInWithEmailAndPassword(
            email: emailController.text, password: passwordController.text);
      }
    } on FirebaseAuthException catch (error) {
      Keys.scaffoldMessengerKey.currentState!.showSnackBar(
          SnackBar(content: Text(error.code), backgroundColor: Colors.red));
    } catch (error) {
      Keys.scaffoldMessengerKey.currentState!.showSnackBar(SnackBar(
          content: Text(error.toString()), backgroundColor: Colors.red));
    }
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
