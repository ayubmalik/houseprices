#!/bin/bash

csv_url=$1
dest_file=$2

if [[ "$csv_url" == "" || "$dest_file" == "" ]]; then
  echo "usage: $0 <csv_url> <dest_file>"
  exit 1
fi

printf "\nWriting file %s to %s\n" "${csv_url}" "${dest_file}"

curl -o ${dest_file} ${full_url}


