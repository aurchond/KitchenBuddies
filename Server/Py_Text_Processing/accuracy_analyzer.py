# iterate through each section of each step and check how many items are correct

# print out accuracy

import json

def compare_outputs(filename):

    # Read in the first JSON file
    with open('Py_Text_Processing/output/' + filename, 'r') as f:
        json1 = json.load(f)

    # Read in the second JSON file
    with open('Py_Text_Processing/expected/exp_' + filename, 'r') as f:
        try:
            json2 = json.load(f)
        except json.decoder.JSONDecodeError as e:
            print(f"Error: Failed to parse JSON file: {e}")
            return 0

    # Check if the JSON arrays are the same size
    if len(json1) != len(json2):
        print("The JSON arrays are not the same size.")
        return 0

    # weight of each field per step
    weight_dict = {"instructions": 0.05,
                  "ingredients": 0.125,
                  "baseIngredients": 0.1,
                  "ingredientsQuantity": 0.025,
                  "resourcesRequired": 0.2,
                  "stepTime": 0.20,
                  "userTime": 0.05,
                  "holdingResource": 0.20,
                  "holdingID": 0.05}
    
    avg_dict = {"instructions": 0,
                  "ingredients": 0,
                  "baseIngredients": 0,
                  "ingredientsQuantity": 0,
                  "resourcesRequired": 0,
                  "stepTime": 0,
                  "userTime": 0,
                  "holdingResource": 0,
                  "holdingID": 0}

    # Compare each field in the JSON arrays and add a mark if the fields match
    total_fields = 0
    total_marks = 0
    for i in range(len(json1)):
        print("RESULTS FOR STEP", i)
        for step_id in json1[i]:
            step_accuracy = 0
            #print(json1[i][step_id])
            #print(json1[i][step_id]['instructions'])
            for field in json1[i][step_id]:
                #print(field)

                if field == 'instructions' and json1[i][step_id][field] == json2[i][step_id][field]:
                    avg_dict[field] += 100
                    step_accuracy += weight_dict[field]

                elif field == 'ingredients' or field == 'ingredientsQuantity' or field == 'baseIngredients' or field == 'resourcesRequired':
                    gen = json1[i][step_id][field]
                    exp = json2[i][step_id][field]
                    field_weight = compare_arrays(gen, exp, weight_dict[field])
                    step_accuracy += field_weight
                    avg_dict[field] += 100*(field_weight/weight_dict[field])
                    print('The accuracy of', field, "is:", 100*(field_weight/weight_dict[field]), "%") 

                elif field == 'stepTime' or field == 'userTime' or field == 'holdingResource' or field == 'holdingID':
                    if json1[i][step_id][field] == json2[i][step_id][field]: 
                        step_accuracy += weight_dict[field]
                        avg_dict[field] += 100
                        print('The accuracy of', field, "is:", "100%")
                    else:
                        print('The accuracy of', field, "is:", "0%")
            total_marks += step_accuracy
    
    for field in avg_dict:
        print(field, "AVERAGE", avg_dict[field]/len(json1))

    total_accuracy = (total_marks/len(json1)) * 100
    print('TOTAL ACCURACY FOR', filename, ':', total_accuracy, '%')
    return total_accuracy


'''
                elif field == 'ingredientsQuantity':
                    gen_quant = json1[i][step_id][field]
                    exp_quant = json2[i][step_id][field]
                    field_weight = compare_arrays(gen_quant, exp_quant, weight_dict[field])
                    step_accuracy += field_weight
                    avg_dict[field] += 100*(field_weight/weight_dict[field])
                    print('The accuracy of', field, "is:", 100*(field_weight/weight_dict[field]), "%") 

                elif field == 'resourcesRequired':
                    gen_res = json1[i][step_id][field]
                    exp_res = json2[i][step_id][field]
                    field_weight = compare_arrays(gen_res, exp_res, weight_dict[field])
                    step_accuracy += field_weight
                    avg_dict[field] += 100*(field_weight/weight_dict[field])
                    print('The accuracy of', field, "is:", 100*(field_weight/weight_dict[field])) 
'''

'''             
                    for j in range(len(exp_quant)):
                        if j < len(gen_quant) and gen_quant[j] == exp_quant[j]:
                            num_correct += 1
                    
                    step_accuracy += weight_dict[field]*(num_correct/len(exp_ingr))
'''
'''
        for key in json1[i]:
            if key in json2[i] and json1[i][key] == json2[i][key]:
                total_marks += 1
            total_fields += 1

    # Calculate the percentage of matching fields
    matching_percentage = (total_marks / total_fields) * 100

    # Print the percentage of matching fields
    print(f"The percentage of matching fields is {matching_percentage}%")
    You can modify the script to use the file names and paths of your JSON files. Note that this script assumes that the JSON arrays have the same structure and the same keys. If the JSON arrays have different structures or different keys, you will need to modify the script accordingly.
'''
def compare_arrays(gen_array, exp_array, weight):
    num_correct = 0

    if len(exp_array) > 0: 
        for j in range(len(exp_array)):
            if j < len(gen_array) and gen_array[j] == exp_array[j]:
                num_correct += 1
        
        return weight*(num_correct/len(exp_array))
    else: 
        if (len(gen_array) == 0 and len(exp_array) == 0):
            return weight
        else: return 0

test_outputs = [
'AC_Summer_Corn_Chowder.json', 
'DEMO_Cinnamon_Apple_Cake.json', 
'DEMO_Greek_Pasta_Salad.json', 
'DEMO_How_to_Steam_Broccoli_Perfectly_Every_Time.json', 
'DEMO_Pork_Schnitzel.json', 
'AC_One_Pan_Spanish_Chicken_and_Rice.json', 
'AC_Egg_Roll_in_a_Bowl.json', 
'AC_The_Best_Classic_Chili_Recipe.json',
'AC_Beer_Braised_Pork_Knuckles_with_Caraway,_Garlic,_Apples_and_Potatoes.json']

total = 0
for test_file in test_outputs:
    total += compare_outputs(test_file)

print('TOTAL AVERAGE:', total/len(test_outputs))


#compare_outputs('AC_Summer_Corn_Chowder.json')