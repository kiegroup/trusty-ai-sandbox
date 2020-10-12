from random import random
import pandas as pd
import numpy as np

def disp_risk(holder_index, amount, noise=0.1):
    random_threshold = 1 - noise
    if holder_index == 3:
        if amount >= 200:
            return 4 if random()<random_threshold else 2
        elif amount >= 100:
            return 2
        else:
            return 1
    elif holder_index == 2:
        if amount >= 200:
            return 4
        elif amount >= 150:
            return 3 if random()<random_threshold else 2
        elif amount >= 100:
            return 2
        else:
            return 1
    elif holder_index == 1:
        if amount >= 200:
            return 5
        elif amount >= 150:
            return 3 if random()<random_threshold else 2
        elif amount >= 100:
            return 2
        else:
            return 1
    else:
        if amount >= 200:
            return 5
        elif amount >= 150:
            return 4 if random()<random_threshold else 3
        elif amount >= 100:
            return 3
        else:
            return 2

if __name__ == '__main__':
    size_of_dataset=1000
    holders_index = np.random.randint(0, 4, size_of_dataset)
    amounts = list(map(lambda x: round(max(10, x), 2), np.random.normal(125, 50, size_of_dataset)))
    dispute_risk = list(map(lambda x, y: disp_risk(x, y), holders_index, amounts))
    
    raw_data = {'holder_index': holders_index, 'amount': amounts, 'dispute_risk': dispute_risk}
    
    df = pd.DataFrame(raw_data)
    
    print(df.head())
    df.to_csv('input_data.csv', index=False)