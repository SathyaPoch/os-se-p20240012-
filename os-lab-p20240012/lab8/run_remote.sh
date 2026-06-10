#!/bin/bash
DISPLAY=:0 SSH_ASKPASS_REQUIRE=force SSH_ASKPASS=/home/pochsathya/os-se-p20240012-/os-lab-p20240012/lab8/askpass.sh /usr/bin/ssh -o StrictHostKeyChecking=no se-sathya-poch@ssh2.rathpisey.site "$@"
