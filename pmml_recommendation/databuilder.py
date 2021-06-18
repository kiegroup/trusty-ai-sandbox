from random import random, randrange, getrandbits, randint, sample 
import pandas as pd
import numpy as np

# type_index = item type
# item_index = item object
item_types = ["Book", "Car", "PC"]

number_of_items_per_customer = 6
number_of_items_per_type = 10
number_of_types = 3

def create_item(item_type, item_index):
    return item_type + "-" + str(item_index)

def create_item_names(item_type):
    items = []
    for x in range(number_of_items_per_type):
        items.append(create_item(item_type, x))
    return items    
            
def create_items():
    items = []
    for x in item_types:
        for y in range(number_of_items_per_type):
             items.append(create_item(x, y))
    return items          

# Defines the items buyed by a single customer as a 0/1 30-column array (1 = buyed)
def buy_customer_items(type_index):
    items = []
    items_indexes = sample(range(number_of_items_per_type), number_of_items_per_customer)
    items_indexes = [element + (type_index * 10) for element in items_indexes]
    for x in range(number_of_types * number_of_items_per_type):
        if x in items_indexes:
            items.append(1)
        else:
            items.append( casual_buy())
    return items
    
# randomly returns 0 or 1    
def casual_buy():
    rnd = randint(0, 100)
    if rnd < 10:
        return 1
    else:
        return 0
    
    
    
# Defines the items buyed by all customers
def buy_all_items(type_indexes, size_of_dataset):
    items = []
    for x in range(size_of_dataset):
        items.append(buy_customer_items(type_indexes[x]))
    return items

# Map the type_index to its name
def buy_types(type_indexes, item_types, size_of_dataset):
    items = []
    for x in range(size_of_dataset):
        items.append(item_types[type_indexes[x]])
    return items 

# Map the type_index/item_index to its name
def buy_items(type_indexes, item_indexes, created_items, size_of_dataset):
    items = []
    for x in range(size_of_dataset):
        type_index = type_indexes[x]
        sub_items = []
        sub_items_indexes = item_indexes[x]
        for y in range(number_of_types * number_of_items_per_type):
            if sub_items_indexes[y] == 1:
                sub_items.append(created_items[y])
        items.append(sub_items)
    return items


if __name__ == '__main__':
    created_items = create_items()
   
    size_of_dataset=1000
   
    buyed_type_indexes = np.random.randint(0, number_of_types, size_of_dataset)
    buyed_items_indexes = buy_all_items(buyed_type_indexes, size_of_dataset)
  
    buyed_types = buy_types(buyed_type_indexes, item_types, size_of_dataset)
    buyed_items = buy_items(buyed_type_indexes, buyed_items_indexes, created_items, size_of_dataset)
    
    raw_data = {'type': buyed_types, 'buyed_items': buyed_items,  'type_index': buyed_type_indexes, 'buyed_items_indexes': buyed_items_indexes}
    
    df = pd.DataFrame(raw_data)
    print(df.head())
    
    df.to_csv('input_data.csv', index=False)
