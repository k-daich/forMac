#!/bin/bash

sed 's/(a+)/\033[31;7m\$1\e[0m/g' ./Test.txt



