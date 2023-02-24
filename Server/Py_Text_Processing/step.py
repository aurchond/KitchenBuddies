from mysql_db import verify_ingredients_supplies, debug_verify_ingr_supplies
import math

# key = verb, value = time it takes to finish per ingredient in minutes
time_per_ingr_dict = {'chop': 1,
                      'simmer': 2,
                      'stir': 1,
                      'mix': 2,
                      'blend': 2,
                      'whisk': 2,
                      'beat': 2,
                      'fold': 3,
                      'dice': 2,
                      'mince': 3,
                      'grate': 4,
                      'slice': 3,
                      'peel': 4,
                      'mash': 2,
                      'puree': 3,
                      'crush': 3,
                      'season': 1,
                      'sprinkle': 1,
                      'coat': 3,
                      'dip': 1,
                      'baste': 5,
                      'glaze': 3,
                      'brush': 3,
                      'fry': 2,
                      'saute': 3,
                      'sear': 2,
                      'squeeze': 3,
                      'add': 1,
                      'combine': 1,
                      'arrange': 1,
                      }

time_total_dict = {'preheat': 1,
                   'spray': 2,
                   'boil': 5,
                   'grill': 10,
                   'bake': 20,
                   'roast': 30,
                   'broil': 10,
                   'poach': 10,
                   'steam': 6,
                   'blanch': 10,
                   'drain': 3,
                   'rinse': 2,
                   'marinate': 60,
                   'garnish': 1,
                   'serve': 1,
                   'set': 1,
                   'pour': 2,
                   'snip': 2}

prep_dict = {'preheat':'',
             'spray':'',
             'chop':'',
             'dice':'',
             'mince':'',
             'grate':'',
             'slice':'',
             'mix':'',
             'peel':'',
             'combine':'',
             'whisk':'',
             'beat':'',
             'brush':'',
             'cut': '', 
             'prepare': ''}

# Verbs that are CLEARLY non for preparation
non_prep_dict = {'simmer':'',
                 'mash':'',
                 'fry':'',
                 'cook':'',
                 'saute':'',
                 'boil':'',
                 'grill':'',
                 'bake':'',
                 'roast':'',
                 'broil':'',
                 'poach':'',
                 'blanch':'',
                 'toast':''}

