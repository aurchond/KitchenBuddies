import 'package:first_app/backend_processing/data_class.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:provider/provider.dart';

class InstructionsScreen extends StatefulWidget {
  const InstructionsScreen({Key? key}) : super(key: key);

  @override
  _InstructionsScreenState createState() => _InstructionsScreenState();
}

class _InstructionsScreenState extends State<InstructionsScreen> {
  @override
  void initState() {
    super.initState();
    final postModel = Provider.of<DataClass>(context, listen: false);
    postModel.getPostData(0); //call with index
  }

  @override
  Widget build(BuildContext context) {
    final postModel = Provider.of<DataClass>(context);

    //stitch ingredient quantity and list together
    List<List<String>> ingredients = new List.generate(postModel.post?.recipeStep?.length ?? 0, (i) => []);
    if (postModel.post?.recipeStep?.length != null && postModel.post?.recipeStep?[0].ingredientQuantity?.length != null) {
      for (int i = 0; i < (postModel.post?.recipeStep?.length ?? 0); i++) { //should include error case for missing length
        for (int j = 0; j < (postModel.post?.recipeStep?[i].ingredientQuantity?.length ?? 0); j++) {
          ingredients[i].add((postModel.post?.recipeStep?[i].ingredientQuantity?[j].toString() ?? "") + " " + (postModel.post?.recipeStep?[i].ingredientList?[j] ?? ""));
        }
      }
    }

    print(ingredients);

    return Scaffold(
      appBar: AppBar(
        title: Text("Instructions"),
      ),
      body: Container(
        padding: EdgeInsets.all(20),
        child: postModel.loading
            ? Center(
          child: Container(
            child: SpinKitThreeBounce(
              itemBuilder: (BuildContext context, int index) {
                return DecoratedBox(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    color: index.isEven ? Colors.red : Colors.deepOrange.shade300,
                  ),
                );
              },
            ),
          ),
        )
            : Center(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                margin: EdgeInsets.only(top: 40, bottom: 20),
                child: Text(
                  postModel.post?.userEmail ?? "",
                  style: TextStyle(
                      fontWeight: FontWeight.bold, fontSize: 18),
                ),
              ),
              new Expanded(
                  child: new ListView.builder
                    (
                      itemCount: postModel.post?.recipeStep?.length,
                      itemBuilder: (BuildContext context, int index) {
                        return new Text(
                            (index+1).toString() + ". " + (postModel.post?.recipeStep?[index].instructions ?? "")
                                + " (" + ingredients[index].join(', ') + ")",
                            style: TextStyle(
                                fontSize: 14,
                                color: Colors.deepOrange.shade300
                            ));
                      }
                  )
              )
              // Container(
              //   child: ListView(
              //     shrinkWrap: true,
              //     padding: const EdgeInsets.all(8),
              //     children: <Widget>[
              //       Container(
              //           height: 50,
              //           color: Colors.green,
              //           child: Text((postModel.post?.recipeStep)?[0]
              //                   .instructions ??
              //               "")),
              //       Container(
              //           height: 50,
              //           color: Colors.green,
              //           child: Text((postModel.post?.recipeStep)?[1]
              //               .instructions ??
              //               "")),
              //     ],
              //   ),
              // ),
            ],
          ),
        ),
      ),
    );
  }
}