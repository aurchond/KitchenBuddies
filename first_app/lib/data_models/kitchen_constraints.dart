class KitchenConstraints {
  String? _userEmail;
  int? _oven;
  int? _pot;
  int? _pan;
  int? _bowl;
  int? _cuttingBoard;

  KitchenConstraints(
      {String? userEmail,
        int? oven,
        int? pot,
        int? pan,
        int? bowl,
        int? cuttingBoard}) {
    if (userEmail != null) {
      this._userEmail = userEmail;
    }
    if (oven != null) {
      this._oven = oven;
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
  }

  String? get userEmail => _userEmail;
  set userEmail(String? userEmail) => _userEmail = userEmail;
  int? get oven => _oven;
  set oven(int? oven) => _oven = oven;
  int? get pot => _pot;
  set pot(int? pot) => _pot = pot;
  int? get pan => _pan;
  set pan(int? pan) => _pan = pan;
  int? get bowl => _bowl;
  set bowl(int? bowl) => _bowl = bowl;
  int? get cuttingBoard => _cuttingBoard;
  set cuttingBoard(int? cuttingBoard) => _cuttingBoard = cuttingBoard;

  KitchenConstraints.fromJson(Map<String, dynamic> json) {
    _userEmail = json['userEmail'];
    _oven = json['oven'];
    _pot = json['pot'];
    _pan = json['pan'];
    _bowl = json['bowl'];
    _cuttingBoard = json['cuttingBoard'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userEmail'] = this._userEmail;
    data['oven'] = this._oven;
    data['pot'] = this._pot;
    data['pan'] = this._pan;
    data['bowl'] = this._bowl;
    data['cuttingBoard'] = this._cuttingBoard;
    return data;
  }
}