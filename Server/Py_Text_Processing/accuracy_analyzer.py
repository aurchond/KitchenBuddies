# iterate through each section of each step and check how many items are correct

# print out accuracy

import json

def compare_outputs(filename)

    # Read in the first JSON file
    with open('Py_Text_Processing/output/' + filename, 'r') as f:
        json1 = json.load(f)

    # Read in the second JSON file
    with open('Py_Text_Processing/expected/exp_' + filename, 'r') as f:
        json2 = json.load(f)

    # Check if the JSON arrays are the same size
    if len(json1) != len(json2):
        print("The JSON arrays are not the same size.")
        return 0

    # weight of each field per step
    weight_dict = {"instructions": 0.05,
                  "ingredientList": 0.2,
                  "ingredientQuantity": 0.025,
                  "resourcesRequired": 0.2,
                  "prepStep": 0.025,
                  "stepTime": 0.25,
                  "userTime": 0.05,
                  "holdingResouce": 0.25,
                  "holdingID": 0.05}

    # Compare each field in the JSON arrays and add a mark if the fields match
    total_fields = 0
    total_marks = 0
    for i in range(len(json1)):
        for step_id in json1[i]:
            step_accuracy = 0
            for field in step:
                if field == 'instructions' and json1[step_id][field] == json2[step_id][field]:
                    step_accuracy += weight_dict[field]
                elif field == 'ingredientList':
                    gen_ingr = json1[step_id][field]
                    exp_ingr = json2[step_id][field]
                    step_accuracy += compare_arrays(gen_ingr, exp_ingr, weight_dict[field])
                elif field == 'ingredientQuantity':
                    gen_quant = json1[step_id][field]
                    exp_quant = json2[step_id][field]
                    num_correct = 0

                    for j in range(len(exp_quant)):
                        if j < len(gen_quant) and gen_quant[j] == exp_quant[j]:
                            num_correct += 1
                    
                    step_accuracy += weight_dict[field]*(num_correct/len(exp_ingr))

        for key in json1[i]:
            if key in json2[i] and json1[i][key] == json2[i][key]:
                total_marks += 1
            total_fields += 1

    # Calculate the percentage of matching fields
    matching_percentage = (total_marks / total_fields) * 100

    # Print the percentage of matching fields
    print(f"The percentage of matching fields is {matching_percentage}%")
    You can modify the script to use the file names and paths of your JSON files. Note that this script assumes that the JSON arrays have the same structure and the same keys. If the JSON arrays have different structures or different keys, you will need to modify the script accordingly.

def compare_arrays(gen_array, exp_array, weight):
    num_correct = 0

    for j in range(len(exp_array)):
        if j < len(gen_array) and gen_array[j] == exp_array[j]:
            num_correct += 1
    
    return weight*(num_correct/len(exp_array))



Regenerate response