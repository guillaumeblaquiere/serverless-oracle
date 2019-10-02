# Overview
This repository present the different way to contact an Oracle database with GCP serverless Product.

App Engine standard and flex, Cloud Run and function are used. Except for App Engine8, the 4 products are usable with the same source code.

Think to customize the configuration files with your values.

# Deployment and tests

## Java
### App Engine Java8
App Engine Java 8 requires specific dependencies and code structure which is not compliant with other GCP product.

Java directory is compliant App Engine Standard Java11, App Engine flex, Cloud Run

```bash
# Go to the directory
cd appEngine8

# Install manually the Oracle maven dependency (impossible to download automatically)
mvn install:install-file -Dfile=src/main/resources/ojdbc7.jar \
    -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=1.0.0 \
    -Dpackaging=jar

# Update the file in src/main/webapp/WEB-INF/appengine-web.xml with your environment variables

# Run Mvn command. Maven 3.5 or above must be installed
mvn clean package appengine:deploy

# Test your App Engine
curl $(gcloud app browse -s java8-serverless-oracle \
    --no-launch-browser)
```

### App Engine Java11
It's not a real Java11 version. It's a Java 8 but fully compliant with Java11 environment.

Fat Jar mode is used to embed the Oracle Jar in the deployment. Only Standard deployment is perform. It's enough and cheaper

In the `pom.xml` files, change the `PROJECT_ID` value with your project id. In the `src/main/appengine` update the `app.yaml` with your env vars values
```bash
# Go to the directory
cd java

# Install manually the Oracle maven dependency (impossible to download automatically)
mvn install:install-file -Dfile=src/main/resources/ojdbc7.jar \
    -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=1.0.0 \
    -Dpackaging=jar

# Run Mvn command. Maven 3.5 or above must be installed
mvn clean package appengine:deploy

# Test your App Engine
curl $(gcloud app browse -s java11-serverless-oracle \
    --no-launch-browser)   
```

### App Engine flexible Java11
For App Engine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

Update the `app-flexible.yaml` with your env vars values
```bash
# Go to the directory
cd java

# Run Mvn command. Maven 3.5 or above must be installed
gcloud app deploy app-flexible.yaml

# Test your App Engine
curl $(gcloud app browse -s java11-serverless-oracle-flex \
    --no-launch-browser)
```

## Cloud Function Java

*This part will be populated when the Java Cloud Function will be in Beta. Alpha program don't allow to share details*

## Cloud Run Java

In the `pom.xml` files, change the `PROJECT_ID` value with your project id

### With Cloud Build
For App Engine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd java

# Run the build
gcloud builds submit --config googlecloudbuild.yaml

# Deploy on Cloud run 
# Change <PROJECT_ID> by your project ID. Change the env vars by your values
gcloud beta run deploy java-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/java-serverless-oracle \
    --set-env-vars ORACLE_IP=<YOUR IP>,ORACLE_SCHEMA=<YOUR SCHEMA>,\
ORACLE_USER=<YOUR USER>,ORACLE_PASSWORD=<YOUR PASSWORD>

