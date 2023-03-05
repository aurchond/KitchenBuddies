import 'kitchen_constraints_model.dart';

class MealSessionStepsRequest {
  KitchenConstraints? _kitchenConstraints;
  List<int>? _recipeIDs;
  List<String>? _includedFriends;

  MealSessionStepsRequest(
      {KitchenConstraints? kitchenConstraints,
        List<int>? recipeIDs,
        List<String>? includedFriends}) {
    if (kitchenConstraints != null) {
      this._kitchenConstraints = kitchenConstraints;
    }
    if (recipeIDs != null) {
      this._recipeIDs = recipeIDs;
    }
    if (includedFriends != null) {
      this._includedFriends = includedFriends;
    }
  }

  KitchenConstraints? get kitchenConstraints => _kitchenConstraints;
  set kitchenConstraints(KitchenConstraints? kitchenConstraints) =>
      _kitchenConstraints = kitchenConstraints;
  List<int>? get recipeIDs => _recipeIDs;
  set recipeIDs(List<int>? recipeIDs) => _recipeIDs = recipeIDs;
  List<String>? get includedFriends => _includedFriends;
  set includedFriends(List<String>? includedFriends) =>
      _includedFriends = includedFriends;

  MealSessionStepsRequest.fromJson(Map<String, dynamic> json) {
    _kitchenConstraints = json['kitchenConstraints'] != null
        ? new KitchenConstraints.fromJson(json['kitchenConstraints'])
        : null;
    _recipeIDs = json['recipeIDs'].cast<int>();
    _includedFriends = json['includedFriends'].cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this._kitchenConstraints != null) {
      data['kitchenConstraints'] = this._kitchenConstraints!.toJson();
    }
    data['recipeIDs'] = this._recipeIDs;
    data['includedFriends'] = this._includedFriends;
    return data;
  }
}