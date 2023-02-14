import spacy 
from spacy import displacy
nlp = spacy.load("en_core_web_sm")

from utilities import extract_recipe_text
from step import Step

output_steps = []

recipe_ingredients= [
            "0.5 cup white wine vinegar",
            "1.5 tablespoons sugar",
            "1 teaspoon thyme leaves",
            "0.25 cup canola oil (plus more for frying)",
            "Salt and freshly ground pepper",
            "and freshly ground pepper",
            "1 pound small fingerling potatoes",
            "3 garlic cloves",
            "1 cup all-purpose flour",
            "2 large eggs beaten with 2 tablespoons water",
            "2 cups panko breadcrumbs",
            "Four 4-ounce boneless pork chops, butterflied and pounded 1\/3-inch thick, or eight 2-ounce pork cutlets, lightly pounded",
            "Kosher salt",
            "pepper",
            "Canola",
            "1 cup flat-leaf parsley, patted thoroughly dry"
        ]
time_key_words = ['seconds','minute','minutes','hours','hour']

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
    time_key_words = ['seconds','minute','minutes','hours','hour']

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

        step_words = []
        for token in doc: 
            step_words.append(token)

        # track the index in the sentence
        idx = 0
        skip_words = 0
        for token in doc:
            # print(token.text, token.lemma_, token.pos_, token.tag_, token.dep_,[child for child in token.children])
            children =  [child for child in token.children]

            if verb_condition(token, idx):
                # Do we need to store the verb
                # print(token.text, " ", token.tag_, token.dep_,[child for child in token.children])
                step.extract_verb_from_step(token)

            if skip_words == 0:
                if ingredient_condition(token, time_key_words, step.ingredients):
                    skip_words += step.extract_ingredient_from_step(token, step_words, children, recipe_ingredients)
            else:
                skip_words -= 1
            
            if time_condition(token, time_key_words):
                step.extract_time_from_step(token, children)
            # elif supplies_condition
            #   supply = supplies_fn
            #   append to supply list

            idx += 1
        
        
        steps_out.append(step)
        print("PARSED STEP:")
        print(step.instructions)
        print(step.ingredients)
        print(step.ingredientsQuantity)
        print(step.verbs)
        print(step.stepTime)
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

def ingredient_condition(token, time_key_words, ingredients_in_step):
    #checking for ingredients, making sure the noun is not a duplicate of a previously found ingredient or a time
    if token.pos_ == 'NOUN' and str(token) not in time_key_words \
        and str(token) not in ingredients_in_step:
        return True

    return False

def time_condition(token, time_key_words):
    if token.pos_ == 'NOUN' and str(token) in time_key_words: 
        return True

    return False

text_steps = extract_recipe_text('test.txt')

parsed_steps = extract_text_from_steps(text_steps)
print(parsed_steps)