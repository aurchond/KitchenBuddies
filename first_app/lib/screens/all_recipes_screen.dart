import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../backend_processing/data_class.dart';

class AllRecipes extends StatefulWidget {
  const AllRecipes({Key? key}) : super(key: key);

  @override
  State<AllRecipes> createState() => _AllRecipesState();
}

//no field for isSelected
class recipeTile {
  final String? title;
  final String? ingredients; //we will join list of ingredients with .join(", ")
  final int? totalTime;
  final DateTime? lastDateMade;

  recipeTile({this.title, this.ingredients, this.totalTime, this.lastDateMade});
}

class _AllRecipesState extends State<AllRecipes> {
  @override
  Widget build(BuildContext context) {
    List<recipeTile> _data = [
      recipeTile(title: "Option ", ingredients: "Ingredients", totalTime: 60),
      recipeTile(title: "Option ", ingredients: "Ingredients", totalTime: 60),
      recipeTile(title: "Option ", ingredients: "Ingredients", totalTime: 60),
      recipeTile(title: "Option ", ingredients: "Ingredients", totalTime: 60),
      recipeTile(title: "Option ", ingredients: "Ingredients", totalTime: 60),
    ];

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
            Expanded(
              child: new ListView.builder(
                  itemCount: _data.length,
                  itemBuilder: (context, index) {
                    return SizedBox(
                      width: 100,
                      height: 100,
                      child: Center(
                        child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Container(
                              decoration: BoxDecoration(
                                border: Border.all(
                                    width: 3,
                                    color: Colors.deepOrange.shade300),
                                borderRadius: BorderRadius.circular(20),
                              ),
                              child: ListTile(
                                leading: Icon(Icons.fastfood),
                                title: Text(
                                  _data[index].title! +
                                      " " +
                                      (index + 1).toString(),
                                ),
                                subtitle: Text(
                                  _data[index].ingredients! +
                                      " " +
                                      (index + 1).toString() +
                                      "\nTotal time: " +
                                      _data[index].totalTime.toString() +
                                      " minutes",

                                ), isThreeLine: true,
                              ),
                            )),
                      ),
                    );
                  }),
            )
          ],
        ),
      ),
    );
  }
}
