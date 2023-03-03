class KitchenConstraints {
  String? _userEmail;
  int? _pot;
  int? _pan;
  int? _bowl;
  int? _cuttingBoard;
  int? _oven;
  int? _microwave;

  KitchenConstraints(
      {String? userEmail,
        int? pot,
        int? pan,
        int? bowl,
        int? cuttingBoard,
        int? oven,
        int? microwave}) {
    if (userEmail != null) {
      this._userEmail = userEmail;
    }
    if (pot != null) {
      this._pot = pot;
    }
    if (pan != null) {
      this._pan = pan;
    }
    if (bowl != null) {
      this._bowl = bowl;
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
  int? get pot => _pot;
  set pot(int? pot) => _pot = pot;
  int? get pan => _pan;
  set pan(int? pan) => _pan = pan;
  int? get bowl => _bowl;
  set bowl(int? bowl) => _bowl = bowl;
  int? get cuttingBoard => _cuttingBoard;
  set cuttingBoard(int? cuttingBoard) => _cuttingBoard = cuttingBoard;
  int? get oven => _oven;
  set oven(int? oven) => _oven = oven;
  int? get microwave => _microwave;
  set microwave(int? microwave) => _microwave = microwave;

  KitchenConstraints.fromJson(Map<String, dynamic> json) {
    _userEmail = json['userEmail'];
    _pot = json['pot'];
    _pan = json['pan'];
    _bowl = json['bowl'];
    _cuttingBoard = json['cuttingBoard'];
    _oven = json['oven'];
    _microwave = json['microwave'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userEmail'] = this._userEmail;
    data['pot'] = this._pot;
    data['pan'] = this._pan;
    data['bowl'] = this._bowl;
    data['cuttingBoard'] = this._cuttingBoard;
    data['oven'] = this._oven;
    data['microwave'] = this._microwave;
    return data;
  }
}