
def extract_recipe_text(filename):
    instr_steps = []
    # Parse recipe text into array
    count = 0
    with open ("./input/"+filename, 'rt') as recipe:
        for instr_line in recipe:
            mini_steps = instr_line.split('.')
            # print(mini_steps)
            for sent in mini_steps:
                if sent == '' or sent == '\n':
                    continue
                instr_steps.append(sent)
            instr_steps.append('BREAK' + str(count))
            count += 1
            
    return instr_steps