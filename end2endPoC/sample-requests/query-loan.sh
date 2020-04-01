curl --location --request POST 'http://localhost:8080/dmn-loan-eligibility' --header 'Content-Type: application/json' --data-raw '{
"Client": {
"age": 43,
"salary": 1950,
"existing payments": 100
},
"Loan": {
"duration": 15,
"installment": 180
}, "God" : "No"
}'

