while true; do (./random-query-dmn-risk.sh ; sleep $(bc -l <<< "scale=2 ; ${RANDOM}/6552")); done
