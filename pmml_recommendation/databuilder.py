"""Data builder module"""
from random import randint, sample
import pandas as pd
import numpy as np

# type_index = item type
# item_index = item object
item_types = ["Book", "Car", "PC"]

NUMBER_OF_ITEMS_PER_CUSTOMER = 6
NUMBER_OF_ITEMS_PER_TYPE = 10
NUMBER_OF_TYPES = 3
SIZE_OF_DATASET = 1000


def create_item(item_type, item_index):
    """Create an item"""
    return f"{item_type}-{str(item_index)}"


def create_item_names(item_type):
    """Create item names"""
    return [create_item(item_type, x) for x in range(NUMBER_OF_ITEMS_PER_TYPE)]


def create_items():
    """Create items"""
    return [
        create_item(x, y) for x in item_types for y in range(NUMBER_OF_ITEMS_PER_TYPE)
    ]


def buy_customer_items(type_index):
    """Defines the items buyed by a single customer as a 0/1 30-column array (1 = buyed)"""
    items_indexes = sample(
        range(NUMBER_OF_ITEMS_PER_TYPE), NUMBER_OF_ITEMS_PER_CUSTOMER
    )
    items_indexes = [element + (type_index * 10) for element in items_indexes]
    return [
        1 if x in items_indexes else casual_buy()
        for x in range(NUMBER_OF_TYPES * NUMBER_OF_ITEMS_PER_TYPE)
    ]


def casual_buy():
    """Randomly returns 0 or 1"""
    rnd = randint(0, 100)
    if rnd < 10:
        return 1
    return 0


def buy_all_items(type_indexes):
    """Defines the items buyed by all customers"""
    return [buy_customer_items(type_indexes[x]) for x in range(SIZE_OF_DATASET)]


def buy_types(type_indexes):
    """Map the type_index to its name"""
    return [item_types[type_indexes[x]] for x in range(SIZE_OF_DATASET)]


def buy_items(item_indexes, _created_items):
    """Map the type_index/item_index to its name"""
    items = []
    for i in range(SIZE_OF_DATASET):
        sub_items_indexes = item_indexes[i]
        sub_items = [
            _created_items[y]
            for y in range(NUMBER_OF_TYPES * NUMBER_OF_ITEMS_PER_TYPE)
            if sub_items_indexes[y] == 1
        ]
        items.append(sub_items)
    return items


if __name__ == "__main__":
    created_items = create_items()

    buyed_type_indexes = np.random.randint(0, NUMBER_OF_TYPES, SIZE_OF_DATASET)
    buyed_items_indexes = buy_all_items(buyed_type_indexes)

    buyed_types = buy_types(buyed_type_indexes)
    buyed_items = buy_items(buyed_items_indexes, created_items)

    raw_data = {
        "type": buyed_types,
        "buyed_items": buyed_items,
        "type_index": buyed_type_indexes,
        "buyed_items_indexes": buyed_items_indexes,
    }

    df = pd.DataFrame(raw_data)
    print(df.head())

    df.to_csv("input_data.csv", index=False)
