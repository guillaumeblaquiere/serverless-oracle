# Overview
TODO use env variable

            String dbIp = System.getenv("ORACLE_IP");
            String dbSchema = System.getenv("ORACLE_SCHEMA");
            String dbUser = System.getenv("ORACLE_USER");
            String dbPassword = System.getenv("ORACLE_PASSWORD");

# Deployment and tests

## Java
### AppEngine Java8
AppEngine Java 8 requires specific dependencies and code structure which is not compliant with other GCP product.

Java directory is compliant AppEngine Standard Java11, AppEngine flex, Cloud Function, Cloud Run

```bash
# Go to the directory
cd appEngine8

# Run Mvn command. Maven 3.5 or above must be installed
mvn clean package appengine:deploy

# Test your appEngin
gcloud app browse -s java8-serverless-oracle
```

### AppEngine Java11
It's not a real Java11 version. It's a Java 8 but fully compliant with Java11 environment.
The Java8 is kept for the test of Alpha version of Cloud Function Java.

Fat Jar mode is used to embed the Oracle Jar in the deployment. Only Standard deployment is perform. It's enough and cheaper

In the `pom.xml` files, change the `PROJECT_ID` value with your project id
```bash
# Go to the directory
cd java

# Run Mvn command. Maven 3.5 or above must be installed
mvn clean package appengine:deploy

# Test your appEngin
gcloud app browse -s java11-serverless-oracle
```

## Cloud Function Java
The Java8 is kept for the test of Alpha version of Cloud Function Java.
Fat Jar mode is used to embed the Oracle Jar in the deployment

```bash
# Go to the directory
cd java

# Package the fat jar
mvn clean package

# Delete the useless Jar in the Target directory
rm target/original*

# Deploy the alpha function
gcloud alpha functions deploy oracle-serverless --trigger-http --region us-central1 \
   --runtime java8 --source target --allow-unauthenticated \
   --entry-point dev.gblaquiere.serverlessoracle.java.function.OracleConnection.doGet

# Test your function 
gcloud functions call oracle-serverless
```

## Cloud Run Java

In the `pom.xml` files, change the `PROJECT_ID` value with your project id

### With Cloud Build
```bash
# Go to the directory
cd java

# Run the build
gcloud builds submit

# Deploy on Cloud run (Change <PROJECT_ID> by your project ID)
gcloud beta run deploy java-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/java-serverless-oracle

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

# Deploy on Cloud run (Change <PROJECT_ID> by your project ID)
gcloud beta run deploy java-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/java-serverless-oracle-jib

# Test your deployment
curl $(gcloud beta run services describe java-serverless-oracle-jib --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```

## Go
### Function
Not applicable (no instant client)

### AppEngine Standard
Not applicable (no instant client)

Try this `gcloud app deploy app-standard.yaml` for validating that is doesn't work

### AppEngine Flexible
For AppEngine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd go

# Run Mvn command. Maven 3.5 or above must be installed
gcloud app deploy app-flexible.yaml

# Test your appEngin
gcloud app browse -s go-serverless-oracle-flex
```

### Cloud Run
For AppEngine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd go

# Run the build
gcloud builds submit --config googlecloudbuild.yaml

# Deploy on Cloud run (Change <PROJECT_ID> by your project ID)
gcloud beta run deploy go-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/go-serverless-oracle

# Test your deployment
curl $(gcloud beta run services describe go-serverless-oracle --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```

## NodeJS
### Function
Not applicable (no instant client)

### AppEngine Standard
Not applicable (no instant client)

Try this `gcloud app deploy app-standard.yaml` for validating that is doesn't work

### AppEngine Flexible
For AppEngine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd nodejs

# Run Mvn command. Maven 3.5 or above must be installed
gcloud app deploy app-flexible.yaml

# Test your appEngin
gcloud app browse -s nodejs-serverless-oracle-flex
```

### Cloud Run
For AppEngine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd nodejs

# Run the build
gcloud builds submit --config googlecloudbuild.yaml

# Deploy on Cloud run (Change <PROJECT_ID> by your project ID)
gcloud beta run deploy nodejs-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/nodejs-serverless-oracle

# Test your deployment
curl $(gcloud beta run services describe nodejs-serverless-oracle --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```

## Python
### Function
Not applicable (no instant client)

### AppEngine Standard
Not applicable (no instant client)

Try this `gcloud app deploy app-standard.yaml` for validating that is doesn't work

### AppEngine Flexible
For AppEngine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd nodejs

# Run Mvn command. Maven 3.5 or above must be installed
gcloud app deploy app-flexible.yaml

# Test your appEngin
gcloud app browse -s nodejs-serverless-oracle-flex
```

### Cloud Run
For AppEngine flex with custom runtime, `Dockerfile` and `cloudbuild.yaml` file can't be in the same directory.
That's why, `googlecloudbuild.yaml` exists instead of the regular name

```bash
# Go to the directory
cd nodejs

# Run the build
gcloud builds submit --config googlecloudbuild.yaml

# Deploy on Cloud run (Change <PROJECT_ID> by your project ID)
gcloud beta run deploy nodejs-serverless-oracle --region us-central1 --platform managed \
    --allow-unauthenticated --image gcr.io/<PROJECT_ID>/nodejs-serverless-oracle

# Test your deployment
curl $(gcloud beta run services describe nodejs-serverless-oracle --region us-central1 \
    --format "value(status.address.hostname)" --platform managed)
```
