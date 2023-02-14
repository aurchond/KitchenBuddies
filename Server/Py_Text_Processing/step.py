
class Step:
    def __init__(self, instructions):
        self.instructions = instructions
        self.ingredients = []
        self.ingredientsQuantity = []
        self.resourcesRequired = []
        self.prepStep = False
        self.verbs = []
        self.stepTime = ''
        self.userTime = 0
        self.holdingResource = ''
        self.holdingID = -1

    def extract_verb_from_step(self, token):
        self.verbs.append(token.text.lower())

        # compare to verb database to check if prep step

    def extract_ingredient_from_step(self, token, step_words, children, recipe_ingredients):
        ingredient = ""
        for check_for_mods in children:      #consider ingredients modified by amod 
            if check_for_mods.dep_ == 'amod' or check_for_mods.dep_ =='nmod':
                ingredient += str(check_for_mods) 
                ingredient += " "
                if str(check_for_mods) in self.ingredients:
                    self.ingredientsQuantity.pop(self.ingredients.index(str(check_for_mods))) 
                    self.ingredients.pop(self.ingredients.index(str(check_for_mods)))

        ingredient += str(token)

        #if step_words.index(token) < (len(step_words)-1): children_check = [child for child in step_words[step_words.index(token)+1].children]
        #else: children_check = []; #end of sentence so nothing is after 

        skip_words = 0
        token_check = token
        while token_check.dep_ == 'compound': # and token_check in children_check: #checking to see if ingredients have multiple words
            ingredient += " "
            ingredient += " "
            ingredient += str(token_check.head)
            token_check = token_check.head
            
           # ingredient += str(step_words[step_words.index(token_check)+1])
           # token_check = step_words[step_words.index(token_check)+1]
           # children_check = [child for child in step_words[step_words.index(token_check)+1].children]
            skip_words += 1
        
        is_in_ingredients = False #need to check if the potential ingredient is in the list of ingredients from the json
        for check_ingredient in recipe_ingredients:
            if ingredient in check_ingredient: is_in_ingredients = True
        
        #if is_in_ingredients: 
        self.ingredients.append(ingredient) #add ingredient to array of ingredients for the step

        quantity = "given"
        for quantity_test in token_check.children:
            if quantity_test.dep_ == 'nummod' and quantity_test.pos_ == 'NUM':
                quantity = str(quantity_test)
                for misc in quantity_test.children: 
                    quantity += " "
                    quantity += str(misc)  #this is to account for quantities like "2 or 3" or "5-7"
        self.ingredientsQuantity.append(quantity)

        return skip_words

    def extract_time_from_step(self, token, children):
        time_for_step = ''
        #calculating time dependencies
        for time_check in children:
            if time_check.pos_ == 'NUM': 
                grandchildren = [grandchild for grandchild in time_check.children]
                for grandchild in grandchildren:
                    if (grandchild.pos_ == 'NUM' or str(grandchild) == 'to') and grandchild.dep_ == 'quantmod':
                        time_for_step += str(grandchild)
                
                time_for_step += str(time_check)
                time_for_step += str(token)
        self.stepTime = time_for_step
    