import 'package:first_app/screens/all_recipes_screen.dart';
import 'package:first_app/screens/home_screen.dart';
import 'package:first_app/screens/instructions_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class BottomNavigation extends StatefulWidget {
  const BottomNavigation({Key? key}) : super(key: key);

  @override
  State<BottomNavigation> createState() => _BottomNavigationState();
}

class _BottomNavigationState extends State<BottomNavigation> {
  int currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack (
          children: [
        /// List of tab page widgets
        Offstage(
          offstage: currentIndex != 0,
          child: const HomeScreen(),
        ), //these may not be final pages in bottom nav bar
        Offstage(
          offstage: currentIndex != 1,
          child: const AllRecipes(),
        ),
        Offstage(
          offstage: currentIndex != 2,
          child: const InstructionsScreen(),
        ),
      ]),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: currentIndex,
        onTap: (index) {
          setState(() {
            currentIndex = index;

            /// Switching tabs
          });
        },
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: "Home"),
          BottomNavigationBarItem(
              icon: Icon(Icons.fastfood), label: "All Recipes"),
          BottomNavigationBarItem(
              icon: Icon(Icons.fork_left), label: "Instructions"),
        ],
      ),
    );
  }
}
