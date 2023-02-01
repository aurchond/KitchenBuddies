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
            l_dict = {}
            parsed_labels = label_line.split(',')
            for word in parsed_labels:
                key = word.lower().strip()
                l_dict[key] = ''
            labels.append(l_dict)

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

    if len(labels) != len(verbs):
        print('Either the labels or raw data are missing. Line length do not equal')
        return 1

    line_accuracy = []
    for i in range(len(verbs)):
        if '/n' in labels[i] and len(labels) == 1:
            continue
        correct = 0
        total = len(labels[i])
        for j in range(len(verbs[i])):
            if len(labels[i]) == 0 or len(verbs[i]) == 0:
                print("Line ", i+2, ": ",verbs[i], " ", labels[i])
                # account for number of errors
                break
            elif len(verbs[i]) > len(labels[i]):
                print("Extra verbs found - Line ", i+2, ": ",verbs[i], " ", labels[i])
        
            if 'BREAK' in verbs[i][j]:
                break
            
            # print(verbs[i][j], " compared to ", labels[i])
            if verbs[i][j].lower() in labels[i]:
                correct += 1

        # print('Correct: ', correct, ' Total: ', total)
        acc = correct/total
        if acc < 1:
            print("Line ", i+2, ' Comparing ', verbs[i], ' with ', labels[i])
        line_accuracy.append(acc)
    print(line_accuracy)
    print('Prediction Accuracy', mean(line_accuracy))


test_verb_goodness()

# fn test ingredient goodness
# fn test kitchen supplies goodness
# fn test time goodness

