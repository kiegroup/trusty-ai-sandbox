ID=$((1 + RANDOM % 5))

case $ID in

  1)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 10000, \"Auth Code\" : \"Authorized\"}, {\"Card Type\" : \"Unsecured Credit\", \"Location\" : \"Foreign\", \"Amount\" : 1000, \"Auth Code\" : \"Denied\"}, {\"Card Type\" : \"Prepaid\", \"Location\" : \"Local\", \"Amount\" : 500, \"Auth Code\" : \"Authorized\"}]}"
    ;;

  2)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 20000, \"Auth Code\" : \"Authorized\"}, {\"Card Type\" : \"Debit\", \"Location\" : \"Foreign\", \"Amount\" : 100, \"Auth Code\" : \"Authorized\", \"Merchant Code\" : \"FOOD\"}, {\"Card Type\" : \"Prepaid\", \"Location\" : \"Foreign\", \"Amount\" : 5, \"Auth Code\" : \"Authorized\", \"Merchant Code\" : \"ICECREAM\"}]}"
    ;;

  3)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 30000, \"Auth Code\" : \"Authorized\"} , {\"Card Type\" : \"Debit\", \"Location\" : \"Foreign\", \"Amount\" : 600, \"Auth Code\" : \"Authorized\", \"Merchant Code\" : \"FOOD\"}]}"
    ;;

  4)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 40000, \"Auth Code\" : \"Authorized\"}, {\"Card Type\" : \"Debit\", \"Location\" : \"Foreign\", \"Amount\" : 1000, \"Auth Code\" : \"Denied\", \"Merchant Code\" : \"VERYBADTHING\"}]}"
    ;;

  5)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 50000, \"Auth Code\" : \"Authorized\"}]}"
    ;;
esac
