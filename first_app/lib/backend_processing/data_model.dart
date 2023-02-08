class UserDetails {
  List<Users>? users;

  UserDetails({this.users});

  UserDetails.fromJson(Map<String, dynamic> json) {
    if (json['Users'] != null) {
      users = <Users>[];
      json['Users'].forEach((v) {
        users!.add(new Users.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.users != null) {
      data['Users'] = this.users!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Users {
  User? user;

  Users({this.user});

  Users.fromJson(Map<String, dynamic> json) {
    user = json['User'] != null ? new User.fromJson(json['User']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.user != null) {
      data['User'] = this.user!.toJson();
    }
    return data;
  }
}

class User {
  String? name;
  List<Steps>? steps;

  User({this.name, this.steps});

  User.fromJson(Map<String, dynamic> json) {
    name = json['Name'];
    if (json['Steps'] != null) {
      steps = <Steps>[];
      json['Steps'].forEach((v) {
        steps!.add(new Steps.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['Name'] = this.name;
    if (this.steps != null) {
      data['Steps'] = this.steps!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Steps {
  Step? step;

  Steps({this.step});

  Steps.fromJson(Map<String, dynamic> json) {
    step = json['Step'] != null ? new Step.fromJson(json['Step']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.step != null) {
      data['Step'] = this.step!.toJson();
    }
    return data;
  }
}

class Step {
  String? number;
  String? instructions;
  List<String>? ingredientList;
  List<int>? ingredientQuantity;
  String? dependencyItem;
  String? nextPerson;

  Step(
      {this.number,
        this.instructions,
        this.ingredientList,
        this.ingredientQuantity,
        this.dependencyItem,
        this.nextPerson});

  Step.fromJson(Map<String, dynamic> json) {
    number = json['number'];
    instructions = json['instructions'];
    ingredientList = json['ingredientList'].cast<String>();
    ingredientQuantity = json['ingredientQuantity'].cast<int>();
    dependencyItem = json['dependencyItem'];
    nextPerson = json['nextPerson'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['number'] = this.number;
    data['instructions'] = this.instructions;
    data['ingredientList'] = this.ingredientList;
    data['ingredientQuantity'] = this.ingredientQuantity;
    data['dependencyItem'] = this.dependencyItem;
    data['nextPerson'] = this.nextPerson;
    return data;
  }
}