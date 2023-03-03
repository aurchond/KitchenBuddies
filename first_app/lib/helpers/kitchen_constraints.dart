import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

Widget KitchenConstraintsContainer(
    List<FocusNode> focusNodes, List<TextEditingController> controllers) {
  return Column(children: [
    Container(
        margin: EdgeInsets.all(10),
        child: Text(
          "Customize your kitchen:",
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
        )),
    Container(
        height: 170,
        child: GridView.count(
            padding: const EdgeInsets.all(10),
            crossAxisSpacing: 10,
            mainAxisSpacing: 10,
            crossAxisCount: 5,
            children: <Widget>[
              //text descriptions of constraints
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Ovens', textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                color: Colors.deepOrange[300],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Pots', textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                color: Colors.deepOrange[300],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Pans', textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                color: Colors.deepOrange[300],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Bowls', textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),),
                color: Colors.deepOrange[300],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: const Text('Cutting Boards', textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                color: Colors.deepOrange[300],
              ),
              //number input of constraints
              Container(
                padding: const EdgeInsets.all(8),
                child: TextField(
                  focusNode: focusNodes[0],
                  controller: controllers[0],
                  keyboardType: TextInputType.number,
                  inputFormatters: <TextInputFormatter>[
                    FilteringTextInputFormatter.digitsOnly
                  ],
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                  ),
                ),
                color: Colors.deepOrange[200],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: TextField(
                  focusNode: focusNodes[1],
                  controller: controllers[1],
                  keyboardType: TextInputType.number,
                  inputFormatters: <TextInputFormatter>[
                    FilteringTextInputFormatter.digitsOnly
                  ],
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                  ),
                ),
                color: Colors.deepOrange[200],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: TextField(
                  focusNode: focusNodes[2],
                  controller: controllers[2],
                  keyboardType: TextInputType.number,
                  inputFormatters: <TextInputFormatter>[
                    FilteringTextInputFormatter.digitsOnly
                  ],
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                  ),
                ),
                color: Colors.deepOrange[200],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: TextField(
                  focusNode: focusNodes[3],
                  controller: controllers[3],
                  keyboardType: TextInputType.number,
                  inputFormatters: <TextInputFormatter>[
                    FilteringTextInputFormatter.digitsOnly
                  ],
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                  ),
                ),
                color: Colors.deepOrange[200],
              ),
              Container(
                padding: const EdgeInsets.all(8),
                child: TextField(
                  focusNode: focusNodes[4],
                  controller: controllers[4],
                  keyboardType: TextInputType.number,
                  inputFormatters: <TextInputFormatter>[
                    FilteringTextInputFormatter.digitsOnly
                  ],
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                  ),
                ),
                color: Colors.deepOrange[200],
              ),
            ])),
    SizedBox(
        width: 360,
        height: 60,
        child: ElevatedButton(
            onPressed: () {
              print(controllers[0].text);
            },
            child: Text("Save your kitchen constraints!")))
  ]);
}