class Step:
    def __init__(self, instructions):
        self.instructions = instructions
        self.ingredients = []
        self.ingredientsQuantity = []
        self.resourcesRequired = []
        self.prepStep = False
        self.verbs = []
        self.stepTime = -1
        self.userTime = 0
        self.holdingResource = ''
        self.holdingID = -1

    def extract_verb_from_step(self, token):
        self.verbs.append(token.text.lower())

        # compare to verb database to check if prep step

    def extract_full_noun_from_step(self, token, children):
        validity = True
        ingredient = ""
        for check_for_mods in children:      #consider ingredients modified by amod 
            if check_for_mods.dep_ == 'amod' or check_for_mods.dep_ =='nmod':
                ingredient += str(check_for_mods) 
                ingredient += " "
                if str(check_for_mods) in self.ingredients:
                    self.ingredientsQuantity.pop(self.ingredients.index(str(check_for_mods))) 
                    self.ingredients.pop(self.ingredients.index(str(check_for_mods)))

        ingredient += str(token)

        skip_words = 0
        token_check = token
        while token_check.dep_ == 'compound': # and token_check in children_check: #checking to see if ingredients have multiple words
            ingredient += " "
            ingr_dummy, supply_dummy = debug_verify_ingr_supplies([str(token_check.head)]) #need to consider cases like "donut pan"
            if len(supply_dummy) == 0: 
                ingredient += str(token_check.head)
                token_check = token_check.head
                skip_words += 1
            else: 
                ingredient = None
                validity = False 
                break
        
        quantity = -1
        edge_case = False
        for quantity_test in token_check.children:
            if quantity_test.dep_ == 'nummod' and quantity_test.pos_ == 'NUM':
                if not str(quantity_test).isnumeric():
                    break
                # NOTE: It can be a string (i.e. 'one')
                quantity = int(str(quantity_test))
                for misc in quantity_test.children: 
                    print(f" {str(misc)}")
                    edge_case = True
                    # quantity += " "
                    # quantity += str(misc)  #this is to account for quantities like "2 or 3" or "5-7"
        # TODO: Convert this to an int in minutes
        if edge_case:
            print("Ingredients Quantity Edge Case Detected!!")

        # self.ingredientsQuantity.append(quantity)

        return skip_words, ingredient, quantity, validity

    def extract_supply_from_step(self, token):
        supply += str(token)

        skip_words = 0
        token_check = token
        while token_check.dep_ == 'compound': # and token_check in children_check: #checking to see if ingredients have multiple words
            supply += " "
            supply += str(token_check.head)
            token_check = token_check.head
            skip_words += 1
        
        return skip_words, supply
    
    def define_prep_step(self):
        '''
        Determine if the task is used for cooking preparation (i.e. cutting veggies)
        Ensure that the verbs have been extracted before using this
        '''
        is_prep = False
        for verb in self.verbs:
            if verb in non_prep_dict:
                self.prepStep = False
                return
            if verb in prep_dict:
                is_prep = True
        # if one verb is a prep_step and no non_prep
        if is_prep:
            self.prepStep = True

    def extract_time_from_step(self, token, children):
        '''
        Take the max time from step 
        Place in stepTime as time in minutes
        '''

        time_for_step = -1
        #calculating time dependencies
        for time_check in children:
            if time_check.pos_ == 'NUM': 
                grandchildren = [grandchild for grandchild in time_check.children]
                gc_time = -1
                for grandchild in grandchildren:
                    if (grandchild.pos_ == 'NUM' or str(grandchild) == 'to') and grandchild.dep_ == 'quantmod':
                        # print(f'Grandchild {grandchild}')
                        gc_time = int(str(grandchild))
                        # time_for_step += str(grandchild)
                # print(f'time_check {time_check}')
                # print(f'token {token}')
                if not str(time_check).isnumeric():
                    break
                time = int(str(time_check))
                if time > gc_time:
                    time_for_step = time
                else:
                    time_for_step = gc_time

                if str(token) in 'seconds':
                    time /= 60
                elif str(token) in 'hours':
                    time *= 60
                
                time_for_step = math.ceil(time)
                # time_for_step += str(time_check)
                # time_for_step += str(token)
        if self.stepTime != -1:
            # multiple times defined within the same step
            # print(f'Time is {time_for_step}')
            self.stepTime += time_for_step
        else: 
            self.stepTime = time_for_step

    def approximate_step_time(self):
        # setup local variables
        s_time = 0
        len_ingredients = len(self.ingredients)
        prep_step_time = 2
        non_prep_step_time = 1

        is_total = False

        # add time based on number of verbs (more verbs = longer time)
        for verb in self.verbs:
            # TODO: Refine step time approach
            if verb in time_total_dict:
                is_total = True
                s_time += time_total_dict[verb]
                continue

            if verb in time_per_ingr_dict:
                s_time += time_per_ingr_dict[verb]*len_ingredients
            else:
                if self.prepStep:
                    s_time += prep_step_time*len_ingredients
                else:
                    s_time += non_prep_step_time*len_ingredients
        self.stepTime = s_time
        if is_total:
            self.approx_user_time()
        else:
            self.userTime = s_time
        
        if self.stepTime == -1:
            self.stepTime = 2

    def approx_user_time(self):
        # only used if step_time is explicitly defined within the instruction
        if 0 < self.stepTime <= 5:
            self.userTime = self.stepTime
        elif 5 < self.stepTime <= 10:
            self.userTime = 5
        elif 10 < self.stepTime <= 30:
            self.userTime = 2
        elif 30 < self.stepTime:
            self.userTime = 2
        
        # TODO: Add edge cases here

    def verify_key_words(self, key_words, verbose_ingr, verbose_supply, recipe_ingr):
        '''
        Verify which nouns in instructions are either ingredients or supplies
        '''
        ingr_out = []
        supplies_out = []
        db_ingr = []

        # iterate through key words and check ingredients
        # print("Key words: ", key_words)
        for key in key_words:
            for ingr in recipe_ingr:
                if key in ingr: 
                    ingr_out.append(key)
                    # print(key)
                    key_words.pop(key_words.index(key))
                    break
            
        # call database with:
        # - ingredients not in recipe ingredients
        # - supplies
        # - garbage that will be sifted
        if len(key_words) != 0:
            db_ingr, supplies_out = debug_verify_ingr_supplies(key_words)
            # db_ingr, supplies_out = verify_ingredients_supplies(key_words)
            ingr_out = ingr_out + db_ingr 

        for ingr in ingr_out:
            if ingr.lower() not in verbose_ingr:
                print(f"{ingr} was considered as an ingr??")
                continue
            values = verbose_ingr[ingr.lower()]
            self.ingredients.append(values[0])
            self.ingredientsQuantity.append(values[1])
        
        for supply in supplies_out:
            if supply.lower() not in verbose_ingr:
                print(f"{supply} was considered as supply??")
                continue
            self.resourcesRequired.append(verbose_ingr[supply.lower()][0])
        
        return ingr_out
        
        # print(self.ingredients)
        # print(self.resourcesRequired)
            
    def extract_holdingres_from_step(self, token, step_words, hold_res_dic, hold_res_count):
        head_word = token.head 
        #print(token, head_word.lemma_)
        if token.dep_ == "pobj" and (str(head_word.lemma_) == "in" or str(head_word.lemma_) == "on" or str(head_word.lemma_) == "to" or str(head_word.lemma_) == "into"  ) :
            # assume new holding resource
            #print('I FOUND A RESOURCE:' + str(token)) 
            self.holdingResource = str(token)
            hold_res_dic[self.holdingResource] = hold_res_count
            self.holdingID = hold_res_count
            print(self.holdingResource, hold_res_count)
            hold_res_dic[self.holdingResource] = hold_res_count

            hold_res_count += 1          
            return True, hold_res_dic, hold_res_count
        else:
            return False, hold_res_dic, hold_res_count
            #need to access resources (supplies.txt) from data
    
    def holdingres_edge_case(self, hold_res_count, hold_res_dic, step_words, steps_out, resource_dataset, ingr_base_words, all_ingr_base_words):
        '''
        Any edge case we encounter for holding resources is added here. 
        This function is used if the holding resource isn't already defined.

        List of edge cases
        - Holding resource is in oven
        - make instructions with 'cut' have a holding resource of cutting board
        - Add hold_res_dic
        - First step of recipe
        - If step isn't the first step in the line (BREAK wasn't before this step), use the holding resource from previous step
        - If first step after break, look at intersection of ingredients
        - change so that the algorithm does not break when a holding resource is found
        '''
        #print(step_words)
            
        check_oven_keywords = ['Bake', 'bake', 'broil', 'Broil', 'Roast', 'roast']
        #need to check for the oven being a holding resource (look for words like 'bake' or 'broil' or 'oven')
        string_words = [str(i) for i in step_words]
        if len(set(check_oven_keywords).intersection(set(string_words))) != 0:
            # Set oven to holding resource
            # NOTE: Change this to the resource holding the food
            self.holdingResource = 'oven'
            self.holdingID = hold_res_count
            hold_res_dic[self.holdingResource] = hold_res_count
            hold_res_count += 1      

        elif len(steps_out) == 0: #if it is the first step
            for word in step_words: 
                if str(word) in resource_dataset and str(word) != "cup":
                    self.holdingResource = str(word)
                    self.holdingID = hold_res_count
                    hold_res_count += 1      
            if self.holdingResource == '': self.holdingResource = 'N/A'

        elif 'BREAK' not in steps_out[-1].instructions:
            self.holdingResource = steps_out[-1].holdingResource
            self.holdingID = steps_out[-1].holdingID

        elif 'BREAK' in steps_out[-1].instructions:   #if the instruction string does contain BREAK
            # NOTE: Assumes that ingredients are labelled the same across steps (i.e. milk, dough -> batter)
            set1 = set(ingr_base_words)
            for test_index in range(len(all_ingr_base_words)):
                max_count = 0
                set2 = set(all_ingr_base_words[test_index]) 
                #print(set1)
                #print(set2)
                common_ingredients = set1.intersection(set2)
                if len(common_ingredients) > max_count: 
                    self.holdingResource = steps_out[test_index].holdingResource
                    self.holdingID = steps_out[test_index].holdingID
                    

                    max_count = len(common_ingredients)
            if len(common_ingredients) == 0:
                for resource in self.resourcesRequired: 
                    if resource != "cup":
                        self.holdingResource = resource
                        self.holdingID = hold_res_count
                        hold_res_count += 1 
                if self.holdingResource == '':
                    self.holdingResource = steps_out[-2].holdingResource #just a holder for now, need to consider this edge case 
                    self.holdingID = steps_out[-2].holdingID

        return hold_res_count, hold_res_dic

        
