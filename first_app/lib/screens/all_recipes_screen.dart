import 'dart:convert';

import 'package:first_app/data_models/recipe_info.dart';
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
  final double? totalTime;
  final DateTime? lastDateMade;

  recipeTile({this.title, this.ingredients, this.totalTime, this.lastDateMade});
}

class _AllRecipesState extends State<AllRecipes> {
  final recipeByUrlController = TextEditingController();
  late FocusNode myFocusNode = FocusNode();

  // code for inputting recipes as text
  late String valueText;
  final recipeByTextController = TextEditingController();

  @override
  void initState() {
    final dataModel = Provider.of<DataClass>(context, listen: false);
    dataModel.loadAllRecipesPage(); //todo: save string value

    super.initState();
  }

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

    String recipeName = valueText.substring(0, timeIndex-1);
    print(recipeName);

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

    RecipeInfo recipeByText = RecipeInfo(recipeName: recipeName, ingredientList: ingredientList, totalTime: double.parse(totalTime), instructionList: instructionList);
    String data = jsonEncode(recipeByText);
    //TODO: send json to backend
    //print(data);
  }

  textCallback(String value) {
    setState(() {
      valueText = value;
    });
  }

  //input text button callback
  onPressedCallback(FocusNode _focusNode, TextEditingController _controller) {
    print(_controller.text);
    _focusNode.unfocus();
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

    final dataModel = Provider.of<DataClass>(context);

    List<recipeTile> _pastRecipes = <recipeTile>[];

    int? pastRecipesLength = dataModel.pastRecipes?.length;
    if (pastRecipesLength != null) {
      for (int i=0; i<pastRecipesLength; i++) {
        _pastRecipes.add(recipeTile(
            title: dataModel.pastRecipes?[i]?.recipeName,
            ingredients: "oops baby sipping on the goose like whoops baby",
            totalTime: dataModel.pastRecipes?[i]?.completionTime
        ));
      }
    }

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
                  itemCount: _pastRecipes.length,
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
                                Text(_pastRecipes[index].title! +
                                    ""),
                                Text(_pastRecipes[index].ingredients! +
                                    " " +
                                    "\nTotal time: " +
                                    _pastRecipes[index].totalTime.toString() +
                                    " minutes"),
                                true),
                          ),
                        ));
                  }),
            ),
            SizedBox(width: 10, height: 10),
            inputTextButton(recipeByUrlController, "Input your recipe URL",
                "Add recipe!", myFocusNode, onPressedCallback),
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
