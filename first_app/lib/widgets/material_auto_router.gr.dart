// **************************************************************************
// AutoRouteGenerator
// **************************************************************************

// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// AutoRouteGenerator
// **************************************************************************
//
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:auto_route/auto_route.dart' as _i5;
import 'package:flutter/material.dart' as _i6;

import '../dashboard.dart' as _i1;
import '../screens/current_meal_session.dart' as _i4;
import '../screens/new_meal_session_screen.dart' as _i2;
import '../screens/past_recipes_screen.dart' as _i3;

class AppRouter extends _i5.RootStackRouter {
  AppRouter([_i6.GlobalKey<_i6.NavigatorState>? navigatorKey])
      : super(navigatorKey);

  @override
  final Map<String, _i5.PageFactory> pagesMap = {
    Dashboard.name: (routeData) {
      return _i5.MaterialPageX<dynamic>(
        routeData: routeData,
        child: _i1.Dashboard(),
      );
    },
    NewMealSession.name: (routeData) {
      return _i5.MaterialPageX<dynamic>(
        routeData: routeData,
        child: const _i2.NewMealSession(),
      );
    },
    PastRecipes.name: (routeData) {
      return _i5.MaterialPageX<dynamic>(
        routeData: routeData,
        child: const _i3.PastRecipes(),
      );
    },
    CurrentMealSession.name: (routeData) {
      return _i5.MaterialPageX<dynamic>(
        routeData: routeData,
        child: const _i4.CurrentMealSession(),
      );
    },
  };

  @override
  List<_i5.RouteConfig> get routes => [
        _i5.RouteConfig(
          Dashboard.name,
          path: '/',
          children: [
            _i5.RouteConfig(
              NewMealSession.name,
              path: 'newmeal/',
              parent: Dashboard.name,
              children: [
                _i5.RouteConfig(
                  CurrentMealSession.name,
                  path: 'currentmeal',
                  parent: NewMealSession.name,
                )
              ],
            ),
            _i5.RouteConfig(
              PastRecipes.name,
              path: 'recipes',
              parent: Dashboard.name,
            ),
          ],
        )
      ];
}

/// generated route for
/// [_i1.Dashboard]
class Dashboard extends _i5.PageRouteInfo<void> {
  const Dashboard({List<_i5.PageRouteInfo>? children})
      : super(
          Dashboard.name,
          path: '/',
          initialChildren: children,
        );

  static const String name = 'Dashboard';
}

/// generated route for
/// [_i2.NewMealSession]
class NewMealSession extends _i5.PageRouteInfo<void> {
  const NewMealSession({List<_i5.PageRouteInfo>? children})
      : super(
          NewMealSession.name,
          path: 'newmeal/',
          initialChildren: children,
        );

  static const String name = 'NewMealSession';
}

/// generated route for
/// [_i3.PastRecipes]
class PastRecipes extends _i5.PageRouteInfo<void> {
  const PastRecipes()
      : super(
          PastRecipes.name,
          path: 'recipes',
        );

  static const String name = 'PastRecipes';
}

/// generated route for
/// [_i4.CurrentMealSession]
class CurrentMealSession extends _i5.PageRouteInfo<void> {
  const CurrentMealSession()
      : super(
          CurrentMealSession.name,
          path: 'currentmeal',
        );

  static const String name = 'CurrentMealSession';
}
