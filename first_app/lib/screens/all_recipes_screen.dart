import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../backend_processing/data_class.dart';

class AllRecipes extends StatefulWidget {
  const AllRecipes({Key? key}) : super(key: key);

  @override
  State<AllRecipes> createState() => _AllRecipesState();
}

class Data {
  final String? title, subTitle;
  bool? isSelected;

  Data({this.isSelected, this.title, this.subTitle});
}

class _AllRecipesState extends State<AllRecipes> {
  @override
  Widget build(BuildContext context) {
    //final postModel = Provider.of<DataClass>(context); //TODO: data and service classs for past recipes
    List<Data> _data = [
      Data(title: "Option ", subTitle: "Description", isSelected: false),
      Data(title: "Option ", subTitle: "Description", isSelected: false),
      Data(title: "Option ", subTitle: "Description", isSelected: false),
      Data(title: "Option ", subTitle: "Description", isSelected: false),
      Data(title: "Option ", subTitle: "Description", isSelected: false),
    ];

    return Scaffold(
      appBar: AppBar(title: const Text("All Recipes")),
      // body: CheckboxListTile(
      //   title: const Text('GeeksforGeeks'),
      //   subtitle: const Text('A computer science portal for geeks.'),
      //   secondary: const Icon(Icons.code),
      //   autofocus: false,
      //   activeColor: Colors.green,
      //   checkColor: Colors.white,
      //   value: isChecked,
      //   onChanged: (bool? value) {
      //     setState(() {
      //       isChecked = value!;
      //     });
      //   },
      // ),
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
            StatefulBuilder(
              builder: (BuildContext context, StateSetter setState) {
                return Expanded(
                  child: new ListView.builder(
                    itemCount: _data.length,
                    itemBuilder: (context, index) {
                      return CheckboxListTile(
                        secondary: Icon(Icons.person),
                        title: Text(
                          _data[index].title! + " " + (index + 1).toString(),
                        ),
                        subtitle: Text(
                          _data[index].subTitle! + " " + (index + 1).toString(),
                        ),
                        value: _data[index].isSelected,
                        onChanged: (val) {
                          setState(
                            () {
                              _data[index].isSelected = val!;
                            },
                          );
                        },
                      );
                    },
                  ),
                );
              },
            )
            // child: new ListView.builder(
            //     itemCount:
            //         10, //will be something like postModel.post?.recipes?.length
            //     itemBuilder: (BuildContext context, int index) {
            //       return SizedBox(
            //         width: 400,
            //         height: 400,
            //         child: Center(
            //           child: Padding(
            //             padding: const EdgeInsets.all(8.0),
            //             child: Container(
            //               decoration: BoxDecoration(
            //                 border: Border.all(
            //                     width: 3,
            //                     color: Colors.deepOrange.shade300),
            //                 borderRadius: BorderRadius.circular(20),
            //               ), //BoxDecoration

            //               /** CheckboxListTile Widget **/
            //               child: CheckboxListTile(
            //                 title: const Text('GeeksforGeeks'),
            //                 subtitle: const Text(
            //                     'A computer science portal for geeks.'),
            //                 secondary: const Icon(Icons.code),
            //                 autofocus: false,
            //                 activeColor: Colors.green,
            //                 checkColor: Colors.white,
            //                 value: isChecked,
            //                 onChanged: (bool? value) {
            //                   setState(() {
            //                     isChecked = value!;
            //                   });
            //                 },
            //               ),
            //             ),
            //           ),
            //         ),
            //       );
            //     })),
          ],
        ),
      ),
    );
  }
}
