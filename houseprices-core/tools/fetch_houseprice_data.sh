#!/bin/bash
csv_file=http://publicdata.landregistry.gov.uk/market-trend-data/price-paid-data/b/pp-complete.csv
tmp_file=/tmp/pp-complete.csv

printf "Deleting temp file: %s\n" "${tmp_file}"

printf "\nFetching file: %s\nIt's over 3.5GB in size! It may take a while...\n\n" "${csv_file}"

curl -o ${tmp_file} ${csv_file_}

