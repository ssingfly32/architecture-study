#!/bin/bash
# docker stats + Actuator 지표를 함께 수집 (after 상태는 Actuator가 있어서 JVM 힙까지 본다)
CONTAINER=$1
OUT=$2
DURATION=$3
echo "timestamp,cpu_percent,mem_usage,mem_percent,jvm_heap_used_bytes,process_cpu_usage" > "$OUT"
END=$((SECONDS + DURATION))
while [ $SECONDS -lt $END ]; do
  STATS=$(docker stats "$CONTAINER" --no-stream --format "{{.CPUPerc}},{{.MemUsage}},{{.MemPerc}}")
  HEAP=$(curl -s "http://localhost:8080/actuator/metrics/jvm.memory.used?tag=area:heap" | grep -o '"value":[0-9.E]*' | head -1 | cut -d: -f2)
  CPU=$(curl -s "http://localhost:8080/actuator/metrics/process.cpu.usage" | grep -o '"value":[0-9.E]*' | head -1 | cut -d: -f2)
  TS=$(date +%H:%M:%S)
  echo "$TS,$STATS,$HEAP,$CPU" >> "$OUT"
  sleep 5
done
