class FriendsList {
  List<String>? _friends;

  FriendsList({List<String>? friends}) {
    if (friends != null) {
      this._friends = friends;
    }
  }

  List<String>? get friends => _friends;
  set friends(List<String>? friends) => _friends = friends;

  FriendsList.fromJson(Map<String, dynamic> json) {
    _friends = json['friends'].cast<String>();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['friends'] = this._friends;
    return data;
  }
}