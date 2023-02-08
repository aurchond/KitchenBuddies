import 'package:auto_route/auto_route.dart';
import 'package:first_app/screens/home_page_screen.dart';
import 'package:first_app/widgets/material_auto_router.gr.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Dashboard extends StatelessWidget {
  @override
  Widget build(context) {
    return AutoTabsScaffold(
      routes: const [
        NewMealSession(),
        PastRecipes(),
      ],
      bottomNavigationBuilder: (_, tabsRouter) {
        return BottomNavigationBar(
          currentIndex: tabsRouter.activeIndex,
          onTap: tabsRouter.setActiveIndex,
          items: [
            BottomNavigationBarItem(
              icon: Icon(Icons.soup_kitchen),
              label: 'New Meal Session',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.kitchen),
              label: 'Past Recipes',
            ),
          ],
        );
      },
    );
  }
}