import 'package:first_app/backend_processing/post_requests.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../data_models/kitchen_constraints_model.dart';
import '../helpers/globals.dart';

Widget KitchenConstraintsContainer(
    List<FocusNode> focusNodes,
    List<TextEditingController> controllers,
    List<bool> isTextFieldShown,
    StateSetter setState) {
  return Column(children: [
    Container(
        margin: EdgeInsets.all(10),
        child: Text(
          "Customize your kitchen:",
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
        )),
    SizedBox(
        height: 160,
        child: GridView.count(
            padding: const EdgeInsets.all(10),
            crossAxisSpacing: 10,
            mainAxisSpacing: 10,
            crossAxisCount: 5,
            children: <Widget>[
              //text descriptions of constraints
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Ovens',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: Colors.white, fontWeight: FontWeight.bold)),
                color: Colors.deepOrange[300],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Pots',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: Colors.white, fontWeight: FontWeight.bold)),
                color: Colors.deepOrange[300],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Pans',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: Colors.white, fontWeight: FontWeight.bold)),
                color: Colors.deepOrange[300],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text(
                  'Bowls',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                      color: Colors.white, fontWeight: FontWeight.bold),
                ),
                color: Colors.deepOrange[300],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Cutting Boards',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: Colors.white, fontWeight: FontWeight.bold)),
                color: Colors.deepOrange[300],
              ),
              //number input of constraints
              Container(
                  padding: const EdgeInsets.all(8),
                  color: Colors.deepOrange[200],
                  child: AnimatedSwitcher(
                    duration: Duration(
                      milliseconds: 300,
                    ),
                    child: isTextFieldShown[0]
                        ? TextField(
                            focusNode: focusNodes[0],
                            controller: controllers[0],
                            keyboardType: TextInputType.number,
                            decoration: InputDecoration(
                              border: OutlineInputBorder(),
                            ),
                          )
                        : SizedBox(
                            height: 50,
                            child: ElevatedButton(
                                onPressed: () {
                                  setState(() {
                                    isTextFieldShown[0] = true;
                                  });
                                },
                                child: Text("1"))),
                  )),
              Container(
                padding: const EdgeInsets.all(8),
                child: isTextFieldShown[1]
                    ? TextField(
                        focusNode: focusNodes[1],
                        controller: controllers[1],
                        keyboardType: TextInputType.number,
                        decoration: InputDecoration(
                          border: OutlineInputBorder(),
                        ),
                      )
                    : SizedBox(
                        height: 50,
                        child: ElevatedButton(
                            onPressed: () {
                              setState(() {
                                isTextFieldShown[1] = true;
                              });
                            },
                            child: Text("2"))),
                color: Colors.deepOrange[200],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: isTextFieldShown[2]
                    ? TextField(
                  focusNode: focusNodes[2],
                  controller: controllers[2],
                  keyboardType: TextInputType.number,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                  ),
                )
                    : SizedBox(height: 50, child: ElevatedButton(
                    onPressed: () {
                      setState(() {
                        isTextFieldShown[2] = true;
                      });
                    },
                    child: Text("3"))),
                color: Colors.deepOrange[200],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: isTextFieldShown[3]
                    ? TextField(
                  focusNode: focusNodes[3],
                  controller: controllers[3],
                  keyboardType: TextInputType.number,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                  ),
                )
                    : SizedBox(height: 50, child: ElevatedButton(
                    onPressed: () {
                      setState(() {
                        isTextFieldShown[3] = true;
                      });
                    },
                    child: Text("4"))),
                color: Colors.deepOrange[200],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: isTextFieldShown[4]
                    ? TextField(
                  focusNode: focusNodes[4],
                  controller: controllers[4],
                  keyboardType: TextInputType.number,
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                  ),
                )
                    : SizedBox(height: 50, child: ElevatedButton(
                    onPressed: () {
                      setState(() {
                        isTextFieldShown[4] = true;
                      });
                    },
                    child: Text("5"))),
                color: Colors.deepOrange[200],
              ),
            ])),
    SizedBox(
        width: 360,
        height: 60,
        child: ElevatedButton(
            onPressed: () {
              KitchenConstraints kitchenConstraints = KitchenConstraints(
                  userEmail: myEmail,
                  oven: int.parse(controllers[0].text),
                  pot: int.parse(controllers[1].text),
                  pan: int.parse(controllers[2].text),
                  bowl: int.parse(controllers[3].text),
                  cuttingBoard: int.parse(controllers[4].text));

              addKitchenConstriants(kitchenConstraints);
              setState(() {
                for (int i = 0; i < isTextFieldShown.length; i++) {
                  isTextFieldShown[i] = false;
                }
              });
            },
            child: Text("Save your kitchen constraints!")))
  ]);
}
