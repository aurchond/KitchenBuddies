import spacy 
from spacy import displacy
nlp = spacy.load("en_core_web_sm")

from utilities import extract_recipe_text
from step import Step

output_steps = []

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

def extract_text_from_steps(instr_steps):
    '''
    Input a list of steps and extract important components

    :return: List of dictionary containing verb, ingredients, kitchen supplies, and time for each step
    '''
    steps_out = []

    for instr in instr_steps: 
        if 'BREAK' in instr:
            # used to help understand dependencies, should we make this a step for the time being?
            print('STEP:', instr)
        #     break
        # print('STEP:', step)

        # create a new step object
        step = Step(instr)

        # build a custom pipeline to produce better nlp results?
        # clean sentence
        doc = nlp(instr)

        # track the index in the sentence
        idx = 0
        for token in doc:
            # print(token.text, token.lemma_, token.pos_, token.tag_, token.dep_,[child for child in token.children])

            if verb_condition(token, idx):
                # Do we need to store the verb
                print(token.text, " ", token.tag_, token.dep_,[child for child in token.children])
            # elif ingredient_condition
            #   (ingredient, quantitie) = ingredient_fn
            #   append to ingredient list
            # elif supplies_condition
            #   supply = supplies_fn
            #   append to supply list
            # elif time_condition
            #   step.stepTime = time_fn

            idx += 1
        
        
        # steps_out.append(step) 
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
