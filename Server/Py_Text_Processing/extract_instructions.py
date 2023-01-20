import spacy 
from spacy import displacy
nlp = spacy.load("en_core_web_sm")

output_steps = []
def extract_recipe_text(filename):
    instr_steps = []
    # Parse recipe text into array
    count = 0
    with open (filename, 'rt') as recipe:
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

def extract_text_from_steps(instr_steps):
    not_verb_dict = {"advcl": '', # might need this one
                     "amod": ''} 
                     # we need conj
                     # xcomp
                     # dobj
                     # nmod
                     

    for step in instr_steps: 
        if 'BREAK' in step:
            print('STEP:', step)
        #     break
        # print('STEP:', step)

        # create a new step object

        # build a custom pipeline to produce better nlp results?
        # clean sentence
        doc = nlp(step)
        for token in doc:
            # print(token.text, token.lemma_, token.pos_, token.tag_, token.dep_,[child for child in token.children])

            # might need to include adv, adj at beginning, propn at beginning of sentence, noun (beginning of sentence and middle of sentence)
            if token.text == ''
            if token.pos_ == 'VERB' and token.dep_ not in not_verb_dict:
                print(token.text, " ", token.tag_, token.dep_,[child for child in token.children])

            if token.pos == 'NOUN' and #in ingredients database 
                #define string to add to ingredients array
                #check if there is a conjunction (Ex. soy sauce)
                    #while the children of the conjunction has a another conjunction, keep looping and adding to ingredient list 
                #check if there is a nummod (if so, add)
                    #check if there is a compound (ex. 'or', 'to')
                        #check if there is conj in the child of the quantifier
                            #if so, add 
                #add string to array of ingredients for step 
            
        #find action algorithm
            #if token.pos_ == 'VERB' and token.dep_ != "ADVCL"
                #append token.pos_ into action array 

                # output_steps.append()
    # nlp = spacy.load("en_core_web_sm")

# init variables

    

i_steps = extract_recipe_text('input/bibimbap.txt')
extract_text_from_steps(i_steps)

# doc = nlp("Apple is looking at buying U.K. startup for $1 billion")
# for ent in doc.ents:
#     print(ent.text, ent.label_)
    #print(token.text, token.lemma_, token.pos_, token.tag_, token.dep_)



# displacy.serve(doc, style="dep")
