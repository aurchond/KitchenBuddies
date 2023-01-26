# Script to check goodness of model
import os.path
from utilities import extract_recipe_text
from extract_instructions import extract_verb_from_steps
from statistics import mean
import pandas as pd

recipe_instructions = "schnitzel.txt"
'''
Options
- verb_data.csv
- ingredients.csv
- supplies.csv
- time.csv
'''
data_filename = "verb_data.csv"

def insert_training_data(recipe_file, data_file):
    '''
    It takes the recipe instructions and 
    places each sentence it into a csv for training.
    Labelling must be done MANUALLY
    '''
    steps = extract_recipe_text(recipe_file)

    # create dataframe
    steps_df=pd.DataFrame({'steps':steps})
    print(steps_df)

    # get data from file
    # check if file exists
    data_df = pd.DataFrame(columns=['steps'])
    if os.path.isfile('./data/'+data_file):
        data_df = pd.read_csv('./data/'+data_file)

    steps_df = pd.concat([data_df, steps_df])

    steps_df.to_csv('./data/'+data_file, index=False)
    # 

# insert_training_data('schnitzel.txt', 'verb_data.csv')

def extract_labels(label_file):
    labels = []
    with open ("./data/"+label_file, 'rt') as recipe:
        for label_line in recipe:
            label = label_line.split(',')
            labels.append(label)

    return labels



# fn test verb goodness
def test_verb_goodness():
    verb_df = pd.read_csv('./data/verb_data.csv')
    print(verb_df.head)
    instructions = verb_df['steps']

    # run test
    verbs = extract_verb_from_steps(instructions)

    labels = extract_labels('verb_labels.txt')
    print(labels)

    for i in range(len(verbs)):
        line_accuracy = []
        # print("Line ", i+2, ": ",verbs[i], " ", labels[i])

        correct = 0
        total = 0
        includes_verb = True
        for j in range(len(labels[i])):
            if labels[i][j] == '/n' and len(labels[i]) == 1:
                includes_verb = False
                break
            total += 1
            print(len(verbs[i]), " ", len(labels[1]))
            if j > len(labels[i]) or j > len(verbs[i]) or \
                len(labels[i]) == 0 or len(verbs[i]) == 0:
                print("Line ", i+2, ": ",verbs[i], " ", labels[i])
                # account for number of errors
                break

            if verbs[i][j] in labels[i][j]:
                correct += 1
        
        if includes_verb:
            line_accuracy.append(correct/total)
    
    print('Prediction Accuracy', mean(line_accuracy))

        #     print()
        #     if verbs[i][j] in labels[i][j]:
        #         continue
        #     else:


test_verb_goodness()

# fn test ingredient goodness
# fn test kitchen supplies goodness
# fn test time goodness

