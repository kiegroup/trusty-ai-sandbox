import requests
import os
import json
import random

ENDPOINT = 'http://localhost:8080/dmn-loan-eligibility'

def generate_random_request():
    data = {
            "Client" : {
                "age" : int(random.normalvariate(40, 10)),
                "salary" : int(random.normalvariate(2000, 1000)),
                "existing payments": random.randint(50, 200)
                },
            "Loan" : {
                "duration" : random.randint(10, 40),
                "installment": random.randint(100, 200)
            },
            "God" : random.choice(["Yes", "No"])
            }
    return data

def make_request():
    data = generate_random_request()
    print(data)
    r = requests.post(url = ENDPOINT, data = json.dumps(data), headers = {"Content-Type":"application/json"})
    print(r.text)

if __name__ == "__main__":
    for x in range(10):
        print("sending req number: " + str(x))
        make_request()
