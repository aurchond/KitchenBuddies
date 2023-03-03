import 'package:first_app/helpers/checkbox_decorated.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'instructions_screen.dart';

class NewMealSession extends StatefulWidget {
  const NewMealSession({Key? key}) : super(key: key);

  @override
  State<NewMealSession> createState() => _NewMealSessionState();
}

class _NewMealSessionState extends State<NewMealSession> {

  checkboxCallback(
      bool? val, List<Map> _data, int index, StateSetter setState) {
    setState(() {
      _data[index]["isSelected"] = val!;
    });
  }

  @override
  Widget build(BuildContext context) {
    //final postModel = Provider.of<DataClass>(context);
    //TODO: data and service classs for past recipes
    //TODO: read recipes and create list below

    final List<Map> myFriends = List.generate(
        6,
        (index) => {
              "id": index,
              "name": "Friend $index",
              "isSelected": false
            }).toList();

    final List<Map> myRecipes = List.generate(
        6,
            (index) => {
          "id": index,
          "title": "Recipe $index","ingredients": "Ingredients $index",
              "totalTime": "Total time: $index minutes",
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
                    itemCount: myRecipes.length,
                    itemBuilder: (context, index) {
                      return SizedBox(
                        width: 100,
                        height: 100,
                        child: Center(
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: CheckboxDecorated(myRecipes, Colors.white,  Icon(Icons.fastfood), index,
                      Text(
                             myRecipes[index]["title"],
                          ), Text(
                          myRecipes[index]["ingredients"] + "\n" +
                               myRecipes[index]["totalTime"]
                          ), true, checkboxCallback, setState)
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
                        return CheckboxDecorated(
                            myFriends,
                            Colors.deepOrange.shade200,
                            Icon(
                              Icons.person,
                              color: Colors.deepOrange.shade700,
                            ),
                            index,
                            Text(myFriends[index]["name"],
                                style: TextStyle(fontSize: 12)),
                            Text(""), false,
                            checkboxCallback,
                            setState);
                      }),
                ));
              },
            ),
        SizedBox(
            width: 360,
            height: 60, child: ElevatedButton(
                onPressed: () {
                  Navigator.of(context).push(MaterialPageRoute(
                      builder: (context) => InstructionsScreen()));
                },
                child: Text("Start new session!"))),
          ])),
    );
  }
}
