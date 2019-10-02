FROM oraclelinux:7-slim

ARG release=19
ARG update=3

RUN  yum -y install oracle-release-el7 && \
     yum-config-manager --enable ol7_oracle_instantclient && \
     yum -y install oracle-instantclient${release}.${update}-basic && \
     rm -rf /var/cache/yum && \
     yum install -y python3 && \
     yum install -y python3-pip

WORKDIR /app

COPY requirements.txt .

RUN pip3 install --no-cache-dir -r requirements.txt

COPY . .
ENV PORT 8080

# Run the web service on container startup.
CMD [ "python3", "server.py" ]
