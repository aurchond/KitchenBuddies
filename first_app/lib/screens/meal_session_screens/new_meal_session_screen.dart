import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:first_app/widgets/checkbox_decorated.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../backend_processing/data_class.dart';
import 'instructions_screen.dart';

class NewMealSession extends StatefulWidget {
  const NewMealSession({Key? key}) : super(key: key);

  @override
  State<NewMealSession> createState() => _NewMealSessionState();
}

class _NewMealSessionState extends State<NewMealSession> {
  dynamic tokens;

  checkboxCallback(
      bool? val, List<Map> _data, int index, StateSetter setState) {
    setState(() {
      _data[index]["isSelected"] = val!;
    });
  }

  Future<dynamic> getTokens(List<Map>? myFriends) async {
    List<String> selectedFriends = <String>[];
    List<String> tokens = <String>[];

    for (int i = 0; i < (myFriends?.length ?? 0); i++) {
      if (myFriends?[i]["isSelected"] == true) {
        selectedFriends.add(myFriends?[i]["name"]);
      }
    }

    print(selectedFriends);

    var collection = FirebaseFirestore.instance.collection('users');
    for (int i = 0; i < selectedFriends.length; i++) {
      var querySnapshot = await collection.where('email', isEqualTo: selectedFriends[i]).get();
      if (!querySnapshot.docs.isEmpty) {
        for (QueryDocumentSnapshot ds in querySnapshot.docs) {
          tokens.add(ds.get("token"));
        } // <-- The value you want to retrieve.
        // Call setState if needed.
      }
    }

    print(tokens);
  }

  @override
  Widget build(BuildContext context) {
    //TODO: read recipes and create list below
    final dataModel = Provider.of<DataClass>(context);

    final List<Map>? myFriends = List.generate(
        dataModel.friendsList?.friends?.length ?? 0,
        (index) => {
              "id": index,
              "name": dataModel.friendsList?.friends?[index],
              "isSelected": false
            }).toList();

    final List<Map>? myRecipes = List.generate(
        dataModel.pastRecipes?.length ?? 0,
        (index) => {
              "id": index,
              "title": dataModel.pastRecipes?[index]?.recipeName,
              "totalTime": dataModel.pastRecipes?[index]?.completionTime,
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
                    itemCount: myRecipes?.length,
                    itemBuilder: (context, index) {
                      return SizedBox(
                        width: 100,
                        height: 100,
                        child: Center(
                          child: Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: CheckboxDecorated(
                                  myRecipes!,
                                  Colors.white,
                                  Icon(Icons.fastfood),
                                  index,
                                  Text(
                                    myRecipes[index]["title"],
                                  ),
                                  Text("Total time: " +
                                      myRecipes[index]["totalTime"].toString() +
                                      " minutes"),
                                  true,
                                  checkboxCallback,
                                  setState)),
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
                return SizedBox(
                    height: 205,
                    child: Container(
                      padding: const EdgeInsets.all(10),
                      child: GridView.builder(
                          gridDelegate:
                              const SliverGridDelegateWithFixedCrossAxisCount(
                                  crossAxisCount: 2,
                                  childAspectRatio: 4 / 2,
                                  crossAxisSpacing: 10,
                                  mainAxisSpacing: 10),
                          itemCount: myFriends?.length,
                          itemBuilder: (BuildContext context, index) {
                            return CheckboxDecorated(
                                myFriends!,
                                Colors.deepOrange.shade200,
                                Icon(
                                  Icons.person,
                                  color: Colors.deepOrange.shade700,
                                ),
                                index,
                                Text(myFriends[index]["name"],
                                    style: TextStyle(fontSize: 12)),
                                Text(""),
                                false,
                                checkboxCallback,
                                setState);
                          }),
                    ));
              },
            ),
            SizedBox(
                width: 360,
                height: 60,
                child: ElevatedButton(
                    onPressed: () {
                      getTokens(myFriends);
                      Navigator.of(context).push(MaterialPageRoute(
                          builder: (context) => InstructionsScreen()));

                      //send notification to other friends in session
                      //call firebase to get device tokens
                    },
                    child: Text("Start new session!"))),
          ])),
    );
  }
}
