class SkillLevel {
  String? _userEmail;
  String? _skillLevel;

  SkillLevel({String? userEmail, String? skillLevel}) {
    if (userEmail != null) {
      this._userEmail = userEmail;
    }
    if (skillLevel != null) {
      this._skillLevel = skillLevel;
    }
  }

  String? get userEmail => _userEmail;
  set userEmail(String? userEmail) => _userEmail = userEmail;
  String? get skillLevel => _skillLevel;
  set skillLevel(String? skillLevel) => _skillLevel = skillLevel;

  SkillLevel.fromJson(Map<String, dynamic> json) {
    _userEmail = json['userEmail'];
    _skillLevel = json['skillLevel'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userEmail'] = this._userEmail;
    data['skillLevel'] = this._skillLevel;
    return data;
  }
}