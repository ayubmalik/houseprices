#!/bin/bash
# find only running instances
instance_ids=$(aws ec2 describe-instances --output text --filters Name=instance-state-name,Values=running | grep INSTANCES | cut -f8 | tr '\n' ' ')
if [[ -z "${instance_ids}" ]]; then
  echo "No running instances to terminate"
else
  echo "Terminating ${instances_ids}"
  aws ec2 terminate-instances --instance-ids ${instance_ids}
fi

