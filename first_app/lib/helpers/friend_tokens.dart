import 'globals.dart';

Map<String,String> removeMyTokenFromMap(Map<String,String> allTokensMap)  {
  Map<String, String> newTokensMap = new Map<String, String>.from(allTokensMap);
  newTokensMap.removeWhere((key, value) => key == myEmail);

  return newTokensMap;
}

List<String> removeMyTokenFromList(List<String> tokensList, String tokenToRemove)  {
  int removeIndex = -1; //yikes
  for (int i=0; i<tokensList.length; i++) {
    if (tokensList[i] == tokenToRemove) {
      removeIndex = i;
    }
  }

  if (removeIndex != -1) {
    print(tokensList);
    tokensList.removeAt(removeIndex);
    print(tokensList);
    return tokensList;
  }
    return tokensList;
}

