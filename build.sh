#!/usr/bin/env bash

set -eux -o pipefail

cd "$(dirname "${BASH_SOURCE[0]}")"

docker-compose -f docker-compose-build.yml build --pull
if [ ${#} -gt 0 ]; then
    command docker-compose -f docker-compose-build.yml up -V "${@}"
else
    command docker-compose -f docker-compose-build.yml up -V
fi

docker cp ticket-checker-server-build:/build/target/ticket-checker-server.jar .