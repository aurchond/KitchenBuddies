from pathlib import Path
import spacy 
from spacy import displacy

nlp = spacy.load("en_core_web_sm")

instr_steps = []

recipe_name = 'bibimbap'

with open ('input/recipe2.txt', 'rt') as recipe:
    for instr in recipe:
        instr_steps.append(instr)

count = 0
for i in instr_steps:
    if count > 1:
        break
    doc = nlp(i)
    actions = []
    for token in doc:
        print(token.text, token.lemma_, token.pos_, token.tag_, token.dep_)
        if token.pos_ == 'VERB':
            actions.append(token)
    print(actions)

    #svg = displacy.render(doc, style="dep")
    #f = open(f"./images/{recipe_name}_test_{count}.svg", "x")
    #output_path = Path(f"./images/{recipe_name}_test_{count}.svg")
    #output_path.open("w", encoding="utf-8").write(svg)

    count += 1