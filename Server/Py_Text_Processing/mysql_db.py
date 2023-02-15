import mysql.connector

def verify_ingredients_supplies(istr_nouns):
    '''
    Check which nouns within the sentence are ingredients

    :istr_nouns: List of nouns within recipe instruction
    :return: List of ingredients
    '''
    # create ingredient query string
    query = 'SELECT * FROM Food WHERE Name IN ('

    for i in range(len(istr_nouns)):
        where_clause = f"\'{istr_nouns[i]}\'"
        if i < len(istr_nouns)-1:
            where_clause += ','
        else:
            where_clause += ');'
        query += where_clause
    
    print(query)

    # connect to database
    mydb = mysql.connector.connect(
        host="localhost",
        user="shadi",
        password="password",
        database="KitchenBuddies"
    )

    mycursor = mydb.cursor()

    # run ingredient query - return only ingredients
    mycursor.execute(query)

    results = mycursor.fetchall()
    print(results)

    items = []

    for row in results:
        items.append(row[0])

    print(items)

    # Change table to extract supplies
    supply_query = query.replace('Food', 'KitchenSupplies')
    print(supply_query)
    mycursor.execute(supply_query)

    results = mycursor.fetchall()
    print(results)

    supplies = []

    for row in results:
        supplies.append(row[0])

    print(supplies)

    mycursor.close()
    mydb.close()

    # run supply query - return only supplies

    # return (ingredients, supplies)

verify_ingredients_supplies(['pan', 'parsley', 'zucchini'])


