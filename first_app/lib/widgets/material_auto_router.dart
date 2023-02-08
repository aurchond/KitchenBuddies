import 'package:auto_route/annotations.dart';
import 'package:first_app/dashboard.dart';
import 'package:first_app/screens/current_meal_session.dart';
import 'package:first_app/screens/home_page_screen.dart';
import 'package:first_app/screens/new_meal_session_screen.dart';
import 'package:first_app/screens/past_recipes_screen.dart';

@MaterialAutoRouter(
  replaceInRouteName: 'Page,Route',
  routes: <AutoRoute>[
    AutoRoute(
      path: "/",
      page: Dashboard,
      children: [
        AutoRoute(
          path: "newmeal/",
          page: NewMealSession,
          children: [
            AutoRoute(path: "currentmeal", page: CurrentMealSession)
          ]
        ),
        // our AccountRouter has been moved into the children field
        AutoRoute(
          path: "recipes",
          page: PastRecipes
        ),
      ],
    ),
  ],
)
class $AppRouter {}