from flask import Flask, request
import random
import uuid 
import string
import json
from flask_cors import CORS, cross_origin

app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

MAX_DEPTH = 5
MAX_LIST_SIZE = 10
MAX_COMPONENTS_IN_STRUCT = 5

class Number():
    def __init__(self):
        self.ttype = "number"
    
    def get_value(self):
        return random.randint(-10000, 100000)

class Boolean():
    def __init__(self):
        self.ttype = "boolean"
    
    def get_value(self):
        return random.choice([True, False])

class String():
    def __init__(self):
        self.ttype = "string"
    
    def get_value(self):
        return generateRandomString("string-", 15)

# def CompositeType():
#     def __init__(self, name, typeRef, componcomponentsTypeRefsents):
#         self.name = name
#         self.typeRef = typeRef
#         self.componentsTypeRefs = componentsTypeRefs
    
def generateSingleHeader():
    return {"executionId" : str(uuid.uuid1()), 
                        "executionDate" : "", 
                        "executionSucceeded" : random.choice([True, False]), 
                        "executorName" : "", 
                        "executionType" : "DECISION"}

def generateHeaders(size):
    response = []
    for i in range(size):
        response.append(generateSingleHeader())
    return response

EVALUATION_STATES = ["EVALUATING", "SUCCEEDED", "SKIPPED", "FAILED"]
    #     "name": "Is Enought?",
    #     "typeRef": "number",
    #     "value": 100,
    #     "components": null

BUILT_IN_TYPES = [Number(), String()] #, Boolean()]

def generateRandomString(prefix, length):
    return prefix + ''.join([random.choice(string.ascii_lowercase) for i in range(random.randint(4, length))])


def generate_value(hook):
    isCollection = random.choice([True, False])
    if isCollection:
        return [hook() for i in range(1,random.randint(1,MAX_LIST_SIZE))]
    return hook()

def generateOutcomeResult(prefix, max_depth):
    isComposed = random.choice([True, False]) or max_depth == 0 # stop at max depth
    currentPrefix = prefix + "Struct " if isComposed else prefix + "Simple "
    if not isComposed:
        obj = random.choice(BUILT_IN_TYPES)
        ttype = obj.ttype
        tocall = obj.get_value
    else:
        ttype = generateRandomString("type ", 10)
        tocall = (lambda: generateOutcomeResult(prefix, max_depth - 1))
    return {
        "name" : generateRandomString(currentPrefix, 10),
        "typeRef" : ttype,
        "value" : None if isComposed else generate_value(tocall),
        "components" : None if not isComposed else [tocall() for i in range(random.randint(1,MAX_COMPONENTS_IN_STRUCT))]
    }

def generateOutcomes():
    return {
        "outcomeId" : str(uuid.uuid1()),
        "outcomeName" : generateRandomString("name ", 10),
        "evaluationStatus" : random.choice(EVALUATION_STATES),
        "messages" : [generateRandomString("message ", 10) for i in range(random.randint(0, 4))],
        "hasErrors" : random.choice([True, False]),
        "outcomeResult" : generateOutcomeResult("outcome", MAX_DEPTH)
    }

@app.route('/executions')
@cross_origin()
def get_executions():
    limit = int(request.args.get('limit'))
    offset = int(request.args.get('offset'))
    headers = generateHeaders(random.randint(1,limit))
    response = {"limit" : limit, "offset" : offset, "total" : len(headers), "headers" : headers}
    return json.dumps(response),200,{'content-type':'application/json'}

@app.route('/executions/decisions/<executionId>')
@cross_origin()
def get_execution_by_id(executionId):
    response = generateSingleHeader()
    return json.dumps(response),200,{'content-type':'application/json'}


@app.route('/executions/decisions/<executionId>/outcomes')
@cross_origin()
def get_execution_outcome(executionId):
    response = {"header" : generateSingleHeader(), "outcomes" : [generateOutcomes() for i in range(random.randint(1,20))]}
    return json.dumps(response),200,{'content-type':'application/json'}

def generate_components_for_composite_type(choices, exclude):
    choices = filter(lambda x: x not in exclude, choices)
    return random.choices(choices, k = random.randint(1,10))

@app.route('/executions/decisions/<executionId>/structuredInputs')
@cross_origin()
def get_execution_structured_inputs(executionId):
    # structs = ["structname-" + generateRandomString(10) for i in range(1,10)]
    # structsTypeRef = map(lambda x: "typestruct-" + x, structs)
    # structsTypes = map(lambda x: CompositeType(structs[x], structsTypeRef[x], generate_components_for_composite_type(structs  + BUILT_IN_TYPES, structs[x]), range(1, len(structs))))
    response = {"inputs" : [generateOutcomeResult("input", MAX_DEPTH) for i in range(1, 20)]}
    return json.dumps(response),200,{'content-type':'application/json'}

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=1337)


