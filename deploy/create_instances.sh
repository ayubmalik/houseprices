#!/bin/bash

# centos7/ireland
image_id="ami-33734044"

num_instances=${1:-1}
cloud_config=$(<puppet.yml)
key_name="ayubmalik-aws"

aws ec2 run-instances \
  --key-name ${key_name} \
  --image-id ${image_id} \
  --instance-type t2.small \
  --count ${num_instances} \
  --subnet-id subnet-7b5c270c \
  --user-data "${cloud_config}"

