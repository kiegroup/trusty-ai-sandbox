ID=$((1 + RANDOM % 5))

case $ID in

  1)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 10000, \"Auth Code\" : \"Authorized\"}]}";
    ;;

  2)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 20000, \"Auth Code\" : \"Authorized\"}]}";
    ;;

  3)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 30000, \"Auth Code\" : \"Authorized\"}]}";
    ;;

  4)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 40000, \"Auth Code\" : \"Authorized\"}]}";
    ;;

  5)
    curl --location --request POST 'http://localhost:1337/fraud-scoring' --header 'Content-Type: application/json' -d "{  \"Transactions\" : [{\"Card Type\" : \"Debit\", \"Location\" : \"Local\", \"Amount\" : 50000, \"Auth Code\" : \"Authorized\"}]}";
    ;;
esac
