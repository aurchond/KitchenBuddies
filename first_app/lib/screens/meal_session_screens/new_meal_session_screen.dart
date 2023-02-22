import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../../helpers/recipe_tile.dart';
import '../../helpers/checkboxListTIle.dart';
import 'instructions_screen.dart';

class NewMealSession extends StatefulWidget {
  const NewMealSession({Key? key}) : super(key: key);

  @override
  State<NewMealSession> createState() => _NewMealSessionState();
}

class _NewMealSessionState extends State<NewMealSession> {
  // didn't work :(
  // void onCheckedBox(List<recipeTile> _data, bool? val, int index) {
  //   setState(
  //     () {
  //       _data[index].isSelected = val!;
  //     },
  //     //TODO: if isSelected, add to json request
  //   );
  // }

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

    final List<Map> myFriends = List.generate(
        6,
        (index) => {
              "id": index,
              "name": "Friend $index",
              "isSelected": false
            }).toList();

    return Scaffold(
      appBar: AppBar(title: const Text("New Meal Session")),
      body: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(children: [
            Container(
                margin: EdgeInsets.only(top: 20, bottom: 20),
                child: Text(
                  "Add your recipes below:",
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
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
                                        _data[index].totalTime.toString() +
                                        " minutes",
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
            Container(
                margin: EdgeInsets.only(top: 20, bottom: 20),
                child: Text(
                  "Add your friends below:",
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                )),
            StatefulBuilder(
              builder: (BuildContext context, StateSetter setState) {
                return Expanded(
                    child: Container(
                  padding: const EdgeInsets.all(10),
                  child: GridView.builder(
                      gridDelegate:
                          const SliverGridDelegateWithMaxCrossAxisExtent(
                              maxCrossAxisExtent: 300,
                              childAspectRatio: 5 / 2,
                              crossAxisSpacing: 10,
                              mainAxisSpacing: 10),
                      itemCount: myFriends.length,
                      itemBuilder: (BuildContext context, index) {
                        return Container(
                            alignment: Alignment.center,
                            decoration: BoxDecoration(
                                color: Colors.deepOrange.shade200,
                                border: Border.all(
                                    width: 3,
                                    color: Colors.deepOrange.shade300),
                                borderRadius: BorderRadius.circular(20)),
                            child: CheckboxListTile(
                              secondary: Icon(
                                Icons.person,
                                color: Colors.deepOrange.shade700,
                              ),
                              title: Text(
                                myFriends[index]["name"],
                                style: TextStyle(
                                    fontSize: 12),
                              ),
                              contentPadding: EdgeInsets.only(left:10, right:10),
                              visualDensity: VisualDensity.compact,
                              value: myFriends[index]["isSelected"],
                              onChanged: (val) {
                                setState(
                                  () {
                                    myFriends[index]["isSelected"] = val!;
                                  },
                                  //TODO: if isSelected, add to json request
                                );
                              },
                            ));
                      }),
                ));
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
