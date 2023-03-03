import 'package:first_app/helpers/input_text_button.dart';
import 'package:first_app/helpers/tile_decorated.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../backend_processing/data_class.dart';
import '../helpers/alert_dialog.dart';

class AllRecipes extends StatefulWidget {
  const AllRecipes({Key? key}) : super(key: key);

  @override
  State<AllRecipes> createState() => _AllRecipesState();
}

//no field for isSelected
class recipeTile {
  final String? title;
  final String?
      ingredients; //todo: we will join list of ingredients with .join(", ")
  final int? totalTime;
  final DateTime? lastDateMade;

  recipeTile({this.title, this.ingredients, this.totalTime, this.lastDateMade});
}

class _AllRecipesState extends State<AllRecipes> {
  final recipeByUrlController = TextEditingController();
  late FocusNode myFocusNode = FocusNode();

  // code for inputting recipes as text
  late String valueText;
  final recipeByTextController = TextEditingController();

  // callback functions for alert dialog
  buttonCallback() {
    setState(() {
      Navigator.pop(context);
    });
  }

  parseCallback() {
    setState(() {
      Navigator.pop(context);
    });

    int timeIndex = valueText.indexOf("Total Time:");
    int ingredientIndex = valueText.indexOf("Ingredients:");
    int instructionsIndex = valueText.indexOf("Instructions:");

    String recipeTitle = valueText.substring(0, timeIndex);
    print(recipeTitle);

    //hardcode length of "total time" string bc im too lazy to declare variables for each pattern
    String totalTime = valueText.substring(timeIndex+12, ingredientIndex);
    print(totalTime);

    //add or subtract 1 to index to account for newline character we don't want
    String ingredients = valueText.substring(ingredientIndex+13, instructionsIndex-1);
    List<String> ingredientList = ingredients.split('\n');
    print(ingredientList);

    String instructions = valueText.substring(instructionsIndex+14);
    List<String> instructionList = instructions.split('\n');
    print(instructionList);

    Map<String, dynamic> data;

    // populate json with string data??
    data = {
      "title": recipeTitle,
      "total_time": totalTime,
      "ingredients": {
        ingredientList,
      },
      "instructions": {
       instructionList,
      },
    };
    //print(data);
  }

  textCallback(String value) {
    setState(() {
      valueText = value;
    });
  }

  @override
  void dispose() {
    recipeByTextController.dispose();
    recipeByUrlController.dispose();
    myFocusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    //should this be a map too? to match CheckboxDecorated
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
                  "Welcome to your pantry!",
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
                            child: TileDecorated(
                                Colors.white,
                                Icon(Icons.fastfood),
                                Text(_data[index].title! +
                                    "" +
                                    (index + 1).toString()),
                                Text(_data[index].ingredients! +
                                    " " +
                                    (index + 1).toString() +
                                    "\nTotal time: " +
                                    _data[index].totalTime.toString() +
                                    " minutes"),
                                true),
                          ),
                        ));
                  }),
            ),
            SizedBox(width: 10, height: 10),
            inputTextButton(recipeByUrlController, "Input your recipe URL",
                "Add recipe!", myFocusNode),
            SizedBox(width: 10, height: 10),
            SizedBox(
                width: 360,
                height: 60,
                child: ElevatedButton(
                  onPressed: () {
                    alertDialog(
                        context,
                        buttonCallback,
                        parseCallback,
                        textCallback,
                        recipeByTextController,
                        "Add Recipe By Text",
                        "Enter your recipe text");
                  },
                  child: Text('Add recipe by text!'),
                ))
          ],
        ),
      ),
    );
  }
}