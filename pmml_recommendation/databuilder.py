from random import randint, sample
import pandas as pd
import numpy as np

# type_index = item type
# item_index = item object
item_types = ["Book", "Car", "PC"]

number_of_items_per_customer = 6
number_of_items_per_type = 10
number_of_types = 3
size_of_dataset = 1000


def create_item(item_type, item_index):
    return f"{item_type}-{str(item_index)}"


def create_item_names(item_type):
    return [create_item(item_type, x) for x in range(number_of_items_per_type)]


def create_items():
    return [create_item(x, y)
            for x in item_types for y in range(number_of_items_per_type)]


# Defines the items buyed by a single customer as a 0/1 30-column array (1
# = buyed)
def buy_customer_items(type_index):
    items_indexes = sample(
        range(number_of_items_per_type),
        number_of_items_per_customer)
    items_indexes = [element + (type_index * 10) for element in items_indexes]
    return [1 if x in items_indexes else casual_buy()
            for x in range(number_of_types * number_of_items_per_type)]


# randomly returns 0 or 1
def casual_buy():
    rnd = randint(0, 100)
    if rnd < 10:
        return 1
    else:
        return 0


# Defines the items buyed by all customers
def buy_all_items(type_indexes):
    return [buy_customer_items(type_indexes[x])
            for x in range(size_of_dataset)]


# Map the type_index to its name
def buy_types(type_indexes):
    return [item_types[type_indexes[x]] for x in range(size_of_dataset)]


# Map the type_index/item_index to its name
def buy_items(item_indexes, created_items):
    items = []
    for x in range(size_of_dataset):
        sub_items_indexes = item_indexes[x]
        sub_items = [
            created_items[y] for y in range(
                number_of_types *
                number_of_items_per_type) if sub_items_indexes[y] == 1]
        items.append(sub_items)
    return items


if __name__ == '__main__':
    created_items = create_items()

    buyed_type_indexes = np.random.randint(0, number_of_types, size_of_dataset)
    buyed_items_indexes = buy_all_items(buyed_type_indexes)

    buyed_types = buy_types(buyed_type_indexes)
    buyed_items = buy_items(
        buyed_items_indexes,
        created_items)

    raw_data = {
        'type': buyed_types,
        'buyed_items': buyed_items,
        'type_index': buyed_type_indexes,
        'buyed_items_indexes': buyed_items_indexes}

    df = pd.DataFrame(raw_data)
    print(df.head())

    df.to_csv('input_data.csv', index=False)
