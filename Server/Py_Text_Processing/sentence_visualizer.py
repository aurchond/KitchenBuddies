from pathlib import Path
import spacy 
from spacy import displacy

from utilities import extract_instructions

# ----- USER: Set file name from input folder -----
input_file = "schnitzel.txt"
image_file_name = "schnitzel"
# -------------------------------------------------

def generate_sentence_visualizer(filename, image_file):
    instr_steps = extract_instructions(filename)

    nlp = spacy.load("en_core_web_sm")

    count = 0
    for i in instr_steps:
        # if count > 1:
        #     break
        doc = nlp(i)

        for token in doc:
            print(token.text, token.lemma_, token.pos_, token.dep_, [child for child in token.children])
                    
        svg = displacy.render(doc, style="dep")
        f = open(f"./images/{image_file}_{count}.svg", "x")
        output_path = Path(f"./images/{image_file}_{count}.svg")
        output_path.open("w", encoding="utf-8").write(svg)
        count += 1

generate_sentence_visualizer(input_file, image_file_name)