import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:first_app/helpers/globals.dart';
import 'package:first_app/provider/notification_provider.dart';
import 'package:first_app/widgets/checkbox_decorated.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../backend_processing/data_class.dart';
import '../../data_models/friends_list_model.dart';
import '../../data_models/recipe_info_model.dart';
import '../../provider/auth_provider.dart';
import 'instructions_screen.dart';

class NewMealSession extends StatefulWidget {
  const NewMealSession({Key? key}) : super(key: key);

  @override
  State<NewMealSession> createState() => _NewMealSessionState();
}

class _NewMealSessionState extends State<NewMealSession> {
  dynamic tokens;

  checkboxCallback(bool? val, List<Map> _data, int index,
      StateSetter setState) {
    setState(() {
      _data[index]["isSelected"] = val!;
    });
  }

  List<int> getSelectedRecipes(List<Map>? myRecipes) {
    List<int>? selectedRecipes = <int>[];

    for (int i = 0; i < (myRecipes?.length ?? 0); i++) {
      if (myRecipes?[i]["isSelected"] == true) {
        selectedRecipes.add(myRecipes?[i]["recipeID"]);
      }
    }
    return selectedRecipes;
  }

  // todo: make getSelectedFriends function and pass return value to getTokens()
  List<String> getSelectedFriends(List<Map>? myFriends) {
    List<String>? selectedFriends = <String>[];

    for (int i = 0; i < (myFriends?.length ?? 0); i++) {
      if (myFriends?[i]["isSelected"] == true) {
        selectedFriends.add(myFriends?[i]["name"]);
      }
    }

    return selectedFriends;
  }


  Future<Map<String, String>> getTokenMap(List<String> selectedFriends) async {
    Map<String, String> tokens = new Map();

    var collection = FirebaseFirestore.instance.collection('users');
    for (int i = 0; i < selectedFriends.length; i++) {
      var querySnapshot =
      await collection.where('email', isEqualTo: selectedFriends[i]).get();
      if (!querySnapshot.docs.isEmpty) {
        for (QueryDocumentSnapshot ds in querySnapshot.docs) {
          tokens[selectedFriends[i]] = ds.get("token");
        } // <-- The value you want to retrieve.
        // Call setState if needed.
      }
    }

    // add my token to the tokens map
    String myDeviceToken = await getDeviceToken();
    tokens[myEmail] = myDeviceToken;

    return (tokens);
  }

  // get this device's token to save in the Firebase Auth later
  Future<String> getDeviceToken() async {
    final FirebaseMessaging firebaseMessaging = FirebaseMessaging.instance;

    final String? token =
    await firebaseMessaging.getToken(); // get the device's token
    return token!;
  }

  @override
  Widget build(BuildContext context) {
    //TODO: read recipes and create list below
    final dataModel = Provider.of<DataClass>(context);

    final List<Map>? myFriends = List.generate(
        dataModel.friendsList?.friends?.length ?? 0,
            (index) =>
        {
          "id": index,
          "name": dataModel.friendsList?.friends?[index],
          "isSelected": false
        }).toList();

    final List<Map>? myRecipes = List.generate(
        dataModel.pastRecipes?.length ?? 0,
            (index) =>
        {
          "recipeID": dataModel.pastRecipes?[index]?.recipeID,
          "id": index,
          "title": dataModel.pastRecipes?[index]?.recipeName,
          "totalTime": dataModel.pastRecipes?[index]?.totalTime,
          "isSelected": false
        }).toList();

    /// send meal session steps request
    //set the included friends for the meal session
    // List<String>? selectedFriends = <String>[];
    //
    // for (int i = 0; i< (myFriends?.length ?? 0); i++) {
    //   if(myFriends?[i]["isSelected"] == true) {
    //     selectedFriends.add(myFriends?[i]["name"]);
    //   }
    // }
    // dataModel.mealSessionStepsRequest?.includedFriends = selectedFriends;
    // dataModel.mealSessionStepsRequest?.kitchenConstraints = dataModel.kitchenConstraints;
    //
    // //set the included recipes for the meal session
    // List<RecipeInfo>? selectedRecipes = <RecipeInfo>[];
    //
    // for (int i = 0; i< (myRecipes?.length ?? 0); i++) {
    //   if(myRecipes?[i]["isSelected"] == true) {
    //     selectedRecipes.add(myRecipes?[i]["name"]);
    //   }
    // }

    return Consumer<AuthProvider>(builder: (context, model, _) {
      return Scaffold(
        appBar: AppBar(
          title: const Text("New Meal Session"),
          actions: [
            IconButton(
              onPressed: () {
                model.logOut();
              },
              icon: const Icon(Icons.logout),
            )
          ],
        ),
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
                      onPressed: () async {
                        List<int> _selectedRecipes = getSelectedRecipes(
                            myRecipes);
                        List<String> _selectedFriends = getSelectedFriends(
                            myFriends);
                        Map<String, String> _tokenMap = await getTokenMap(
                            _selectedFriends);
                        print("token map:" + _tokenMap.toString());


                        Navigator.of(context).push(MaterialPageRoute(
                            builder: (context) =>
                                InstructionsScreen(tokenMap: _tokenMap,
                                    selectedRecipes: _selectedRecipes,
                                    selectedFriends: _selectedFriends)));
                      },
                      child: Text("Start new session!"))),
            ])),
      );
    });
  }
}
