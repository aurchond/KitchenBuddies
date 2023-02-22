import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import '../recipe_tile.dart';

Widget PastRecipeBox(bool isMealSession, List<recipeTile> _data, final void Function(List<recipeTile> _data, bool? val, int index) _onChanged) {
  if (isMealSession) {
    return StatefulBuilder(
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
                        _onChanged(_data, val, index);
                      },
                      isThreeLine: true,
                    )),
              ),
            ),
          );
        },
      ),
    );});
  } else {
    return ListTile(
      title: const Text('GeeksforGeeks'),
      subtitle: const Text('A computer science portal for geeks.'),
      trailing: const Icon(Icons.code),
      autofocus: false,
      iconColor: Colors.deepOrange.shade400,
    );
  }
}
