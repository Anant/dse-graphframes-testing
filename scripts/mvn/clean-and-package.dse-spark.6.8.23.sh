#!/bin/bash -eux

if [ "$BASH" != "/bin/bash" ]; then
  echo "Please do ./$0"
  exit 1
fi

current_scripts_dir=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
project_root=$current_scripts_dir/../..

export DSE_VERSION=6.8.23

# 4.14.1 doesn't work due to removing getSideEffects method from DseGraphTraversal class, so using 4.14.0
#export UNIFIED_DRIVER_VERSION=4.14.1
#export TINKERPOP_VERSION=3.5.3
export UNIFIED_DRIVER_VERSION=4.14.0
export TINKERPOP_VERSION=3.4.10
# https://github.com/datastax/release-notes/blob/master/DSE_6.8_Release_Notes.md#components-versions-for-dse-6823
export SPARK_VERSION=2.4.0

# TODO eventually run with tests as well, but for now cancelling so can test actual execution and running tests separately
cd $project_root  && \
  #mvn -Dspark.version=$SPARK_VERSION -Ddse.version=$DSE_VERSION -Dunified.driver.version=$UNIFIED_DRIVER_VERSION -Dtinkerpop.version=$TINKERPOP_VERSION    clean package
  mvn -Dspark.version=$SPARK_VERSION -Ddse.version=$DSE_VERSION -Dunified.driver.version=$UNIFIED_DRIVER_VERSION -Dtinkerpop.version=$TINKERPOP_VERSION   -Dmaven.test.skip=true clean package
  #mvn  -Dspark.version=$SPARK_VERSION -Ddse.version=$DSE_VERSION -Dunified.driver.version=$UNIFIED_DRIVER_VERSION -Dtinkerpop.version=$TINKERPOP_VERSION   dependency:tree

