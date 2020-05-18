ID=$((1 + RANDOM % 5))

case $ID in

  1)
    curl --location --request POST 'http://localhost:1337/myMortgage' --header 'Content-Type: application/json' -d "{\"Applicant\" : {\"First Name\" : \"Jon\", \"Last Name\" : \"Snow\", \"Age\": 23, \"Email\" : \"jon.snow@castle-black.ws\"}, \"Mortgage Request\" : {\"TotalRequired\" : 1000000, \"NumberInstallments\" : 400}, \"Finantial Situation\": {\"MonthlySalary\" : 1000, \"TotalAsset\" : 1000}}"
    ;;

  2)
    curl --location --request POST 'http://localhost:1337/myMortgage' --header 'Content-Type: application/json' -d "{\"Applicant\" : {\"First Name\" : \"Tyrion\", \"Last Name\" : \"Lannister\", \"Age\": 40, \"Email\" : \"tyrion.lannister@casterly-rock.ws\"}, \"Mortgage Request\" : {\"TotalRequired\" : 100000000, \"NumberInstallments\" : 400}, \"Finantial Situation\": {\"MonthlySalary\" : 10000, \"TotalAsset\" : 1000000}}"
    ;;

  3)
    curl --location --request POST 'http://localhost:1337/myMortgage' --header 'Content-Type: application/json' -d "{\"Applicant\" : {\"First Name\" : \"Daenerys\", \"Last Name\" : \"Targaryen\", \"Age\": 24, \"Email\" : \"daenerys.targaryen@mother-of-dragons.ws\"}, \"Mortgage Request\" : {\"TotalRequired\" : 250000000, \"NumberInstallments\" : 1000}, \"Finantial Situation\": {\"MonthlySalary\" : 200000, \"TotalAsset\" : 65000}}"
    ;;

  4)
    curl --location --request POST 'http://localhost:1337/myMortgage' --header 'Content-Type: application/json' -d "{\"Applicant\" : {\"First Name\" : \"Sansa\", \"Last Name\" : \"Stark\", \"Age\": 21, \"Email\" : \"sansa.stark@winter-is-coming.ws\"}, \"Mortgage Request\" : {\"TotalRequired\" : 550000, \"NumberInstallments\" : 400}, \"Finantial Situation\": {\"MonthlySalary\" : 100000, \"TotalAsset\" : 250000}}"
    ;;

  5)
    curl --location --request POST 'http://localhost:1337/myMortgage' --header 'Content-Type: application/json' -d "{\"Applicant\" : {\"First Name\" : \"Brienne\", \"Last Name\" : \"ofTarth\", \"Age\": 40, \"Email\" : \"brienne.of.tarth@wannabe-knight.ws\"}, \"Mortgage Request\" : {\"TotalRequired\" : 10000, \"NumberInstallments\" : 100}, \"Finantial Situation\": {\"MonthlySalary\" : 1000, \"TotalAsset\" : 2000}}"
    ;;
esac
