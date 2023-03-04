import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

//TODO: optional fields for subtitle and body
Widget TileDecorated(
    Color fill, Icon icon, int index, Text title, Text subtitle, Text body, bool hasSub) {
  return hasSub
      ? Container(
          //this is for recipes
          decoration: BoxDecoration(
              color: fill,
              border: Border.all(width: 3, color: Colors.deepOrange.shade300),
              borderRadius: BorderRadius.circular(20)),
    child: ExpansionTile(
      leading: Text("  "+ (index+1).toString(), style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),),
      title: title,
      subtitle: subtitle,
      children: <Widget>[
        ListTile(
                title: body),
          ]))
      : Container(
          //this is for friends
          alignment: Alignment.center,
          decoration: BoxDecoration(
              color: fill,
              border: Border.all(width: 3, color: Colors.deepOrange.shade300),
              borderRadius: BorderRadius.circular(20)),
          child: ListTile(leading: icon, title: title));
}
