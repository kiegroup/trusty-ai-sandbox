while true; do (./random-query-dmn-myMortgage.sh ; sleep $(bc -l <<< "scale=2 ; ${RANDOM}/6552")); done
