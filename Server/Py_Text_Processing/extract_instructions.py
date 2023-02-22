import spacy 
from spacy import displacy
nlp = spacy.load("en_core_web_sm")

from utilities import extract_recipe_text
from step import Step

def extract_verb_from_steps(instr_steps):
    verb_list = []

    for instr in instr_steps: 
        if 'BREAK' in instr:
            verb_list.append([])
            print('STEP:', instr)
            continue
        # print('STEP:', step)

        # create a new step object

        # build a custom pipeline to produce better nlp results?
        # clean sentence
        doc = nlp(instr)
        verbs = []

        idx = 0
        for token in doc:
            # print(token.text, token.lemma_, token.pos_, token.tag_, token.dep_,[child for child in token.children])
            # might need to include adv, adj at beginning, propn at beginning of sentence, noun (beginning of sentence and middle of sentence)
            if verb_condition(token, idx):
                verbs.append(token.text)
                print(token.text, " ", token.tag_, token.dep_,[child for child in token.children])

            idx += 1
        verb_list.append(verbs)

    return verb_list

def extract_text_from_steps(recipe_ingredients, instr_steps):
    '''
    Input a list of steps and extract important components

    :return: List of dictionary containing verb, ingredients, kitchen supplies, and time for each step
    '''
    steps_out = []
    time_key_words = ['seconds','minute','minutes','hours','hour']
    resource_dataset=[]

    with open(".\data\supplies.txt", 'r') as file:
        for words in file: 
            resource_dataset.append(words.rstrip().lower())
        
    print(resource_dataset)

    for instr in instr_steps: 
        # if 'BREAK' in instr:
        #     # used to help understand dependencies, should we make this a step for the time being?
        #     print('STEP:', instr)
        #     break
        # print('STEP:', step)

        # create a new step object
        step = Step(instr)

        # build a custom pipeline to produce better nlp results?
        # clean sentence
        doc = nlp(instr)

        step_words = []
        for token in doc: 
            step_words.append(token)

        # track the index in the sentence
        idx = 0
        skip_words = 0

        verbose_ingr = {}
        verbose_supply = {}
        key_words = []

        holding_res_found = False 
        for token in doc:
            # print(token.text, token.lemma_, token.pos_, token.tag_, token.dep_,[child for child in token.children])
            children =  [child for child in token.children]

            if verb_condition(token, idx):
                # Do we need to store the verb
                # print(token.text, " ", token.tag_, token.dep_,[child for child in token.children])
                step.extract_verb_from_step(token)

            if skip_words == 0:
                if noun_condition(token, time_key_words, step.ingredients):
                    potential_ingr = str(token).lower()
                    key_words.append(potential_ingr)

                    num_words, full_ingr, quantity = step.extract_full_noun_from_step(token, children)
                    skip_words += num_words
                    # apples, bananas, and cherries
                    # apple, (lushish apples, quantity)

                    verbose_ingr[potential_ingr] = (full_ingr, quantity)
            else:
                skip_words -= 1
            
            if time_condition(token, time_key_words):
                step.extract_time_from_step(token, children)
            
            if holding_resource_condition(token, resource_dataset, step_words) and holding_res_found == False:
                #print('yes')
                holding_res_found = step.extract_holdingres_from_step(token, step_words)
                #print(step.holdingResource)
                print(holding_res_found)

            idx += 1

        # Verify ingredients and supplies
        step.verify_key_words(key_words, verbose_ingr, verbose_supply, recipe_ingredients)

        step.define_prep_step()

        # Approximate Step Time
        if step.stepTime == -1:
            # userTime also equals stepTime
            step.approximate_step_time()
        else:
            # userTime will most likely be less than stepTime
            step.approx_user_time()
        
        if holding_res_found == False:
            #Check the most recently appended step in the steps_out list

            if len(steps_out) == 0: #if it is the first step
                for words in step_words: 
                    if str(words) in resource_dataset and str(words) != "cup":
                        step.holdingResource = str(words)
                if step.holdingResource == '': step.holdingResource = 'N/A'

            elif 'BREAK' not in steps_out[-1].instructions:
                step.holdingResource = steps_out[-1].holdingResource

            elif 'BREAK' in steps_out[-1].instructions:   #if the instruction string does contain BREAK
                for test_match in steps_out:
                    max_count = 0
                    set1 = set(test_match.ingredients) 
                    print(set1)
                    set2 = set(step.ingredients)
                    print(set2)
                    common_ingredients = set1.intersection(set2)
                    if len(common_ingredients) > max_count: 
                        step.holdingResource = test_match.holdingResource
                        max_count = len(common_ingredients)
                    if len(common_ingredients) == 0:
                        step.holdingResource = steps_out[-2].holdingResource

            #make the previous holding resource also the current one
            #else: check to see if there is overlap with ingredients in the current step with any of the previous steps
            #if nothing has been found, then make the previous holding resource current one

        
        
        steps_out.append(step)

    
        #***TO DO: CREATE HOLDING RESOURCE ALGORITHM IN HERE****



        # print("PARSED STEP:")
        # print(step.instructions)
        # print(step.ingredients)
        # print(step.ingredientsQuantity)
        # print(step.verbs)
        # print(step.stepTime)
    return steps_out


def verb_condition(token, pos) -> bool:
    not_verb_dict = {"advcl": '', # might need this one
                     "amod": ''} 
                     # we need conj
                     # xcomp
                     # dobj
                     # nmod
    if (token.pos_ == 'VERB' and token.dep_ not in not_verb_dict) \
        or (token.pos_ == 'PROPN' and pos == 0):
        return True
    
    # might need to include adv, adj at beginning, propn at beginning of sentence, noun (beginning of sentence and middle of sentence)
    #         

    return False

def noun_condition(token, time_key_words, ingredients_in_step):
    #checking for ingredients, making sure the noun is not a duplicate of a previously found ingredient or a time
    if token.pos_ == 'NOUN' and str(token) not in time_key_words \
        and str(token) not in ingredients_in_step:
        return True

    return False

def time_condition(token, time_key_words):
    if token.pos_ == 'NOUN' and str(token) in time_key_words: 
        return True

    return False

def holding_resource_condition(token, dataset, step_words):
    if token.pos_ == "NOUN" and str(token) in dataset:
        #print('heres the loop!!')
        return True

    return False 

        

ingredient_cookies, text_steps = extract_recipe_text('test.txt')

parsed_steps = extract_text_from_steps(ingredient_cookies, text_steps)
for steps in parsed_steps:
   # print('FINAL')
    if 'BREAK' not in steps.instructions: 
        print(steps.instructions)
        print(steps.holdingResource)