#!/bin/bash -eux

if [ "$BASH" != "/bin/bash" ]; then
  echo "Please do ./$0"
  exit 1
fi

current_scripts_dir=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
project_root=$current_scripts_dir/../..

export DSE_VERSION=6.7.7
#export UNIFIED_DRIVER_VERSION=4.14.1
#export TINKERPOP_VERSION=3.5.3
export UNIFIED_DRIVER_VERSION=4.14.0
export TINKERPOP_VERSION=3.4.10
# https://docs.datastax.com/en/dse/6.7/dse-admin/datastax_enterprise/releaseNotes/RNdse.html#RNdse677
export SPARK_VERSION=2.2.3

cd $project_root  && \
  mvn -Dspark.version=$SPARK_VERSION -Ddse.version=$DSE_VERSION -Dunified.driver.version=$UNIFIED_DRIVER_VERSION -Dtinkerpop.version=$TINKERPOP_VERSION  clean package
  mvn  -Dspark.version=$SPARK_VERSION -Ddse.version=$DSE_VERSION -Dunified.driver.version=$UNIFIED_DRIVER_VERSION -Dtinkerpop.version=$TINKERPOP_VERSION   dependency:tree


