#!/bin/bash -eux

if [ "$BASH" != "/bin/bash" ]; then
  echo "Please do ./$0"
  exit 1
fi

current_scripts_dir=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
project_root=$current_scripts_dir/../..

# builds jar, then copies built jar to docker container, then executes spark-submit job with local master

# explanation:
# - running locally in dev for testing, so set master to local. When live, will run in databricks
cd $project_root  && \
  export DSE_VERSION=6.8.23 && \
  export OSS_SPARK_VERSION=2.4.5 && \
  docker-compose up -d 


