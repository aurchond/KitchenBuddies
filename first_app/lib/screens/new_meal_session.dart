import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'instructions_screen.dart';

class NewMealSession extends StatefulWidget {
  const NewMealSession({Key? key}) : super(key: key);

  @override
  State<NewMealSession> createState() => _NewMealSessionState();
}

class recipeTile {
  final String? title;
  final String? ingredients; //we will join list of ingredients with .join(", ")
  final int? totalTime;
  final DateTime? lastDateMade;
  bool? isSelected;

  recipeTile(
      {this.isSelected,
        this.title,
        this.ingredients,
        this.totalTime,
        this.lastDateMade});
}

class _NewMealSessionState extends State<NewMealSession> {

  @override
  Widget build(BuildContext context) {
    //final postModel = Provider.of<DataClass>(context);
    //TODO: data and service classs for past recipes
    //TODO: read recipes and create list below
    List<recipeTile> _data = [
      recipeTile(
          title: "Option ",
          ingredients: "Ingredients",
          totalTime: 60,
          isSelected: false),
      recipeTile(
          title: "Option ",
          ingredients: "Ingredients",
          totalTime: 60,
          isSelected: false),
      recipeTile(
          title: "Option ",
          ingredients: "Ingredients",
          totalTime: 60,
          isSelected: false),
      recipeTile(
          title: "Option ",
          ingredients: "Ingredients",
          totalTime: 60,
          isSelected: false),
      recipeTile(
          title: "Option ",
          ingredients: "Ingredients",
          totalTime: 60,
          isSelected: false),
    ];

    return Scaffold(
      appBar: AppBar(title: const Text("New Meal Session")),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
            children: [
                Container(
                    margin: EdgeInsets.only(top: 20, bottom: 20),
                    child: Text(
                      "Add your recipes below:",
                      style:
                          TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                    )),
              StatefulBuilder(
                builder: (BuildContext context, StateSetter setState) {
                  return Expanded(
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
                                  child: CheckboxListTile(
                                    secondary: Icon(Icons.fastfood),
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
                                          _data[index].totalTime.toString() + " minutes",
                                    ),
                                    value: _data[index].isSelected,
                                    onChanged: (val) {
                                      setState(
                                            () {
                                          _data[index].isSelected = val!;
                                        },
                                      //TODO: if isSelected, add to json request
                                      );
                                    },
                                    isThreeLine: true,
                                  )),
                            ),
                          ),
                        );
                      },
                    ),
                  );
                },
              ),
                ElevatedButton(
                    onPressed: () {
                      Navigator.of(context).push(MaterialPageRoute(
                          builder: (context) => InstructionsScreen()));
                    },
                    child: Text("Start new session!")),
              ])),
        );
  }
}
