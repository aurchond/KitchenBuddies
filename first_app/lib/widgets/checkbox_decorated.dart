import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget CheckboxDecorated(
    List<Map> _data,
    Color fill,
    Icon icon,
    int index,
    Text title,
    Text subtitle,
    bool hasSub,
    Function(bool? value, List<Map> _data, int index, StateSetter setState)
        callback,
    StateSetter setState) {
  return Container(
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: fill,
        border: Border.all(width: 3, color: Colors.deepOrange.shade300),
        borderRadius: BorderRadius.circular(20),
      ),
      child: hasSub
          ? CheckboxListTile(
              secondary: Text("  "+ (index+1).toString(), style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),),
              title: title,
              subtitle: subtitle,
              value: _data[index]["isSelected"],
              onChanged: (value) {
                callback(value, _data, index, setState);
              },
              //isThreeLine: true,
            )
          : CheckboxListTile(
              secondary: icon,
              title: title,
              contentPadding: EdgeInsets.only(left: 10, right: 10),
              visualDensity: VisualDensity.compact,
              value: _data[index]["isSelected"],
              onChanged: (value) {
                callback(value, _data, index, setState);
              },
            ));
}
