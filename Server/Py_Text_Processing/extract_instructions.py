import re
import spacy 
from spacy import displacy
nlp = spacy.load("en_core_web_sm")

from utilities import extract_recipe_text
from mysql_db import debug_verify_ingr_supplies
from step import Step

data_path = "./Py_Text_Processing/"

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
    line_num_count = 0

    # print(steps_out)

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
        if 'BREAK' in step.instructions: line_num_count += 1
        step.lineNumber = line_num_count

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
                   # if str(token) == 'pot': print('yay a pot', step_words)
                    #print(str(token))
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
                
                holding_res_found, hold_res_dic, hold_res_count = step.extract_holdingres_from_step(token, children, step_words, hold_res_dic, hold_res_count)


                # NOTE: maybe add hold_res_count and dic to the step property

            idx += 1

        # print(step.instructions, key_words)
        # Verify ingredients and supplies
        ingr_base_words = step.verify_key_words(key_words, verbose_ingr, verbose_supply, recipe_ingredients)
        #print(ingr_base_words)


        step.define_prep_step()

        # Approximate Step Time
        if step.stepTime == -1:
            # userTime also equals stepTime
            #print('edge case:', step.instructions)
            step.approximate_step_time()
        else:
            # userTime will most likely be less than stepTime
            step.approx_user_time()
        

        if holding_res_found == False:
            hold_res_count, hold_res_dic = step.holdingres_edge_case(hold_res_count, hold_res_dic, step_words, steps_out, resource_dataset, ingr_base_words, all_ingr_base_words)
            # print(step_words)
        
        hold_res_nonnumeric = re.sub('\d+', '', step.holdingResource)   #if step.holdingResource = 'bowl1' and step.resourcesRequired = ['separate bowl'], then it will replace 'separate bowl' with 'bowl1'
        for resource in step.resourcesRequired:
            if hold_res_nonnumeric in resource: 
                step.resourcesRequired.remove(resource)
                step.resourcesRequired.append(step.holdingResource)
        
        if len(step.resourcesRequired) == 0 and step.holdingResource != '':
            step.resourcesRequired.append(step.holdingResource)

        steps_out.append(step)
        step.baseIngredients = ingr_base_words
        all_ingr_base_words.append(ingr_base_words)
        
    print(all_ingr_base_words)

        #print(step.instructions, step.lineNumber)
            #make the previous holding resource also the current one
            #else: check to see if there is overlap with ingredients in the current step with any of the previous steps
            #if nothing has been found, then make the previous holding resource current one

    return steps_out


def verb_condition(token, pos) -> bool:
    not_verb_dict = {"advcl": '', # might need this one
                     "amod": ''} 
                     # we need conj
                     # xcomp
                     # dobj
                     # nmod
    ingr_dummy, supply_dummy = debug_verify_ingr_supplies([str(token)])
    if ((token.pos_ == 'VERB' and token.dep_ not in not_verb_dict) \
        or (token.pos_ == 'PROPN' and pos == 0)) and len(ingr_dummy) == 0:
        return True
    
    # might need to include adv, adj at beginning, propn at beginning of sentence, noun (beginning of sentence and middle of sentence)
    #         

    return False

def noun_condition(token, time_key_words, ingredients_in_step):
    if token.pos_ == 'VERB':        #this is here to consider cases where Spacy reads an ingredient such as 'seasoning' as a VERB accidentally
        ingr_dummy, supply_dummy = debug_verify_ingr_supplies([str(token)])
        if len(ingr_dummy) != 0: 
            return True
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


#parsed_steps = extract_text_from_steps(ingredient_cookies, text_steps)
#for steps in parsed_steps:
   # print('FINAL')
    #if 'BREAK' not in steps.instructions: 
       # print(steps.instructions)
        #print(steps.holdingResource)