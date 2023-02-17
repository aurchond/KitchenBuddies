import os
import json

data_path = "./Py_Text_Processing/"

def extract_recipe_text(filename):
    '''
    Input file should have structure of:
    # Name
    # Total Time: X

    # Ingredients:
    # X

    # Instructions:
    # X

    This function extracts the ingredients and instructions from it

    :return: List of ingredients and List of instructions
    '''

    # cwd = os.getcwd()
    # print("Current working directory:", cwd)
    ingredients = []
    instr_steps = []

    # Parse recipe text into array
    ingr_section = False
    instr_section = False
    instr_line_count = 0
    with open (data_path + 'input/'+filename, 'rt') as recipe:
        for instr_line in recipe:
            if 'Ingredients:' in instr_line:
                ingr_section = True
                continue

            if 'Instructions:' in instr_line:
                ingr_section = False
                instr_section = True
                continue

            if ingr_section:
                ingredients.append(instr_line.split('\n')[0])
            
            if instr_section:
                mini_steps = instr_line.split('.')
                # print(mini_steps)
                for sent in mini_steps:
                    if sent == '' or sent == '\n':
                        continue
                    instr_steps.append(sent)
                instr_steps.append('BREAK' + str(instr_line_count))
                instr_line_count += 1
    
    return ingredients, instr_steps

# Used for Debugging
def extract_instructions(file):
    instr_steps = []
    instr_section = False
    instr_line_count = 0
    with open (data_path + 'input/'+file, 'rt') as recipe:
        for instr_line in recipe:

            if 'Instructions:' in instr_line:
                instr_section = True
                continue

            if instr_section:
                mini_steps = instr_line.split('.')
                # print(mini_steps)
                for sent in mini_steps:
                    if sent == '' or sent == '\n':
                        continue
                    instr_steps.append(sent)
                instr_steps.append('BREAK' + str(instr_line_count))
                instr_line_count += 1
    
    return instr_steps

def write_step_json(out_file, json_steps):
    with open(data_path + '/output/' + out_file + ".json", "w") as json_file:
        json.dump(json_steps, json_file)
