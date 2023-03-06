import 'globals.dart';

Map<String,String> removeMyTokenFromMap(Map<String,String> allTokensMap)  {
  allTokensMap.forEach((email, token) {
    if (allTokensMap[email] == myEmail) {
      allTokensMap.remove(email);
    }
  });

  return allTokensMap;
}

List<String> removeMyTokenFromList(List<String> tokensMap, String tokenToRemoved)  {
  for (int i=0; i<tokensMap.length; i++) {
    if (tokensMap[i] == tokenToRemoved) {
      tokensMap.removeAt(i);
    }
  }

  return tokensMap;
}

