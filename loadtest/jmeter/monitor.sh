#!/bin/bash
# docker stats 기반 CPU/Memory 리소스 모니터링 (before 상태는 Actuator가 없어서 docker stats만 사용)
CONTAINER=$1
OUT=$2
DURATION=$3
INTERVAL=${4:-5}
echo "timestamp,cpu_percent,mem_usage,mem_percent" > "$OUT"
END=$((SECONDS + DURATION))
while [ $SECONDS -lt $END ]; do
  STATS=$(docker stats "$CONTAINER" --no-stream --format "{{.CPUPerc}},{{.MemUsage}},{{.MemPerc}}")
  TS=$(date +%H:%M:%S.%3N)
  echo "$TS,$STATS" >> "$OUT"
  sleep "$INTERVAL"
done
