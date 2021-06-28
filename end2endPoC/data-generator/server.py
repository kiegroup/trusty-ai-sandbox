"""End-to-end data generator server"""
# pylint: disable=R0201,R0903
import random
import uuid
import string
import json
from flask import Flask, request
from flask_cors import CORS, cross_origin

app = Flask(__name__)
cors = CORS(app)
app.config["CORS_HEADERS"] = "Content-Type"

MAX_DEPTH = 5
MAX_LIST_SIZE = 10
MAX_COMPONENTS_IN_STRUCT = 5


class Number:
    """Number class"""

    def __init__(self):
        self.ttype = "number"

    def get_value(self):
        """Return the value"""
        return random.randint(-10000, 100000)


class Boolean:
    """Boolean class"""

    def __init__(self):
        self.ttype = "boolean"

    def get_value(self):
        """Return the value"""
        return random.choice([True, False])


class String:
    """String class"""

    def __init__(self):
        self.ttype = "string"

    def get_value(self):
        """Return the value"""
        return generate_random_string("string-", 15)


# def CompositeType():
#     def __init__(self, name, typeRef, componcomponentsTypeRefsents):
#         self.name = name
#         self.typeRef = typeRef
#         self.componentsTypeRefs = componentsTypeRefs


def generate_single_header():
    """Generate a single header"""
    return {
        "executionId": str(uuid.uuid1()),
        "executionDate": "",
        "executionSucceeded": random.choice([True, False]),
        "executorName": "",
        "executionType": "DECISION",
    }


def generate_headers(size):
    """Generate all headers"""
    response = []
    for _ in range(size):
        response.append(generate_single_header())
    return response


EVALUATION_STATES = ["EVALUATING", "SUCCEEDED", "SKIPPED", "FAILED"]
#     "name": "Is Enought?",
#     "typeRef": "number",
#     "value": 100,
#     "components": null

BUILT_IN_TYPES = [Number(), String()]  # , Boolean()]


def generate_random_string(prefix, length):
    """Generate a random string"""
    return prefix + "".join(
        [
            random.choice(string.ascii_lowercase)
            for i in range(random.randint(4, length))
        ]
    )


def generate_value(hook):
    """Generate a value"""
    is_collection = random.choice([True, False])
    if is_collection:
        return [hook() for _ in range(1, random.randint(1, MAX_LIST_SIZE))]
    return hook()


def generate_outcome_result(prefix, max_depth):
    """Generator outcome result"""
    is_composed = random.choice([True, False]) and max_depth != 0  # stop at max depth
    current_prefix = prefix + "Struct " if is_composed else prefix + "Simple "
    if not is_composed:
        obj = random.choice(BUILT_IN_TYPES)
        ttype = obj.ttype
        tocall = obj.get_value
    else:
        ttype = generate_random_string("type ", 10)
        tocall = lambda: generate_outcome_result(prefix, max_depth - 1)
    return {
        "name": generate_random_string(current_prefix, 10),
        "typeRef": ttype,
        "value": None if is_composed else generate_value(tocall),
        "components": None
        if not is_composed
        else sorted(
            [tocall() for i in range(random.randint(1, MAX_COMPONENTS_IN_STRUCT))],
            key=lambda x: x["value"] is None,
        ),
    }


def generate_outcomes():
    """Generate outcomes"""
    return {
        "outcomeId": str(uuid.uuid1()),
        "outcomeName": generate_random_string("name ", 10),
        "evaluationStatus": random.choice(EVALUATION_STATES),
        "messages": [
            generate_random_string("message ", 10) for i in range(random.randint(0, 4))
        ],
        "hasErrors": random.choice([True, False]),
        "outcomeResult": generate_outcome_result("outcome", MAX_DEPTH),
    }


@app.route("/executions")
@cross_origin()
def get_executions():
    """Get executions"""
    limit = int(request.args.get("limit"))
    offset = int(request.args.get("offset"))
    headers = generate_headers(random.randint(1, limit))
    response = {
        "limit": limit,
        "offset": offset,
        "total": len(headers),
        "headers": headers,
    }
    return json.dumps(response), 200, {"content-type": "application/json"}


@app.route("/executions/decisions/<execution_id>")
@cross_origin()
def get_execution_by_id(_):
    """Get execution by id"""
    response = generate_single_header()
    return json.dumps(response), 200, {"content-type": "application/json"}


@app.route("/executions/decisions/<execution_id>/outcomes")
@cross_origin()
def get_execution_outcome(_):
    """Return outcomes"""
    response = {
        "header": generate_single_header(),
        "outcomes": [generate_outcomes() for i in range(random.randint(1, 20))],
    }
    return json.dumps(response), 200, {"content-type": "application/json"}


def generate_components_for_composite_type(choices, exclude):
    """Generate components for composite type"""
    choices = filter(lambda x: x not in exclude, choices)
    return random.choices(choices, k=random.randint(1, 10))


@app.route("/executions/decisions/<execution_id>/structuredInputs")
@cross_origin()
def get_execution_structured_inputs(_):
    """Get structured inputs"""
    # structs = ["structname-" + generateRandomString(10) for i in range(1,10)]
    # structsTypeRef = map(lambda x: "typestruct-" + x, structs)
    # structsTypes = map(lambda x: CompositeType(structs[x], structsTypeRef[x],
    # generate_components_for_composite_type(structs  + BUILT_IN_TYPES, structs[x]),
    # range(1, len(structs))))
    response = {
        "inputs": [generate_outcome_result("input", MAX_DEPTH) for i in range(1, 20)]
    }
    return json.dumps(response), 200, {"content-type": "application/json"}


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=1337)
