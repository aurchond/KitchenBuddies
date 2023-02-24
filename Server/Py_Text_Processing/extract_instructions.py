import spacy 
from spacy import displacy
nlp = spacy.load("en_core_web_sm")

from utilities import extract_recipe_text
from step import Step

data_path = "./Py_Text_Processing/"

def extract_verb_from_steps(instr_steps):
    verb_list = []

    for instr in instr_steps: 
        if 'BREAK' in instr:
            verb_list.append([])
            #print('STEP:', instr)
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
                #print(token.text, " ", token.tag_, token.dep_,[child for child in token.children])

            idx += 1
        verb_list.append(verbs)

    return verb_list

def extract_text_from_steps(recipe_ingredients, instr_steps):
    '''
    Input a list of steps and extract important components

    :return: List of dictionary containing verb, ingredients, kitchen supplies, and time for each step
    '''
    steps_out = []
    all_ingr_base_words = []
    time_key_words = ['seconds','minute','minutes','hours','hour']
    resource_dataset=[]
    hold_res_dic = {}
    hold_res_count = 0

    with open(data_path + "/data/supplies.txt", 'r') as file:
        for words in file: 
            resource_dataset.append(words.rstrip().lower())
        
    #print(resource_dataset)

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
        ingr_base_words = []

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

                    num_words, full_ingr, quantity, validity = step.extract_full_noun_from_step(token, children)
                    skip_words += num_words
                    # apples, bananas, and cherries
                    # apple, (lushish apples, quantity)
                    if validity == True: 
                        key_words.append(potential_ingr)
                        verbose_ingr[potential_ingr] = (full_ingr, quantity)
            else:
                skip_words -= 1
            
            if time_condition(token, time_key_words):
                step.extract_time_from_step(token, children)
            
            if holding_resource_condition(token, resource_dataset, step_words) and holding_res_found == False:
                #print('yes')
                # put some in bag and then pour onto pan
                # NOTE: assume the last supply is the holding resource
                # bag or pan
                
                holding_res_found, hold_res_dic, hold_res_count = step.extract_holdingres_from_step(token, step_words, hold_res_dic, hold_res_count)


                # NOTE: maybe add hold_res_count and dic to the step property

            idx += 1

        # Verify ingredients and supplies
        ingr_base_words = step.verify_key_words(key_words, verbose_ingr, verbose_supply, recipe_ingredients)
        #print(ingr_base_words)


        step.define_prep_step()

        # Approximate Step Time
        if step.stepTime == -1:
            # userTime also equals stepTime
            step.approximate_step_time()
        else:
            # userTime will most likely be less than stepTime
            step.approx_user_time()
        

        if holding_res_found == False:
            step.holdingres_edge_case(hold_res_count, hold_res_dic, step_words, steps_out, resource_dataset, ingr_base_words, all_ingr_base_words)
            # print(step_words)
        
        steps_out.append(step)
        all_ingr_base_words.append(ingr_base_words)
            # check_oven_keywords = ['Bake', 'bake', 'broil', 'Broil', 'Roast', 'roast']
            # #need to check for the oven being a holding resource (look for words like 'bake' or 'broil' or 'oven')
            # string_words = [str(i) for i in step_words]
            # if len(set(check_oven_keywords).intersection(set(string_words))) != 0:
            #     # Set oven to holding resource
            #     # NOTE: Change this to the resource holding the food
            #     step.holdingResource = 'oven'
            #     step.holdingID = hold_res_count
            #     hold_res_count += 1      

            # elif len(steps_out) == 0: #if it is the first step
            #     for word in step_words: 
            #         if str(word) in resource_dataset and str(word) != "cup":
            #             step.holdingResource = str(word)
            #             step.holdingID = hold_res_count
            #             hold_res_count += 1      
            #     if step.holdingResource == '': step.holdingResource = 'N/A'

            # elif 'BREAK' not in steps_out[-1].instructions:
            #     step.holdingResource = steps_out[-1].holdingResource
            #     step.holdingID = steps_out[-1].holdingID

            # elif 'BREAK' in steps_out[-1].instructions:   #if the instruction string does contain BREAK
            #     # NOTE: Assumes that ingredients are labelled the same across steps (i.e. milk, dough -> batter)
            #     set1 = set(ingr_base_words)
            #     for test_index in range(len(all_ingr_base_words)):
            #         max_count = 0
            #         set2 = set(all_ingr_base_words[test_index]) 
            #         #print(set1)
            #         #print(set2)
            #         common_ingredients = set1.intersection(set2)
            #         if len(common_ingredients) > max_count: 
            #             step.holdingResource = steps_out[test_index].holdingResource
            #             step.holdingID = steps_out[test_index].holdingID
            #             max_count = len(common_ingredients)
            #     if len(common_ingredients) == 0:
            #         step.holdingResource = steps_out[-2].holdingResource #just a holder for now, need to consider this edge case 
            #         step.holdingID = steps_out[-2].holdingID

            #make the previous holding resource also the current one
            #else: check to see if there is overlap with ingredients in the current step with any of the previous steps
            #if nothing has been found, then make the previous holding resource current one

        
        

    


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
        return True

    return False 

        

ingredient_cookies, text_steps = extract_recipe_text('test.txt')

parsed_steps = extract_text_from_steps(ingredient_cookies, text_steps)
#for steps in parsed_steps:
   # print('FINAL')
    #if 'BREAK' not in steps.instructions: 
       # print(steps.instructions)
        #print(steps.holdingResource)