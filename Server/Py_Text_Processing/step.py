
class Step:
    def __init__(self, instructions):
        self.instructions = instructions
        self.ingredients = []
        self.ingredientsQuantity = []
        self.resourcesRequired = []
        self.prepStep = False
        self.stepTime = 0
        self.userTime = 0
        self.holdingResource = ''
        self.holdingID = -1

    
        
        