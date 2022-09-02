# dse-graphframe-testing
Repository showing how to do GraphFrame tests in DSE 

# Instructions
## Setup
```
# setup the network
docker network create graph

# pull/start containers for the right dse version
# one of 6.7.7, 6.7.17, or another of the supported versions listed below
DSE_VERSION=6.7.17
./scripts/docker/compose-up.${DSE_VERSION}.sh

# wait until dse is up...
```

Note: When switching dse versions, make sure to delete old container first. 
- Don't just run docker-compose up with new version set - for whatever reason docker has some funky behavior where it doesn't totally restart everything, even though there's no volume etc. So just do this first:

```
docker stop dse && docker rm dse
```

## Using DSE Spark
### Build and run
```
# one of 6.7.7, 6.7.17, or 6.8.23
DSE_VERSION=6.7.17
./scripts/dse/build-and-submit.sh $DSE_VERSION
```

### Just do the spark submit
```
./scripts/dse/spark-submit.sh $DSE_VERSION
```

