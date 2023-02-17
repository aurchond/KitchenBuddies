class KitchenConstraints {
  String? _userEmail;
  int? _skill;
  int? _burner;
  int? _pot;
  int? _pan;
  int? _knife;
  int? _cuttingBoard;
  int? _oven;
  int? _microwave;

  KitchenConstraints(
      {String? userEmail,
        int? skill,
        int? burner,
        int? pot,
        int? pan,
        int? knife,
        int? cuttingBoard,
        int? oven,
        int? microwave}) {
    if (userEmail != null) {
      this._userEmail = userEmail;
    }
    if (skill != null) {
      this._skill = skill;
    }
    if (burner != null) {
      this._burner = burner;
    }
    if (pot != null) {
      this._pot = pot;
    }
    if (pan != null) {
      this._pan = pan;
    }
    if (knife != null) {
      this._knife = knife;
    }
    if (cuttingBoard != null) {
      this._cuttingBoard = cuttingBoard;
    }
    if (oven != null) {
      this._oven = oven;
    }
    if (microwave != null) {
      this._microwave = microwave;
    }
  }

  String? get userEmail => _userEmail;
  set userEmail(String? userEmail) => _userEmail = userEmail;
  int? get skill => _skill;
  set skill(int? skill) => _skill = skill;
  int? get burner => _burner;
  set burner(int? burner) => _burner = burner;
  int? get pot => _pot;
  set pot(int? pot) => _pot = pot;
  int? get pan => _pan;
  set pan(int? pan) => _pan = pan;
  int? get knife => _knife;
  set knife(int? knife) => _knife = knife;
  int? get cuttingBoard => _cuttingBoard;
  set cuttingBoard(int? cuttingBoard) => _cuttingBoard = cuttingBoard;
  int? get oven => _oven;
  set oven(int? oven) => _oven = oven;
  int? get microwave => _microwave;
  set microwave(int? microwave) => _microwave = microwave;

  KitchenConstraints.fromJson(Map<String, dynamic> json) {
    _userEmail = json['userEmail'];
    _skill = json['skill'];
    _burner = json['burner'];
    _pot = json['pot'];
    _pan = json['pan'];
    _knife = json['knife'];
    _cuttingBoard = json['cuttingBoard'];
    _oven = json['oven'];
    _microwave = json['microwave'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userEmail'] = this._userEmail;
    data['skill'] = this._skill;
    data['burner'] = this._burner;
    data['pot'] = this._pot;
    data['pan'] = this._pan;
    data['knife'] = this._knife;
    data['cuttingBoard'] = this._cuttingBoard;
    data['oven'] = this._oven;
    data['microwave'] = this._microwave;
    return data;
  }
}