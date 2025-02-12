#!/bin/bash
PUID=${PUID:-1000}
PGID=${PGID:-1000}
echo Remapping UID:GID to ${PUID}:${PGID}...

chown ${PUID}:${PGID} -R /data
chown ${PUID}:${PGID} -R /game

groupadd -g ${PGID} user
useradd -u ${PUID} -g ${PGID} user

su -c "cd /game; ./check_and_start.sh" -s /bin/bash user
