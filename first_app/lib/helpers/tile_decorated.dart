import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget TileDecorated(
    Color fill,
    Icon icon,
    Text title,
    Text subtitle,
    bool hasSub) {
  return hasSub ? Container( //this is for recipes
      decoration: BoxDecoration(
          color: fill,
          border: Border.all(
              width: 3, color: Colors.deepOrange.shade300),
          borderRadius: BorderRadius.circular(20)),
      // child: ExpansionTile(
      //   title: Text('ExpansionTile 1'),
      //   subtitle: Text('Trailing expansion arrow icon'),
      //   children: <Widget>[
       child: ListTile(
            leading: icon,
            title: title, subtitle: subtitle, isThreeLine: true),
      //   ],
      // ),
      ) :
  Container ( //this is for friends
      alignment: Alignment.center,
      decoration: BoxDecoration(
          color: fill,
          border: Border.all(
              width: 3, color: Colors.deepOrange.shade300),
          borderRadius: BorderRadius.circular(20)),
      child: ListTile(
          leading: icon,
          title: title)
  );
}