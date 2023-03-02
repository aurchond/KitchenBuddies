import sys

from utilities import extract_recipe_text, write_step_json
from extract_instructions import extract_text_from_steps
from step import Step

def parse_recipe():
    recipe_file = ""
    if len(sys.argv) > 1:
        recipe_file = sys.argv[1]
        # print("The parameter is:", recipe_file)
    else:
        print("Parameter error, no input file")
        return

    ingredients, text_steps = extract_recipe_text(recipe_file)

    # Parse each step from text into Step object
    parsed_steps = extract_text_from_steps(ingredients, text_steps)
    # print(parsed_steps)

    # TODO: Iterate over steps to extract holding resource

    json_steps = []
    count = 1
    for step in parsed_steps:
        step_details = step.__dict__
        if 'BREAK' in step_details['instructions']:
            continue

        # TODO: Remove holding_res_count and holding_dic from step
        step_dict = {count: step_details}
        json_steps.append(step_dict)
        count += 1

    # Output json to output folder
    write_step_json(recipe_file.split('.txt')[0], json_steps)
    print('Recipe Steps Successfully Parsed!')

if __name__ == "__main__":
    parse_recipe()
