# Use the offical Golang image to create a build artifact.
# This is based on Debian and sets the GOPATH to /go.
# https://hub.docker.com/_/golang
FROM golang:1.12 as builder

# Copy local code to the container image.
WORKDIR /go/src/function/
COPY go.mod .
ENV GO111MODULE=on
RUN CGO_ENABLED=1 GOOS=linux go mod download

COPY . .
# Perform test for building a clean package
#RUN go test -v ./...
RUN CGO_ENABLED=1 GOOS=linux go build -v -o server

# Use a Docker multi-stage build to create a lean production image.
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds

# Now copy it into our base image.
FROM oraclelinux:7-slim

ARG release=19
ARG update=3

RUN  yum -y install oracle-release-el7 && \
     yum-config-manager --enable ol7_oracle_instantclient && \
     yum -y install oracle-instantclient${release}.${update}-basic && \
     rm -rf /var/cache/yum

COPY --from=builder /go/src/function/server /server
CMD ["/server"]


