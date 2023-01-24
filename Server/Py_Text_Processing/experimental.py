from pathlib import Path
import spacy 
from spacy import displacy

nlp = spacy.load("en_core_web_sm")

instr_steps = []

recipe_name = 'bibimbap'

with open ('input/test.txt', 'rt') as recipe:
    for instr in recipe:
        instr_steps.append(instr)

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
    # print(step_words.index('Put'))
    for token in doc:
        children =  [child for child in token.children]
        print(token.text, token.lemma_, token.pos_, token.dep_, children)
        if token.pos_ == 'NOUN': 
            ingredient = ''
            
            print(step_words[step_words.index(token)+1].children)

           #for child in children: 
                #print(step_words.index(child))
                 
        
        #if token.pos_ == 'VERB':
           # actions.append(token)
    #print(actions)

    #svg = displacy.render(doc, style="dep")
    #f = open(f"./images/{recipe_name}_test_{count}.svg", "x")
    #output_path = Path(f"./images/{recipe_name}_test_{count}.svg")
    #output_path.open("w", encoding="utf-8").write(svg)
    count += 1