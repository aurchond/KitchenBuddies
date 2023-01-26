from pathlib import Path
import spacy 
from spacy import displacy

nlp = spacy.load("en_core_web_sm")

instr_steps = []

recipe_name = 'bibimbap'

with open ('input/test.txt', 'rt') as recipe:
    for instr in recipe:
        instr_steps.append(instr)

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
for ingr in recipe_ingredients:
    print(ingr)
count = 0
for i in instr_steps:
    if count > 1:
        break
    doc = nlp(i)
    actions = []
    step_words = []

    for token in doc: 
        step_words.append(token)  #adding all words of the step into an array

    print(step_words)

    ingredients_in_step = []
    ingredient_quan = [] 
    
    skip_or_not = 0
    for token in doc:
        if skip_or_not == 0 : 
            children =  [child for child in token.children]
            print(token.text, token.pos_, token.dep_, children)
            if token.pos_ == 'NOUN': 

                ingredient = str(token)

                if step_words.index(token) < (len(step_words)-1): children_check = [child for child in step_words[step_words.index(token)+1].children]
                else: children_check = []; #end of sentence so nothing is after 

                token_check = token
                while token_check.dep_ == 'compound' and token_check in children_check: #checking to see if ingredients have multiple words
                    ingredient += " "
                    ingredient += str(step_words[step_words.index(token_check)+1])
                    token_check = step_words[step_words.index(token_check)+1]
                    children_check = [child for child in step_words[step_words.index(token_check)+1].children]
                    skip_or_not += 1
                    #print(children_check)
                print(ingredient)
                print(str(token_check))
                
                is_in_ingredients = False
                for check_ingredient in recipe_ingredients:
                    if ingredient in check_ingredient: is_in_ingredients = True
                
                #if is_in_ingredients: 
                ingredients_in_step.append(ingredient)
                quantity = "given"
                for quantity_test in token_check.children:
                    if quantity_test.dep_ == 'nummod' and quantity_test.pos_ == 'NUM':
                        quantity = str(quantity_test)
                        for misc in quantity_test.children: 
                            quantity += " "
                            quantity += str(misc)  #this is to account for quantities like "2 or 3" or "5-7"
                ingredient_quan.append(quantity)

                    

        else: 
            skip_or_not -= 1
    print(ingredients_in_step)
    print(ingredient_quan)

    #svg = displacy.render(doc, style="dep")
    #f = open(f"./images/{recipe_name}_test_{count}.svg", "x")
    #output_path = Path(f"./images/{recipe_name}_test_{count}.svg")
    #output_path.open("w", encoding="utf-8").write(svg)
    count += 1