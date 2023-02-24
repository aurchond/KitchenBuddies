import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

Widget TileDecorated(
    Color fill,
    Icon icon,
    Text title,
    Text subtitle,
    bool hasSub) {
  return hasSub ? Container(
      decoration: BoxDecoration(
          color: fill,
          border: Border.all(
              width: 3, color: Colors.deepOrange.shade300),
          borderRadius: BorderRadius.circular(20)),
      child: ListTile(
          leading: icon,
          title: title, subtitle: subtitle, isThreeLine: true)
      ) :
  Container (
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