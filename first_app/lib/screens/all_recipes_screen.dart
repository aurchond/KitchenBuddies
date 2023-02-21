import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../backend_processing/data_class.dart';

class AllRecipes extends StatefulWidget {
  const AllRecipes({Key? key}) : super(key: key);

  @override
  State<AllRecipes> createState() => _AllRecipesState();
}

class _AllRecipesState extends State<AllRecipes> {
  @override
  Widget build(BuildContext context) {
    //final postModel = Provider.of<DataClass>(context); //TODO: data and service classs for past recipes

    return Scaffold(
      appBar: AppBar(title: const Text("All Recipes")),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: [
            Container(
                margin: EdgeInsets.only(top: 20, bottom: 20),
                child: Text(
                  "Welcome to your kitchen!",
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                )),
            new Expanded(
                child: new ListView.builder(
                    itemCount:
                        10, //will be something like postModel.post?.recipes?.length
                    itemBuilder: (BuildContext context, int index) {
                      return Text("Recipe " + (index + 1).toString());
                    })),
          ],
        ),
      ),
    );
  }
}
