# Server Maintenance
This document provides understanding of our various components and how to use them

When you enter the server, make sure to enter at KitchenBuddies/Server. This helps with filepaths

## Databases
---
### Neo4j

*Explore Graph Database Visually*
https://console.neo4j.io/
Use Neo4j login (found on discord -> Utilities -> Pinned Message)
Go to the query and login using the Neo4J Explorer credientials (also found in discord)
Run `MATCH (n) RETURN n` to return all nodes

`MATCH (n:Step {recipeID: 12}) RETURN n`

To delete a recipe in Neo4J:
```
MATCH (n:Step {recipeID: 12})-[r]-()
DETACH DELETE n, r
```

### MySQL
On the terminal run, `sudo mysql` and enter the password. This should take you to the mysql terminal. 
Run `USE KitchenBuddies` to enter the KB Database.
From here, run any mysql query you'd like! Make sure to add the `;` at the end of the command to run it.
To exit, run `exit` on the mysql terminal.

## Spring Application
---
If you have changes within the code that haven't been built, run `./gradlew build` from the `KitchenBuddies/Server` directory. Once you receive a build success, there
is a jar file located within `build/libs` called `Server-1.0-SNAPSHOT.jar`. This is our executable. 

Next run, `java -jar ./build/libs/Server-1.0-SNAPSHOT.jar`. This will run our compiled code as well as our Spring Application

## Text Processing (Python)
---
*Debugging Text Processing*
`python3 ./Py_Text_Processing/main.py <input.txt>`
where `input.txt` is the recipe text file within `Py_Text_Processing/input`