# Test your deployment
curl $(gcloud beta run services describe java-serverless-oracle --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```

### With JIB
```bash
# Go to the directory
cd java

# Run the build
mvn clean compile jib:build

# Deploy on Cloud run 
# Change <PROJECT_ID> by your project ID. Change the env vars by your values
gcloud beta run deploy java-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/java-serverless-oracle-jib \
    --set-env-vars ORACLE_IP=<YOUR IP>,ORACLE_SCHEMA=<YOUR SCHEMA>,\
ORACLE_USER=<YOUR USER>,ORACLE_PASSWORD=<YOUR PASSWORD>

# Test your deployment
curl $(gcloud beta run services describe java-serverless-oracle-jib --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```

## Go
### Function
Not applicable (no instant client)

However, if you want to tests

```bash
# Go to the directory
cd nodejs

# copy the dependencies close to the function file
cp go.mod function/

# Deploy the alpha function
# Change the env vars by your values
gcloud beta functions deploy go-oracle-serverless --trigger-http --region us-central1 \
   --runtime go112 --source function --allow-unauthenticated \
   --entry-point OracleConnection \
   --set-env-vars ORACLE_IP=<YOUR IP>,ORACLE_SCHEMA=<YOUR SCHEMA>,\
ORACLE_USER=<YOUR USER>,ORACLE_PASSWORD=<YOUR PASSWORD>

# Clean up the function directory
rm function/go.mod

# Test your function 
gcloud functions call go-oracle-serverless
```

### App Engine Standard
Not applicable (no instant client)

Try this `gcloud app deploy app-standard.yaml` for validating that is doesn't work. Update the `app-standard.yaml` with your env vars values

### App Engine Flexible
For App Engine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

Update the `app-flexible.yaml` with your env vars values
```bash
# Go to the directory
cd go

# Run Mvn command. Maven 3.5 or above must be installed
gcloud app deploy app-flexible.yaml

# Test your App Engine
curl $(gcloud app browse -s go-serverless-oracle-flex \
    --no-launch-browser)
```

### Cloud Run
For App Engine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd go

# Run the build
gcloud builds submit --config googlecloudbuild.yaml

# Deploy on Cloud run 
# Change <PROJECT_ID> by your project ID. Change the env vars by your values
gcloud beta run deploy go-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/go-serverless-oracle \
    --set-env-vars ORACLE_IP=<YOUR IP>,ORACLE_SCHEMA=<YOUR SCHEMA>,\
ORACLE_USER=<YOUR USER>,ORACLE_PASSWORD=<YOUR PASSWORD>

# Test your deployment
curl $(gcloud beta run services describe go-serverless-oracle --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```

## NodeJS
### Function
Not applicable (no instant client)

However, if you want to tests

```bash
# Go to the directory
cd nodejs

# copy the dependencies close to the function file
cp package.json function/

# Deploy the alpha function
# Change the env vars by your values
gcloud beta functions deploy nodejs-oracle-serverless --trigger-http --region us-central1 \
   --runtime nodejs10 --source function --allow-unauthenticated \
   --entry-point oracleConnection \
   --set-env-vars ORACLE_IP=<YOUR IP>,ORACLE_SCHEMA=<YOUR SCHEMA>,\
ORACLE_USER=<YOUR USER>,ORACLE_PASSWORD=<YOUR PASSWORD>

# Clean up the function directory
rm function/package.json

# Test your function 
gcloud functions call nodejs-oracle-serverless
```

### App Engine Standard
Not applicable (no instant client)

Try this `gcloud app deploy app-standard.yaml` for validating that is doesn't work. Update the `app-standard.yaml` with your env vars values


### App Engine Flexible
For App Engine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

Update the `app-flexible.yaml` with your env vars values

```bash
# Go to the directory
cd nodejs

# Run Mvn command. Maven 3.5 or above must be installed
gcloud app deploy app-flexible.yaml

# Test your App Engine
curl $(gcloud app browse -s nodejs-serverless-oracle-flex \
    --no-launch-browser)
```

### Cloud Run
For App Engine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd nodejs

# Run the build
gcloud builds submit --config googlecloudbuild.yaml

# Deploy on Cloud run 
# Change <PROJECT_ID> by your project ID. Change the env vars by your values
gcloud beta run deploy nodejs-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/nodejs-serverless-oracle \
    --set-env-vars ORACLE_IP=<YOUR IP>,ORACLE_SCHEMA=<YOUR SCHEMA>,\
ORACLE_USER=<YOUR USER>,ORACLE_PASSWORD=<YOUR PASSWORD>

# Test your deployment
curl $(gcloud beta run services describe nodejs-serverless-oracle --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```

## Python
### Function
Not applicable (no instant client)

However, if you want to tests

```bash
# Go to the directory
cd python

# copy the dependencies close to the function file
cp requirements.txt function/

# Deploy the alpha function
# Change the env vars by your values
gcloud functions deploy python-oracle-serverless --trigger-http --region us-central1 \
   --runtime python37 --source function --allow-unauthenticated \
   --entry-point oracle_connection \
   --set-env-vars ORACLE_IP=<YOUR IP>,ORACLE_SCHEMA=<YOUR SCHEMA>,\
ORACLE_USER=<YOUR USER>,ORACLE_PASSWORD=<YOUR PASSWORD>

# Clean up the function directory
rm function/requirements.txt

# Test your function 
gcloud functions call python-oracle-serverless
```


### App Engine Standard
Not applicable (no instant client)

Try this `gcloud app deploy app-standard.yaml` for validating that is doesn't work. Update the `app-standard.yaml` with your env vars values

### App Engine Flexible
For App Engine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

Update the `app-flexible.yaml` with your env vars values

```bash
# Go to the directory
cd python

# Run Mvn command. Maven 3.5 or above must be installed
gcloud app deploy app-flexible.yaml

# Test your App Engine
curl $(gcloud app browse -s python-serverless-oracle-flex \
    --no-launch-browser)
```

### Cloud Run
For App Engine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd python

# Run the build
gcloud builds submit --config googlecloudbuild.yaml

# Deploy on Cloud run 
# Change <PROJECT_ID> by your project ID. Change the env vars by your values
gcloud beta run deploy python-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/python-serverless-oracle \
    --set-env-vars ORACLE_IP=<YOUR IP>,ORACLE_SCHEMA=<YOUR SCHEMA>,\
ORACLE_USER=<YOUR USER>,ORACLE_PASSWORD=<YOUR PASSWORD>

# Test your deployment
curl $(gcloud beta run services describe python-serverless-oracle --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```

# License

This library is licensed under Apache 2.0. Full license text is available in
[LICENSE](https://github.com/guillaumeblaquiere/serverless-oracle/tree/master/LICENSE).
