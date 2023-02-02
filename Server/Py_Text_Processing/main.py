import json

from utilities import extract_recipe_text
from extract_instructions import extract_text_from_steps
from step import Step

def parse_recipe():
    # TODO: convert text file as cmd line parameter
    text_steps = extract_recipe_text('test.txt')

    # Parse each step from text into Step object
    parsed_steps = extract_text_from_steps(text_steps)
    print(parsed_steps)

    # TODO: Iterate over steps to extract holding resource

    json_steps = []
    for step in parsed_steps:
        json_steps.append(step.__dict__)

    print(json.dumps(json_steps))
    # return json
    
parse_recipe()