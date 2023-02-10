import 'package:first_app/backend_processing/service_class.dart';
import 'package:flutter/cupertino.dart';

import 'data_model.dart';

class DataClass extends ChangeNotifier {
  UserRecipeDetails? post;
  bool loading = false;

  getPostData(int jsonIndex) async {
    loading = true;
    post = (await getSinglePostData(jsonIndex));
    loading = false;

    notifyListeners();
  }
}