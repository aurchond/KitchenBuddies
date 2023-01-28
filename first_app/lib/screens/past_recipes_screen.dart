import 'package:flutter/material.dart';

class PastRecipes extends StatefulWidget {
  const PastRecipes({Key? key}) : super(key: key);

  @override
  State<PastRecipes> createState() => _PastRecipesState();
}

class _PastRecipesState extends State<PastRecipes> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: const Center(
        child: Text("Past Recipes"),
      ),
    );
  }
}
